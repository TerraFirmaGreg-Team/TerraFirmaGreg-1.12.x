package net.dries007.tfc.objects.recipes;

import su.terrafirmagreg.modules.core.capabilities.food.CapabilityFood;

import net.minecraft.item.ItemStack;

import net.dries007.tfc.api.recipes.knapping.KnappingRecipe;
import net.dries007.tfc.api.recipes.knapping.KnappingType;

public class KnappingRecipeFood extends KnappingRecipe {

  private final ItemStack output;

  public KnappingRecipeFood(KnappingType type, boolean outsideSlotRequired, ItemStack output, String... pattern) {
    super(type, outsideSlotRequired, pattern);
    this.output = output;
  }

  @Override
  public ItemStack getOutput(ItemStack input) {
    ItemStack candidate = output.copy();
    return CapabilityFood.updateFoodFromPrevious(input, candidate);
  }
}
