package net.dries007.tfc.objects.recipes;

import su.terrafirmagreg.modules.core.capabilities.food.CapabilityFood;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.IForgeRegistryEntry;

import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.compat.jei.IJEISimpleRecipe;
import net.dries007.tfc.objects.inventory.ingredient.IIngredient;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class OvenRecipe extends IForgeRegistryEntry.Impl<OvenRecipe> implements IJEISimpleRecipe {

  private final int duration;
  protected IIngredient<ItemStack> inputItem;
  protected ItemStack outputItem;

  public OvenRecipe(IIngredient<ItemStack> input, ItemStack output, int duration) {
    this.inputItem = input;
    this.outputItem = output;
    this.duration = duration;

    if (inputItem == null || outputItem == null) {
      throw new IllegalArgumentException("Sorry, but you can't have oven recipes that don't have an input and output.");
    }
    if (duration < 1) {
      throw new IllegalArgumentException("Sorry, but oven recipes have to have a duration.");
    }
  }

  @Nullable
  public static OvenRecipe get(ItemStack item) {
    return TFCRegistries.OVEN.getValuesCollection().stream().filter(x -> x.isValidInput(item)).findFirst().orElse(null);
  }

  public static int getDuration(OvenRecipe recipe) {
    return recipe.duration;
  }

  @Nonnull
  public ItemStack getOutputItem(ItemStack stack) {
    return CapabilityFood.updateFoodFromPrevious(stack, outputItem.copy());
  }

  // for JEI
  @Override
  public NonNullList<IIngredient<ItemStack>> getIngredients() {
    return NonNullList.withSize(1, inputItem);
  }

  @Override
  public NonNullList<ItemStack> getOutputs() {
    return NonNullList.withSize(1, outputItem);
  }

  private boolean isValidInput(ItemStack inputItem) {
    return this.inputItem.test(inputItem);
  }
}
