package net.dries007.tfc.api.capability.heat;

import net.dries007.tfc.TerraFirmaGreg;
import net.dries007.tfc.api.capability.DumbStorage;
import net.dries007.tfc.common.objects.inventory.ingredient.IIngredient;
import net.dries007.tfc.common.objects.items.TFCItems;
import net.dries007.tfc.config.ConfigTFC;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public final class CapabilityItemHeat {
    public static final ResourceLocation KEY = TerraFirmaGreg.getID("item_heat");
    public static final Map<IIngredient<ItemStack>, Supplier<ICapabilityProvider>> CUSTOM_ITEMS = new HashMap<>(); //Used inside CT, set custom IItemHeat for items outside TFC
    @CapabilityInject(IItemHeat.class)
    public static Capability<IItemHeat> ITEM_HEAT_CAPABILITY;

    public static void preInit() {
        CapabilityManager.INSTANCE.register(IItemHeat.class, new DumbStorage<>(), ItemHeatHandler::new);
    }

    public static void init() {
        CapabilityItemHeat.CUSTOM_ITEMS.put(IIngredient.of(Items.EGG), () -> new ItemHeatHandler(null, 1, 480));
        CapabilityItemHeat.CUSTOM_ITEMS.put(IIngredient.of("blockClay"), () -> new ItemHeatHandler(null, 1, 600));
        CapabilityItemHeat.CUSTOM_ITEMS.put(IIngredient.of(TFCItems.GLASS_SHARD), () -> new ItemHeatHandler(null, 1, 1000));
        CapabilityItemHeat.CUSTOM_ITEMS.put(IIngredient.of(TFCItems.STICK_BUNCH), () -> new ItemHeatHandler(null, 1, 200));
        CapabilityItemHeat.CUSTOM_ITEMS.put(IIngredient.of("terracotta"), () -> new ItemHeatHandler(null, 1, 1200));
        CapabilityItemHeat.CUSTOM_ITEMS.put(IIngredient.of(Blocks.IRON_BARS), () -> new ItemHeatHandler(null, 1, 1535));
        CapabilityItemHeat.CUSTOM_ITEMS.put(IIngredient.of(Items.IRON_INGOT), () -> new ItemHeatHandler(null, 0.35F, 1535));
    }

    /**
     * Helper method to adjust temperature towards a value, without overshooting or stuttering
     */
    public static float adjustTempTowards(float temp, float target, float delta) {
        return adjustTempTowards(temp, target, delta, delta);
    }

    public static float adjustTempTowards(float temp, float target, float deltaPositive, float deltaNegative) {
        if (temp < target) {
            return Math.min(temp + deltaPositive, target);
        } else if (temp > target) {
            return Math.max(temp - deltaNegative, target);
        } else {
            return target;
        }
    }

    /**
     * Call this from within {@link IItemHeat#getTemperature()}
     */
    public static float adjustTemp(float temp, float heatCapacity, long ticksSinceUpdate) {
        if (ticksSinceUpdate <= 0) return temp;
        final float newTemp = temp - heatCapacity * (float) ticksSinceUpdate * (float) ConfigTFC.Devices.TEMPERATURE.globalModifier;
        return newTemp < 0 ? 0 : newTemp;
    }

    public static void addTemp(IItemHeat instance) {
        // Default modifier = 3 (2x normal cooling)
        addTemp(instance, 3);
    }

    /**
     * Use this to increase the heat on an IItemHeat instance.
     *
     * @param modifier the modifier for how much this will heat up: 0 - 1 slows down cooling, 1 = no heating or cooling, > 1 heats, 2 heats at the same rate of normal cooling, 2+ heats faster
     */
    public static void addTemp(IItemHeat instance, float modifier) {
        final float temp = instance.getTemperature() + modifier * instance.getHeatCapacity() * (float) ConfigTFC.Devices.TEMPERATURE.globalModifier;
        instance.setTemperature(temp);
    }

    public static float adjustToTargetTemperature(float temp, float burnTemp, int airTicks, int maxTempBonus) {
        boolean hasAir = airTicks > 0;
        float targetTemperature = burnTemp + (hasAir ? MathHelper.clamp(burnTemp, 0, maxTempBonus) : 0);

        if (targetTemperature > 1601)
            targetTemperature = 1601;

        if (temp != targetTemperature) {
            float delta = (float) ConfigTFC.Devices.TEMPERATURE.heatingModifier;
            return adjustTempTowards(temp, targetTemperature, delta * (hasAir ? 2 : 1), delta * (hasAir ? 0.5f : 1));
        }
        return temp;
    }

    @Nullable
    public static ICapabilityProvider getCustomHeat(ItemStack stack) {
        Set<IIngredient<ItemStack>> itemItemSet = CUSTOM_ITEMS.keySet();
        for (IIngredient<ItemStack> ingredient : itemItemSet) {
            if (ingredient.testIgnoreCount(stack)) {
                return CUSTOM_ITEMS.get(ingredient).get();
            }
        }
        return null;
    }
}
