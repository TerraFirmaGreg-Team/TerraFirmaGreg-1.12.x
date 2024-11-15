package su.terrafirmagreg.modules.device.object.tile;

import su.terrafirmagreg.api.base.tile.BaseTileHorse;
import su.terrafirmagreg.modules.device.object.block.BlockQuernHorse;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import com.google.common.collect.Lists;
import net.dries007.tfc.api.capability.food.CapabilityFood;
import net.dries007.horsepower.recipes.HPRecipeBase;
import net.dries007.horsepower.recipes.HPRecipes;
import net.dries007.horsepower.tileentity.TileHPBase;
import net.dries007.horsepower.util.Localization;

import org.jetbrains.annotations.Nullable;

import lombok.Getter;

import java.awt.Color;

@Getter
public class TileQuernHorse extends BaseTileHorse {

  public ItemStack renderStack = ItemStack.EMPTY;
  public Color grindColor;
  private int currentItemMillTime;
  private int totalItemMillTime;

  public TileQuernHorse() {
    super(3);
  }

  public static void processSecondaries(World world, ItemStack secondary, ItemStack secondaryOutput, HPRecipeBase recipe, TileHPBase teBase) {
    if (!secondary.isEmpty()) {
      int recipeChance = recipe.getSecondaryChance();
      if (recipeChance >= 100 || world.rand.nextInt(100) < recipeChance) {
        if (secondaryOutput.isEmpty()) {
          teBase.setInventorySlotContents(2, secondary.copy());
        } else if (secondaryOutput.isItemEqual(secondary)) {
          secondaryOutput.grow(secondary.getCount());
        }
      }
    }
  }

  public void onBreakBlock(World world, BlockPos pos, IBlockState state) {

  }

  @Override
  public boolean validateArea() {
    if (searchPos == null) {
      searchPos = Lists.newArrayList();

      for (int x = -3; x <= 3; x++) {
        for (int z = -3; z <= 3; z++) {
          if ((x <= 1 && x >= -1) && (z <= 1 && z >= -1)) {
            continue;
          }
          searchPos.add(getPos().add(x, 0, z));
          searchPos.add(getPos().add(x, -1, z));
        }
      }
    }

    for (BlockPos pos : searchPos) {
      if (!getWorld().getBlockState(pos).getBlock().isReplaceable(world, pos)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean targetReached() {
    currentItemMillTime++;

    if (currentItemMillTime >= totalItemMillTime) {
      currentItemMillTime = 0;

      totalItemMillTime = HPRecipes.instance().getGrindstoneTime(getStackInSlot(0), false);
      millItem();
      return true;
    }
    return false;
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
    super.readFromNBT(compound);

    if (getStackInSlot(0).getCount() > 0) {
      currentItemMillTime = compound.getInteger("millTime");
      totalItemMillTime = compound.getInteger("totalMillTime");
    } else {
      currentItemMillTime = 0;
      totalItemMillTime = 1;
    }
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    compound.setInteger("millTime", currentItemMillTime);
    compound.setInteger("totalMillTime", totalItemMillTime);

    return super.writeToNBT(compound);
  }

  @Override
  public int getPositionOffset() {
    return -1;
  }

  private void millItem() {
    if (canWork()) {
      HPRecipeBase recipe = getRecipe();
      ItemStack result = recipe.getOutput();
      ItemStack secondary = recipe.getSecondary();
      //somewhere in here we need to fix the creation date for foods.
      ItemStack input = getStackInSlot(0);
      ItemStack output = getStackInSlot(1);
      ItemStack secondaryOutput = getStackInSlot(2);

      if (output.isEmpty()) {
        ItemStack newOutput = result.copy();
        if (input.hasCapability(CapabilityFood.CAPABILITY, null)) {
          CapabilityFood.updateFoodFromPrevious(input, newOutput);
        }
        setInventorySlotContents(1, newOutput);
      } else if (output.isItemEqual(result)) {
        output.grow(result.getCount());
      }
      processSecondaries(getWorld(), secondary, secondaryOutput, recipe, this);

      input.shrink(1);
      BlockQuernHorse.setState(true, world, pos);
    }
  }

  @Override
  public HPRecipeBase getRecipe() {
    return HPRecipes.instance().getGrindstoneRecipe(getStackInSlot(0), false);
  }

  @Override
  public void setInventorySlotContents(int index, ItemStack stack) {
    ItemStack itemstack = getStackInSlot(index);
    super.setInventorySlotContents(index, stack);

    if ((index == 1 || index == 2) && getStackInSlot(1).isEmpty() && getStackInSlot(2).isEmpty()) {
      BlockQuernHorse.setState(false, world, pos);
    }

    boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
    if (index == 0 && !flag) {
      totalItemMillTime = HPRecipes.instance().getGrindstoneTime(stack, false);
      currentItemMillTime = 0;
    }
    markDirty();
  }

  @Override
  public void markDirty() {
    if (getStackInSlot(1).isEmpty() && getStackInSlot(2).isEmpty()) {
      BlockQuernHorse.setState(false, world, pos);
    }

    if (getStackInSlot(0).isEmpty()) {
      currentItemMillTime = 0;
    }

    super.markDirty();
  }

  @Nullable
  @Override
  public ITextComponent getDisplayName() {
    if (valid) {
      return super.getDisplayName();
    } else {
      return new TextComponentTranslation(Localization.INFO.GRINDSTONE_INVALID.key()).setStyle(new Style().setColor(TextFormatting.RED));
    }
  }

  @Override
  public int getInventoryStackLimit() {
    return 64;
  }

  @Override
  public boolean isItemValidForSlot(int index, ItemStack stack) {
    return index == 0 && HPRecipes.instance().hasGrindstoneRecipe(stack, false);
  }

  @Override
  public int getField(int id) {
    return switch (id) {
      case 0 -> totalItemMillTime;
      case 1 -> currentItemMillTime;
      default -> 0;
    };
  }

  @Override
  public void setField(int id, int value) {
    switch (id) {
      case 0:
        totalItemMillTime = value;
        break;
      case 1:
        currentItemMillTime = value;
    }
  }

  @Override
  public int getFieldCount() {
    return 2;
  }

  @Override
  public String getName() {
    return "container.mill";
  }

  @Override
  public int getOutputSlot() {
    return 2;
  }

  @Override
  public ItemStack getRecipeItemStack() {
    return HPRecipes.instance().getGrindstoneResult(getStackInSlot(0), false);
  }


}
