package net.dries007.tfc.compat.jei.util;

import net.dries007.tfc.objects.inventory.ingredient.IIngredient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Wraps simple ItemStacks -> ItemStacks recipes to JEI
 */
public interface IJEISimpleRecipe {
    /**
     * Returns a list of Item Ingredients for JEI
     *
     * @return NonNullList with ItemStack IIngredients
     */
    NonNullList<IIngredient<ItemStack>> getIngredients();

    /**
     * Returns a list of Item Outputs
     *
     * @return NonNullList with ItemStacks
     */
    NonNullList<ItemStack> getOutputs();
}
