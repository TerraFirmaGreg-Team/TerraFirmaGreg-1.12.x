package net.dries007.tfc.compat.jei.wrappers;

import su.terrafirmagreg.modules.core.init.BlocksCore;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.dries007.tfc.api.recipes.ChiselRecipe;
import net.dries007.tfc.compat.jei.TFCJEIPlugin;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class ChiselRecipeWrapper implements IRecipeWrapper {

  private final List<ItemStack> ingredients;
  private final ItemStack output;

  public ChiselRecipeWrapper(ChiselRecipe recipe) {
    ingredients = new ArrayList<>();
    // Although this looks resource-intensive, it's done one time only
    TFCJEIPlugin.getAllIngredients().stream()
      .filter(stack -> stack.getItem() instanceof ItemBlock)
      .forEach(stack ->
      {
        Block block = ((ItemBlock) stack.getItem()).getBlock();
        if (recipe.matches(block.getDefaultState())) {
          ingredients.add(stack);
        }
      });
    // Ideally we should use Block#getPickBlock but we can't have a World and EntityPlayer at this point
    ItemStack recipeOutput = new ItemStack(recipe.getOutputState().getBlock());
    if (recipeOutput.isEmpty()) {
      // Failed to grab the output block, using debug block
      recipeOutput = new ItemStack(BlocksCore.DEBUG.get());
    }
    this.output = recipeOutput;
  }


  @Override
  public void getIngredients(IIngredients recipeIngredients) {
    recipeIngredients.setInputs(VanillaTypes.ITEM, ingredients);
    recipeIngredients.setOutput(VanillaTypes.ITEM, output);
  }
}
