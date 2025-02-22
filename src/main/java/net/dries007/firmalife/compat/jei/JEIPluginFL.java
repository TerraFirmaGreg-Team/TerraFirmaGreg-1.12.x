package net.dries007.firmalife.compat.jei;

import su.terrafirmagreg.modules.device.init.BlocksDevice;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import net.dries007.firmalife.compat.jei.category.DryingRecipeCategory;
import net.dries007.firmalife.compat.jei.category.OvenRecipeCategory;
import net.dries007.firmalife.compat.jei.wrapper.DryingRecipeWrapper;
import net.dries007.firmalife.compat.jei.wrapper.KnappingRecipeWrapperFL;
import net.dries007.firmalife.compat.jei.wrapper.OvenRecipeWrapper;
import net.dries007.firmalife.init.FoodFL;
import net.dries007.firmalife.init.KnappingFL;
import net.dries007.firmalife.registry.BlocksFL;
import net.dries007.firmalife.registry.ItemsFL;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.compat.jei.categories.KnappingCategory;
import net.dries007.tfc.compat.jei.wrappers.KnappingRecipeWrapper;
import net.dries007.tfc.compat.jei.wrappers.SimpleRecipeWrapper;
import net.dries007.tfc.objects.items.ItemsTFC;

import java.util.List;
import java.util.stream.Collectors;

import static net.dries007.firmalife.FirmaLife.MOD_ID;

@JEIPlugin
public class JEIPluginFL implements IModPlugin {

  public static final String OVEN_ID = MOD_ID + ".oven";
  public static final String DRY_ID = MOD_ID + ".drying";
  public static final String KNAP_PUMPKIN_UID = MOD_ID + ".knap.pumpkin";
  public static final String CASTING_UID = MOD_ID + ".casting";


  private static IModRegistry REGISTRY;

  @Override
  public void registerCategories(IRecipeCategoryRegistration registry) {
    registry.addRecipeCategories(new OvenRecipeCategory(registry.getJeiHelpers().getGuiHelper(), OVEN_ID));
    registry.addRecipeCategories(new DryingRecipeCategory(registry.getJeiHelpers().getGuiHelper(), DRY_ID));
    registry.addRecipeCategories(new KnappingCategory(registry.getJeiHelpers().getGuiHelper(), KNAP_PUMPKIN_UID));
  }

  @Override
  public void register(IModRegistry registry) {
    REGISTRY = registry;

    List<SimpleRecipeWrapper> ovenList = TFCRegistries.OVEN.getValuesCollection().stream().map(OvenRecipeWrapper::new).collect(Collectors.toList());
    registry.addRecipes(ovenList, OVEN_ID);
    registry.addRecipeCatalyst(new ItemStack(BlocksFL.OVEN), OVEN_ID);

    List<SimpleRecipeWrapper> dryList = TFCRegistries.DRYING.getValuesCollection().stream().map(DryingRecipeWrapper::new).collect(Collectors.toList());
    registry.addRecipes(dryList, DRY_ID);
    registry.addRecipeCatalyst(new ItemStack(BlocksFL.LEAF_MAT, 1), DRY_ID);

    registry.addIngredientInfo(new ItemStack(ItemsFL.FRUIT_LEAF, 1), VanillaTypes.ITEM, new TextComponentTranslation("jei.tooltip.firmalife.fruit_leaf").getFormattedText());
    registry.addIngredientInfo(new ItemStack(ItemsFL.COCOA_POWDER, 1), VanillaTypes.ITEM, new TextComponentTranslation("jei.tooltip.firmalife.cocoa_powder").getFormattedText());
    registry.addIngredientInfo(new ItemStack(ItemsFL.getFood(FoodFL.PINEAPPLE_CHUNKS), 1), VanillaTypes.ITEM, new TextComponentTranslation("jei.tooltip.firmalife.pineapple_chunks").getFormattedText());

    // Pumpkin Knapping
    List<KnappingRecipeWrapper> pumpkinknapRecipes = TFCRegistries.KNAPPING.getValuesCollection().stream()
      .filter(recipe -> recipe.getType() == KnappingFL.PUMPKIN)
      .map(recipe -> new KnappingRecipeWrapperFL(recipe, registry.getJeiHelpers()
        .getGuiHelper()))
      .collect(Collectors.toList());

    registry.addRecipeCatalyst(new ItemStack(BlocksDevice.CRUCIBLE.get()), CASTING_UID);
    registry.addRecipeCatalyst(new ItemStack(ItemsTFC.FIRED_VESSEL), CASTING_UID);
    registry.addRecipes(pumpkinknapRecipes, KNAP_PUMPKIN_UID);
    registry.addRecipeCatalyst(new ItemStack(Item.getItemFromBlock(BlocksFL.PUMPKIN_FRUIT)), KNAP_PUMPKIN_UID);
  }
}
