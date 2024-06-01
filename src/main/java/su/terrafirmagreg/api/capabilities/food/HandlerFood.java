package su.terrafirmagreg.api.capabilities.food;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;


import net.dries007.tfc.api.capability.food.FoodData;
import net.dries007.tfc.api.capability.food.FoodHandler;
import net.dries007.tfc.objects.inventory.ingredient.IIngredient;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class HandlerFood {

    public static final Map<IIngredient<ItemStack>, Supplier<ICapabilityProvider>> CUSTOM_FOODS = new HashMap<>(); //Used inside CT, set custom IFood for food items outside TFC

    public static void init() {
        // Add custom vanilla food instances
        CUSTOM_FOODS.put(IIngredient.of(Items.ROTTEN_FLESH), () -> new FoodHandler(null, FoodData.ROTTEN_FLESH));
        CUSTOM_FOODS.put(IIngredient.of(Items.GOLDEN_APPLE), () -> new FoodHandler(null, FoodData.GOLDEN_APPLE));
        CUSTOM_FOODS.put(IIngredient.of(Items.GOLDEN_CARROT), () -> new FoodHandler(null, FoodData.GOLDEN_CARROT));
        CUSTOM_FOODS.put(IIngredient.of(Items.EGG), () -> new FoodHandler(null, FoodData.RAW_EGG));
    }
}
