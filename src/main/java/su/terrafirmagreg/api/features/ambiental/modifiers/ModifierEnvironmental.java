package su.terrafirmagreg.api.features.ambiental.modifiers;

import su.terrafirmagreg.api.capabilities.temperature.ProviderTemperature;
import su.terrafirmagreg.api.features.ambiental.provider.ITemperatureEnvironmentalProvider;
import su.terrafirmagreg.modules.core.ModuleCoreConfig;
import su.terrafirmagreg.modules.core.init.PotionsCore;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.biome.Biome;


import net.dries007.tfc.api.capability.food.IFoodStatsTFC;
import net.dries007.tfc.api.capability.food.Nutrient;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.dries007.tfc.util.climate.ClimateTFC;

import java.util.Optional;

import static su.terrafirmagreg.api.features.ambiental.AmbientalRegistry.ENVIRONMENT;

public class ModifierEnvironmental extends ModifierBase {

    static {
        ENVIRONMENT.register(ModifierEnvironmental::handleGeneralTemperature);
        ENVIRONMENT.register(ModifierEnvironmental::handleTimeOfDay);
        ENVIRONMENT.register(ModifierEnvironmental::handleShade);
        ENVIRONMENT.register(ModifierEnvironmental::handleCozy);
        ENVIRONMENT.register(ModifierEnvironmental::handleThirst);
        ENVIRONMENT.register(ModifierEnvironmental::handleFood);
        ENVIRONMENT.register(ModifierEnvironmental::handleDiet);
        ENVIRONMENT.register(ModifierEnvironmental::handleFire);
        ENVIRONMENT.register(ModifierEnvironmental::handleWater);
        ENVIRONMENT.register(ModifierEnvironmental::handleRain);
        ENVIRONMENT.register(ModifierEnvironmental::handleSprinting);
        ENVIRONMENT.register(ModifierEnvironmental::handleUnderground);
        ENVIRONMENT.register(ModifierEnvironmental::handlePotionEffects);
    }

    public ModifierEnvironmental(String name, float change, float potency) {
        super(name, change, potency);

    }

    public static void computeModifiers(EntityPlayer player, ModifierStorage modifiers) {
        for (ITemperatureEnvironmentalProvider provider : ENVIRONMENT) {
            modifiers.add(provider.getModifier(player));
        }
    }

    public static float getEnvironmentTemperatureWithTimeOfDay(EntityPlayer player) {
        return getEnvironmentTemperature(player) + handleTimeOfDay(player).get().getChange();
    }

    public static float getEnvironmentTemperature(EntityPlayer player) {
        float avg = ClimateTFC.getAvgTemp(player.world, player.getPosition());
        float actual = ClimateTFC.getActualTemp(player.world, player.getPosition());
        if (ModuleCoreConfig.MISC.TEMPERATURE.harsherTemperateAreas) {
            float diff = actual - ProviderTemperature.AVERAGE;
            float sign = Math.signum(diff);
            float generalDiff = Math.abs(avg - 15);
            float mult0 = Math.max(0f, ModuleCoreConfig.MISC.TEMPERATURE.harsherMultiplier - 1f);
            float multiplier = 1 + Math.max(0, 1 - generalDiff / 55) * mult0;
            actual = 20 + (diff + 0.5f * sign);
        }
        return actual;
    }

    public static float getEnvironmentHumidity(EntityPlayer player) {
        return ClimateTFC.getRainfall(player.world, player.getPosition()) / 3000;
    }

    public static Optional<ModifierBase> handleFire(EntityPlayer player) {
        return player.isBurning() ? ModifierBase.defined("on_fire", 4f, 4f) : ModifierBase.none();
    }

    public static Optional<ModifierBase> handleGeneralTemperature(EntityPlayer player) {
        return ModifierBase.defined("environment", getEnvironmentTemperature(player), getEnvironmentHumidity(player));
    }

    public static Optional<ModifierBase> handleTimeOfDay(EntityPlayer player) {
        int dayTicks = (int) (player.world.getWorldTime() % 24000);
        if (dayTicks < 6000) return ModifierBase.defined("morning", 2f, 0);
        else if (dayTicks < 12000) return ModifierBase.defined("afternoon", 4f, 0);
        else if (dayTicks < 18000) return ModifierBase.defined("evening", 1f, 0);
        else return ModifierBase.defined("night", 1f, 0);
    }

    public static Optional<ModifierBase> handleWater(EntityPlayer player) {
        if (player.isInWater()) {
            BlockPos pos = player.getPosition();
            IBlockState state = player.world.getBlockState(pos);
            if (state.getBlock() == FluidsTFC.HOT_WATER.get().getBlock()) {
                return ModifierBase.defined("in_hot_water", 5f, 6f);
            } else if (state.getBlock() == Blocks.LAVA) {
                return ModifierBase.defined("in_lava", 10f, 5f);
            } else if (state.getBlock() == FluidsTFC.SALT_WATER.get().getBlock() && player.world.getBiome(pos)
                    .getTempCategory() == Biome.TempCategory.OCEAN) {
                return ModifierBase.defined("in_ocean_water", -8f, 6f);
            } else {
                return ModifierBase.defined("in_water", -5f, 6f);
            }
        } else {
            return ModifierBase.none();
        }
    }

    public static Optional<ModifierBase> handleRain(EntityPlayer player) {
        if (player.world.isRaining()) {
            if (getSkylight(player) < 15) {
                return ModifierBase.defined("weather", -2f, 0.1f);
            } else {
                return ModifierBase.defined("weather", -4f, 0.3f);
            }
        } else {
            return ModifierBase.none();
        }
    }

    public static Optional<ModifierBase> handleSprinting(EntityPlayer player) {
        if (player.isSprinting()) {
            return ModifierBase.defined("sprint", 2f, 0.3f);
        } else {
            return ModifierBase.none();
        }
    }

    public static Optional<ModifierBase> handleUnderground(EntityPlayer player) {
        if (getSkylight(player) < 2) {
            return ModifierBase.defined("underground", -6f, 0.2f);
        } else {
            return ModifierBase.none();
        }
    }

    public static Optional<ModifierBase> handleShade(EntityPlayer player) {
        int light = getSkylight(player);
        light = Math.max(12, light);
        float temp = getEnvironmentTemperatureWithTimeOfDay(player);
        float avg = ProviderTemperature.AVERAGE;
        if (light < 15 && temp > avg) {
            return ModifierBase.defined("shade", -Math.abs(avg - temp) * 0.6f, 0f);
        } else {
            return ModifierBase.none();
        }
    }

    public static Optional<ModifierBase> handleCozy(EntityPlayer player) {
        int skyLight = getSkylight(player);
        skyLight = Math.max(12, skyLight);
        int blockLight = getBlockLight(player);
        float temp = getEnvironmentTemperatureWithTimeOfDay(player);
        float avg = ProviderTemperature.AVERAGE;
        //		if(EnvironmentalTemperatureProvider.calculateEnclosure(player, 30) && temp < avg - 1) {
        if (skyLight < 15 && blockLight > 4 && temp < avg - 1) {
            return ModifierBase.defined("cozy", Math.abs(avg - 1 - temp) * 0.6f, 0f);
        } else {
            return ModifierBase.none();
        }
    }

    public static Optional<ModifierBase> handleThirst(EntityPlayer player) {
        if (player.getFoodStats() instanceof IFoodStatsTFC stats) {
            if (getEnvironmentTemperatureWithTimeOfDay(player) > ProviderTemperature.AVERAGE + 3 && stats.getThirst() > 80f) {
                return ModifierBase.defined("well_hidrated", -2.5f, 0f);
            }
        }
        return ModifierBase.none();
    }

    public static Optional<ModifierBase> handleFood(EntityPlayer player) {
        if (getEnvironmentTemperatureWithTimeOfDay(player) < ProviderTemperature.AVERAGE - 3 && player.getFoodStats().getFoodLevel() > 14) {
            return ModifierBase.defined("well_fed", 2.5f, 0f);
        }
        return ModifierBase.none();
    }

    public static Optional<ModifierBase> handleDiet(EntityPlayer player) {
        if (player.getFoodStats() instanceof IFoodStatsTFC stats) {
            if (getEnvironmentTemperatureWithTimeOfDay(player) < ProviderTemperature.COOL_THRESHOLD) {
                float grainLevel = stats.getNutrition().getNutrient(Nutrient.GRAIN);
                float meatLevel = stats.getNutrition().getNutrient(Nutrient.PROTEIN);
                return ModifierBase.defined("nutrients", 4f * grainLevel * meatLevel, 0f);
            }
            if (getEnvironmentTemperatureWithTimeOfDay(player) > ProviderTemperature.HOT_THRESHOLD) {
                float fruitLevel = stats.getNutrition().getNutrient(Nutrient.FRUIT);
                float veggieLevel = stats.getNutrition().getNutrient(Nutrient.VEGETABLES);
                return ModifierBase.defined("nutrients", -4f * fruitLevel * veggieLevel, 0f);
            }
        }
        return ModifierBase.none();
    }

    public static Optional<ModifierBase> handlePotionEffects(EntityPlayer player) {
        if (player.isPotionActive(PotionsCore.HYPOTHERMIA)) {
            return ModifierBase.defined("cooling_effect", -10F, 0);
        }
        if (player.isPotionActive(PotionsCore.HYPERTHERMIA)) {
            return ModifierBase.defined("heating_effect", 10F, 0);
        }
        return ModifierBase.none();
    }

    public static int getSkylight(EntityPlayer player) {
        BlockPos pos = new BlockPos(player.getPosition());
        BlockPos pos2 = pos.add(0, 1, 0);
        return player.world.getLightFor(EnumSkyBlock.SKY, pos2);
    }

    public static int getBlockLight(EntityPlayer player) {
        BlockPos pos = new BlockPos(player.getPosition());
        BlockPos pos2 = pos.add(0, 1, 0);
        return player.world.getLightFor(EnumSkyBlock.BLOCK, pos2);
    }

}
