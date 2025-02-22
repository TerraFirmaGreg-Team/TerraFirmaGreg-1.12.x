package net.dries007.tfc.api.recipes.knapping;

import net.minecraft.item.ItemStack;

import net.dries007.tfc.api.types.Rock;
import net.dries007.tfc.api.util.IRockObject;

import java.util.function.Function;

public class KnappingRecipeStone extends KnappingRecipe {

  private final Function<Rock, ItemStack> supplier;

  public KnappingRecipeStone(KnappingType type, Function<Rock, ItemStack> supplier, String... pattern) {
    super(type, false, pattern);
    this.supplier = supplier;
  }

  @Override
  public ItemStack getOutput(ItemStack input) {
    if (input.getItem() instanceof IRockObject rockObject) {
      return supplier.apply(rockObject.getRock(input));
    }
    return ItemStack.EMPTY;
  }
}
