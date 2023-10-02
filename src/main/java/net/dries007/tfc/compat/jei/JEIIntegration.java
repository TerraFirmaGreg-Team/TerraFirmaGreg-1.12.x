package net.dries007.tfc.compat.jei;

import gregtech.api.fluids.MetaFluids;
import gregtech.api.unification.material.Materials;
import gregtech.common.items.ToolItems;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferRegistry;
import net.dries007.tfc.Tags;
import net.dries007.tfc.client.gui.GuiCalendar;
import net.dries007.tfc.client.gui.GuiKnapping;
import net.dries007.tfc.client.gui.GuiNutrition;
import net.dries007.tfc.client.gui.GuiSkills;
import net.dries007.tfc.compat.jei.categories.*;
import net.dries007.tfc.compat.jei.util.TFCInventoryGuiHandler;
import net.dries007.tfc.compat.jei.wrappers.*;
import net.dries007.tfc.module.animal.objects.items.ItemAnimalHide;
import net.dries007.tfc.module.animal.objects.items.ItemAnimalHide.HideType;
import net.dries007.tfc.module.ceramic.init.ItemsCeramic;
import net.dries007.tfc.module.ceramic.objects.items.ItemMold;
import net.dries007.tfc.module.core.api.recipes.heat.HeatRecipeMetalMelting;
import net.dries007.tfc.module.core.api.recipes.knapping.KnappingType;
import net.dries007.tfc.module.core.api.recipes.workbench.SaltingRecipe;
import net.dries007.tfc.module.core.api.recipes.workbench.UnmoldRecipe;
import net.dries007.tfc.module.core.init.BlocksCore;
import net.dries007.tfc.module.core.init.ItemsCore;
import net.dries007.tfc.module.core.init.RegistryCore;
import net.dries007.tfc.module.core.objects.container.ContainerInventoryCrafting;
import net.dries007.tfc.module.devices.client.gui.GuiCrucible;
import net.dries007.tfc.module.devices.client.gui.GuiFirePit;
import net.dries007.tfc.module.devices.init.BlocksDevice;
import net.dries007.tfc.module.metal.client.gui.GuiMetalAnvil;
import net.dries007.tfc.module.metal.objects.blocks.BlockMetalAnvil;
import net.dries007.tfc.module.rock.plugin.jei.wrappers.JEIRecipeWrapperKnapping;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
@JEIPlugin
public final class JEIIntegration implements IModPlugin {
    public static final String ALLOY_UID = Tags.MOD_ID + ".alloy";
    public static final String ANVIL_UID = Tags.MOD_ID + ".anvil";
    public static final String BLAST_FURNACE_UID = Tags.MOD_ID + ".blast_furnace";
    public static final String CASTING_UID = Tags.MOD_ID + ".casting";
    public static final String BLOOMERY_UID = Tags.MOD_ID + ".bloomery";

    public static final String HEAT_UID = Tags.MOD_ID + ".heat";
    public static final String KNAP_CLAY_UID = Tags.MOD_ID + ".knap.clay";
    public static final String KNAP_FIRECLAY_UID = Tags.MOD_ID + ".knap.fireclay";
    public static final String KNAP_LEATHER_UID = Tags.MOD_ID + ".knap.leather";

    public static final String METAL_HEAT_UID = Tags.MOD_ID + ".metal_heat";
    public static final String QUERN_UID = Tags.MOD_ID + ".quern";

    public static final String WELDING_UID = Tags.MOD_ID + ".welding";
    public static final String SCRAPING_UID = Tags.MOD_ID + ".scraping";
    public static final String UNMOLD_UID = Tags.MOD_ID + ".unmold";

    private static IModRegistry REGISTRY;

    /**
     * Helper method to return a collection containing all possible itemstacks registered in JEI
     *
     * @return Collection of ItemStacks
     */
    public static Collection<ItemStack> getAllIngredients() {
        return REGISTRY.getIngredientRegistry().getAllIngredients(VanillaTypes.ITEM);
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        final IGuiHelper gui = registry.getJeiHelpers().getGuiHelper();

        // Add new JEI recipe categories
        registry.addRecipeCategories(
                new AlloyCategory(gui, ALLOY_UID),
                new AnvilCategory(gui, ANVIL_UID),
                new BlastFurnaceCategory(gui, BLAST_FURNACE_UID),
                new BloomeryCategory(gui, BLOOMERY_UID),
                new CastingCategory(gui, CASTING_UID),
                new HeatCategory(gui, HEAT_UID),
                new MetalHeatingCategory(gui, METAL_HEAT_UID),
                new QuernCategory(gui, QUERN_UID),
                new WeldingCategory(gui, WELDING_UID),
                new ScrapingCategory(gui, SCRAPING_UID),
                new UnmoldCategory(gui, UNMOLD_UID)
        );
    }

    @Override
    public void register(IModRegistry registry) {
        REGISTRY = registry;

        // Alloy Recipes
        registry.addRecipeCatalyst(new ItemStack(BlocksDevice.CRUCIBLE), ALLOY_UID);
        registry.addRecipeCatalyst(new ItemStack(ItemsCeramic.FIRED_VESSEL), ALLOY_UID);

        var alloyRecipes = RegistryCore.ALLOYS.getValuesCollection().stream()
                .map(AlloyRecipeWrapper::new)
                .collect(Collectors.toList());

        registry.addRecipes(alloyRecipes, ALLOY_UID);

        // Quern Recipes
        registry.addRecipeCatalyst(new ItemStack(BlocksDevice.QUERN), QUERN_UID);
        registry.addRecipeCatalyst(new ItemStack(ItemsCore.HANDSTONE), QUERN_UID);

        var quernList = RegistryCore.QUERN.getValuesCollection()
                .stream()
                .map(SimpleRecipeWrapper::new)
                .collect(Collectors.toList());

        registry.addRecipes(quernList, QUERN_UID);

        // FirePit
        registry.addRecipeCatalyst(new ItemStack(BlocksDevice.FIREPIT), HEAT_UID);
        registry.addRecipeCatalyst(new ItemStack(BlocksDevice.CHARCOAL_FORGE), HEAT_UID);

        var heatList = RegistryCore.HEAT.getValuesCollection()
                .stream()
                .filter(r -> r.getOutputs().size() > 0 && r.getIngredients().size() > 0)
                .map(HeatRecipeWrapper::new)
                .collect(Collectors.toList());

        registry.addRecipes(heatList, HEAT_UID);

        // Anvil + Welding Recipes
        for (var anvil : BlockMetalAnvil.getAnvilStorage()) {
            registry.addRecipeCatalyst(new ItemStack(anvil), ANVIL_UID);
            registry.addRecipeCatalyst(new ItemStack(anvil), WELDING_UID);
        }

        var anvilList = RegistryCore.ANVIL.getValuesCollection()
                .stream()
                .map(AnvilRecipeWrapper::new)
                .collect(Collectors.toList());

        var weldList = RegistryCore.WELDING.getValuesCollection()
                .stream()
                .map(WeldingRecipeWrapper::new)
                .collect(Collectors.toList());

        registry.addRecipes(anvilList, ANVIL_UID);
        registry.addRecipes(weldList, WELDING_UID);

        // Bloomery Recipes
        registry.addRecipeCatalyst(new ItemStack(BlocksDevice.BLOOMERY), BLOOMERY_UID);

        var bloomeryList = RegistryCore.BLOOMERY.getValuesCollection()
                .stream()
                .map(BloomeryRecipeWrapper::new)
                .collect(Collectors.toList());

        registry.addRecipes(bloomeryList, BLOOMERY_UID);

        // Blast Furnace Recipes
        registry.addRecipeCatalyst(new ItemStack(BlocksDevice.BLAST_FURNACE), BLAST_FURNACE_UID);

        var blastList = RegistryCore.BLAST_FURNACE.getValuesCollection()
                .stream()
                .map(BlastFurnaceRecipeWrapper::new)
                .collect(Collectors.toList());

        registry.addRecipes(blastList, BLAST_FURNACE_UID);


        // Metal Melting Recipes
        registry.addRecipeCatalyst(new ItemStack(BlocksDevice.CRUCIBLE), METAL_HEAT_UID);
        registry.addRecipeCatalyst(new ItemStack(ItemsCeramic.FIRED_VESSEL), METAL_HEAT_UID);

        var heatMetalList = new ArrayList<>();
        getAllIngredients().forEach(stack -> {
            HeatRecipeMetalMelting recipe = (HeatRecipeMetalMelting) RegistryCore.HEAT.getValuesCollection()
                    .stream().filter(x -> x instanceof HeatRecipeMetalMelting)
                    .filter(x -> x.isValidInput(stack, 6))
                    .findFirst().orElse(null);
            if (recipe != null) {
                FluidStack fluidStack = recipe.getOutputFluid(stack);
                // Don't add not meltable (ie: iron ore)
                if (fluidStack != null && MetaFluids.getMaterialFromFluid(fluidStack.getFluid()) == recipe.getMaterial()) {
                    MetalHeatingRecipeWrapper wrapper = new MetalHeatingRecipeWrapper(stack, recipe.getMaterial(), fluidStack.amount, recipe.getTransformTemp());
                    heatMetalList.add(wrapper);
                }
            }
        });

        registry.addRecipes(heatMetalList, METAL_HEAT_UID);

        // Unmold + Casting Recipes
        for (var oreDictStack : OreDictionary.getOres("workbench")) {
            registry.addRecipeCatalyst(oreDictStack, UNMOLD_UID);
        }

        registry.addRecipeCatalyst(new ItemStack(BlocksDevice.CRUCIBLE), CASTING_UID);
        registry.addRecipeCatalyst(new ItemStack(ItemsCeramic.FIRED_VESSEL), CASTING_UID);

        var unmoldList = new ArrayList<UnmoldRecipeWrapper>();
        var castingList = new ArrayList<CastingRecipeWrapper>();

        for (var item : ForgeRegistries.RECIPES) {
            if (!(item instanceof UnmoldRecipe unmoldRecipe)) continue;

            var material = unmoldRecipe.getInputMaterial();
            var orePrefix = ((ItemMold) unmoldRecipe.getInputMold().getItem()).getOrePrefix();

            unmoldList.add(new UnmoldRecipeWrapper(material, orePrefix));
            castingList.add(new CastingRecipeWrapper(material, orePrefix));
        }

        registry.addRecipes(unmoldList, UNMOLD_UID);
        registry.addRecipes(castingList, CASTING_UID);

        // Scrapping Recipes
        registry.addRecipeCatalyst(ToolItems.KNIFE.get(Materials.Iron), SCRAPING_UID);

        var scrapingList = new ArrayList<>();
        for (ItemAnimalHide.HideSize size : ItemAnimalHide.HideSize.values()) {
            scrapingList.add(new ScrapingWrapper(ItemAnimalHide.get(HideType.SOAKED, size), ItemAnimalHide.get(HideType.SCRAPED, size)));
        }
        registry.addRecipes(scrapingList, SCRAPING_UID);

        // Clay Knapping
        registry.addRecipeCatalyst(new ItemStack(Items.CLAY_BALL), KNAP_CLAY_UID);

        var clayknapRecipes = RegistryCore.KNAPPING.getValuesCollection().stream()
                .filter(recipe -> recipe.getType() == KnappingType.CLAY)
                .map(recipe -> new JEIRecipeWrapperKnapping(recipe, registry.getJeiHelpers().getGuiHelper()))
                .collect(Collectors.toList());


        registry.addRecipes(clayknapRecipes, KNAP_CLAY_UID);

        // Fire Clay Knapping
        registry.addRecipeCatalyst(ItemsCore.FIRE_CLAY, KNAP_FIRECLAY_UID);

        var fireclayknapRecipes = RegistryCore.KNAPPING.getValuesCollection().stream()
                .filter(recipe -> recipe.getType() == KnappingType.FIRE_CLAY)
                .map(recipe -> new JEIRecipeWrapperKnapping(recipe, registry.getJeiHelpers().getGuiHelper()))
                .collect(Collectors.toList());


        registry.addRecipes(fireclayknapRecipes, KNAP_FIRECLAY_UID);

        // Leather Knapping
        registry.addRecipeCatalyst(new ItemStack(Items.LEATHER), KNAP_LEATHER_UID);

        var leatherknapRecipes = RegistryCore.KNAPPING.getValuesCollection().stream()
                .filter(recipe -> recipe.getType() == KnappingType.LEATHER)
                .map(recipe -> new JEIRecipeWrapperKnapping(recipe, registry.getJeiHelpers().getGuiHelper()))
                .collect(Collectors.toList());

        registry.addRecipes(leatherknapRecipes, KNAP_LEATHER_UID);


        // Click areas
        registry.addRecipeClickArea(GuiKnapping.class, 97, 44, 22, 15, KNAP_CLAY_UID, KNAP_FIRECLAY_UID, KNAP_LEATHER_UID);
        registry.addRecipeClickArea(GuiMetalAnvil.class, 26, 24, 9, 14, ANVIL_UID, WELDING_UID);
        registry.addRecipeClickArea(GuiCrucible.class, 139, 100, 10, 15, ALLOY_UID);
        registry.addRecipeClickArea(GuiCrucible.class, 82, 100, 10, 15, METAL_HEAT_UID);
        registry.addRecipeClickArea(GuiFirePit.class, 79, 37, 18, 10, HEAT_UID);

        // Fix inventory tab overlap see https://github.com/TerraFirmaCraft/TerraFirmaCraft/issues/646
        registry.addAdvancedGuiHandlers(new TFCInventoryGuiHandler<>(GuiInventory.class));
        registry.addAdvancedGuiHandlers(new TFCInventoryGuiHandler<>(GuiCalendar.class));
        registry.addAdvancedGuiHandlers(new TFCInventoryGuiHandler<>(GuiNutrition.class));
        registry.addAdvancedGuiHandlers(new TFCInventoryGuiHandler<>(GuiSkills.class));

        // Add JEI descriptions for basic mechanics

        registry.addIngredientInfo(new ItemStack(BlocksDevice.PIT_KILN, 1), VanillaTypes.ITEM, new TextComponentTranslation("jei.description.tfc.pit_kiln").getFormattedText());
        registry.addIngredientInfo(new ItemStack(BlocksCore.PLACED_ITEM, 1), VanillaTypes.ITEM, new TextComponentTranslation("jei.description.tfc.placed_item").getFormattedText());
        registry.addIngredientInfo(new ItemStack(Items.COAL, 1, 1), VanillaTypes.ITEM, new TextComponentTranslation("jei.description.tfc.charcoal_pit").getFormattedText());


        //        TFCRegistries.ROCK_CATEGORIES.forEach(category -> registry.addRecipeCatalyst(new ItemStack(ItemRockKnife.get(category)), SCRAPING_UID));

        // Custom handlers
        registry.handleRecipes(SaltingRecipe.class, SaltingRecipeWrapper::new, VanillaRecipeCategoryUid.CRAFTING);

        // ContainerInventoryCrafting - Add ability to transfer recipe items
        IRecipeTransferRegistry transferRegistry = registry.getRecipeTransferRegistry();
        transferRegistry.addRecipeTransferHandler(ContainerInventoryCrafting.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 10, 36);
    }
}
