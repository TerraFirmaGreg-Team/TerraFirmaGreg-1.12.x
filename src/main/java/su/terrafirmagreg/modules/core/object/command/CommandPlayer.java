package su.terrafirmagreg.modules.core.object.command;

import su.terrafirmagreg.api.base.command.BaseCommand;
import su.terrafirmagreg.api.util.ModUtils;
import su.terrafirmagreg.modules.core.capabilities.food.spi.FoodData;
import su.terrafirmagreg.modules.core.capabilities.food.spi.Nutrient;
import su.terrafirmagreg.modules.core.capabilities.player.CapabilityPlayer;
import su.terrafirmagreg.modules.core.feature.skills.Skill;
import su.terrafirmagreg.modules.core.feature.skills.SkillType;
import su.terrafirmagreg.modules.food.api.FoodStatsTFC;
import su.terrafirmagreg.modules.food.api.IFoodStatsTFC;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;

import net.dries007.tfc.api.capability.food.NutritionStats;
import net.dries007.tfc.util.Helpers;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandPlayer extends BaseCommand {

  @Override
  public String getName() {
    return "player";
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return ModUtils.localize("command", "player.usage");
  }

  @Override
  public void execute(MinecraftServer server, ICommandSender sender, String[] args)
    throws CommandException {
    if (sender.getCommandSenderEntity() instanceof EntityPlayer player) {
      if (args.length < 1) {
        throw new WrongUsageException(
          ModUtils.localize("command", "player.usage_expected_first_argument"));
      }
      ExecuteType executeType = ExecuteType.parse(args[0]);
      if (args.length < 2) {
        throw new WrongUsageException(
          ModUtils.localize("command", "player.usage_expected_second_argument"), args[0]);
      }
      switch (args[1]) {
        case "nutrition":
          executeNutrient(sender, player, executeType);
          break;
        case "skill":
          executeSkill(sender, player, args, executeType);
          break;
        case "food":
          executeFood(sender, player, args, executeType);
          break;
        case "saturation":
          executeSaturation(sender, player, args, executeType);
          break;
        case "water":
          executeWater(sender, player, args, executeType);
          break;
        default:
          throw new WrongUsageException(
            ModUtils.localize("command", "player.usage_expected_second_argument"), args[0]);
      }
    } else {
      throw new WrongUsageException(ModUtils.localize("command", "player.usage_needs_player"));
    }
  }

  private void executeNutrient(ICommandSender sender, EntityPlayer player, ExecuteType executeType)
    throws CommandException {
    NutritionStats nutritionStats = ((IFoodStatsTFC) player.getFoodStats()).getNutrition();
    if (executeType == ExecuteType.SET || executeType == ExecuteType.ADD) {
      throw new WrongUsageException(
        ModUtils.localize("command", "player.usage_cant_set_add_nutrients"));
    } else if (executeType == ExecuteType.RESET) {
      nutritionStats.reset();
      sender.sendMessage(
        new TextComponentTranslation(ModUtils.localize("command", "player.reset_nutrients")));
    } else {
      sender.sendMessage(
        new TextComponentTranslation(ModUtils.localize("command", "player.get_nutrients"),
                                     String.format("%.2f", nutritionStats.getAverageNutrition())));
      for (Nutrient nutrient : Nutrient.values()) {
        sender.sendMessage(new TextComponentTranslation(
          ModUtils.localize("command", "player.get_nutrients_nutrient"),
          new TextComponentTranslation(Helpers.getEnumName(nutrient)),
          String.format("%.2f", nutritionStats.getNutrient(nutrient))
        ));
      }
      FoodData lastRecord = nutritionStats.getMostRecentRecord();
      if (lastRecord != null) {
        float[] nutrients = lastRecord.getNutrients();
        sender.sendMessage(new TextComponentTranslation(
          ModUtils.localize("command", "player.get_nutrients_last_eaten"),
          lastRecord.getHunger(),
          String.format("%.2f", lastRecord.getSaturation()),
          String.format("%.2f", lastRecord.getDecayModifier())
        ));
        for (Nutrient nutrient : Nutrient.values()) {
          sender.sendMessage(new TextComponentTranslation(
            ModUtils.localize("command", "player.get_nutrients_last_eaten_nutrient"),
            new TextComponentTranslation(Helpers.getEnumName(nutrient)),
            String.format("%.2f", nutrients[nutrient.ordinal()])
          ));
        }
      }
    }
  }

  private void executeSkill(ICommandSender sender, EntityPlayer player, String[] args,
                            ExecuteType executeType) throws CommandException {
    if (args.length < 3) {
      throw new WrongUsageException(ModUtils.localize("command", "player.usage_expected_id"),
                                    args[0] + " " + args[1]);
    }
    SkillType<Skill> inputSkill = SkillType.get(args[2].toLowerCase(), Skill.class);
    if (inputSkill == null) {
      throw new WrongUsageException(ModUtils.localize("command", "player.usage_unknown_skill"),
                                    args[2]);
    }
    Skill skill = CapabilityPlayer.getSkill(player, inputSkill);
    if (skill != null) {
      if (executeType == ExecuteType.GET) {
        sender.sendMessage(
          new TextComponentTranslation(ModUtils.localize("command", "player.get_skill"),
                                       inputSkill.getName(), skill.getTotalLevel(),
                                       new TextComponentTranslation(Helpers.getEnumName(skill.getTier())),
                                       skill.getLevel()));
      } else if (executeType == ExecuteType.RESET) {
        skill.setTotalLevel(0);
        sender.sendMessage(
          new TextComponentTranslation(ModUtils.localize("command", "player.set_skill"),
                                       inputSkill.getName(), 0));
      } else {
        if (args.length < 4) {
          throw new WrongUsageException(ModUtils.localize("command", "player.usage_expected_value"),
                                        args[0] + " " + args[1] + " " + args[2]);
        }
        double level = parseDouble(args[3], 0, 4);

        if (executeType == ExecuteType.ADD) {
          skill.addTotalLevel(level / 4.0D);
          sender.sendMessage(
            new TextComponentTranslation(ModUtils.localize("command", "player.add_skill"), level,
                                         inputSkill.getName()));
        } else {
          skill.setTotalLevel(level / 4.0D);
          sender.sendMessage(
            new TextComponentTranslation(ModUtils.localize("command", "player.set_skill"),
                                         inputSkill.getName(), level));
        }
      }
    }
  }

  private void executeFood(ICommandSender sender, EntityPlayer player, String[] args,
                           ExecuteType executeType) throws CommandException {
    FoodStats foodStats = player.getFoodStats();
    if (executeType == ExecuteType.GET) {
      int food = foodStats.getFoodLevel();
      sender.sendMessage(
        new TextComponentTranslation(ModUtils.localize("command", "player.get_food"), food));
    } else if (executeType == ExecuteType.RESET) {
      foodStats.setFoodLevel(20);
      sender.sendMessage(
        new TextComponentTranslation(ModUtils.localize("command", "player.set_food"), 20));
    } else {
      if (args.length < 3) {
        throw new WrongUsageException(ModUtils.localize("command", "player.usage_expected_value"),
                                      args[0] + " " + args[1]);
      }
      int value = parseInt(args[2], 0, 20);
      if (executeType == ExecuteType.SET) {
        foodStats.setFoodLevel(value);
        sender.sendMessage(
          new TextComponentTranslation(ModUtils.localize("command", "player.set_food"), value));
      } else if (executeType == ExecuteType.ADD) {
        value += foodStats.getFoodLevel();
        value = MathHelper.clamp(value, 0, 20);
        foodStats.setFoodLevel(value);
        sender.sendMessage(
          new TextComponentTranslation(ModUtils.localize("command", "player.set_food"), value));
      }
    }
  }

  private void executeSaturation(ICommandSender sender, EntityPlayer player, String[] args,
                                 ExecuteType executeType) throws CommandException {
    FoodStatsTFC foodStats = (FoodStatsTFC) player.getFoodStats();
    if (executeType == ExecuteType.GET) {
      float saturation = foodStats.getSaturationLevel();
      sender.sendMessage(
        new TextComponentTranslation(ModUtils.localize("command", "player.get_saturation"),
                                     saturation));
    } else if (executeType == ExecuteType.RESET) {
      foodStats.setSaturation(0);
      sender.sendMessage(
        new TextComponentTranslation(ModUtils.localize("command", "player.set_saturation"), 0));
    } else {
      if (args.length < 3) {
        throw new WrongUsageException(ModUtils.localize("command", "player.usage_expected_value"),
                                      args[0] + " " + args[1]);
      }
      int value = parseInt(args[2], 0, 20);
      if (executeType == ExecuteType.SET) {
        foodStats.setSaturation(value);
        sender.sendMessage(
          new TextComponentTranslation(ModUtils.localize("command", "player.set_saturation"),
                                       value));
      } else if (executeType == ExecuteType.ADD) {
        value += foodStats.getSaturationLevel();
        value = MathHelper.clamp(value, 0, 20);
        foodStats.setSaturation(value);
        sender.sendMessage(
          new TextComponentTranslation(ModUtils.localize("command", "player.set_saturation"),
                                       value));
      }
    }
  }

  private void executeWater(ICommandSender sender, EntityPlayer player, String[] args,
                            ExecuteType executeType) throws CommandException {
    IFoodStatsTFC foodStats = (IFoodStatsTFC) player.getFoodStats();
    if (executeType == ExecuteType.GET) {
      float thirst = foodStats.getThirst();
      sender.sendMessage(
        new TextComponentTranslation(ModUtils.localize("command", "player.get_water"), thirst));
    } else if (executeType == ExecuteType.RESET) {
      foodStats.setThirst(100);
      sender.sendMessage(
        new TextComponentTranslation(ModUtils.localize("command", "player.set_water"), 100));
    } else {
      if (args.length < 3) {
        throw new WrongUsageException(ModUtils.localize("command", "player.usage_expected_value"),
                                      args[0] + " " + args[1]);
      }
      float value = (float) parseDouble(args[2], 0, 100);
      if (executeType == ExecuteType.SET) {
        foodStats.setThirst(value);
        sender.sendMessage(
          new TextComponentTranslation(ModUtils.localize("command", "player.set_water"), value));
      } else if (executeType == ExecuteType.ADD) {
        foodStats.addThirst(value);
        sender.sendMessage(
          new TextComponentTranslation(ModUtils.localize("command", "player.set_water"), value));
      }
    }
  }

  @Override
  public int getRequiredPermissionLevel() {
    return 2;
  }

  @Override
  public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender,
                                        String[] args, @Nullable BlockPos targetPos) {
    if (args.length == 1) {
      return getListOfStringsMatchingLastWord(args, "get", "set", "reset", "add");
    } else if (args.length == 2) {
      return getListOfStringsMatchingLastWord(args, "nutrition", "skill", "food", "saturation",
                                              "water");
    } else if (args.length == 3) {
      if ("skill".equals(args[2])) {
        return getListOfStringsMatchingLastWord(args, SkillType.getSkills()
                                                               .stream()
                                                               .map(s -> s.getName().toLowerCase())
                                                               .collect(Collectors.toList()));
      }
    }
    return Collections.emptyList();
  }

  private enum ExecuteType {
    SET,
    RESET,
    ADD,
    GET;

    static ExecuteType parse(String text) throws CommandException {
      try {
        return ExecuteType.valueOf(text.toUpperCase());
      } catch (IllegalArgumentException e) {
        throw new WrongUsageException(
          ModUtils.localize("command", "player.usage_expected_first_argument"));
      }
    }
  }
}
