package net.dries007.firmalife.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

import net.dries007.firmalife.init.RegistryNamesFL;
import net.dries007.tfc.objects.recipes.CrackingRecipe;
import net.dries007.tfc.objects.recipes.DryingRecipe;
import net.dries007.tfc.objects.recipes.NutRecipe;
import net.dries007.tfc.objects.recipes.OvenRecipe;
import net.dries007.tfc.objects.recipes.PlanterRecipe;
import net.dries007.tfc.objects.recipes.StrainingRecipe;

import static net.dries007.firmalife.FirmaLife.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
@GameRegistry.ObjectHolder(MOD_ID)
public class RegistriesFL {

  @SubscribeEvent
  public static void onNewRegistryEvent(RegistryEvent.NewRegistry event) {
    newRegistry(RegistryNamesFL.OVEN_RECIPE, OvenRecipe.class);
    newRegistry(RegistryNamesFL.DRYING_RECIPE, DryingRecipe.class);
    newRegistry(RegistryNamesFL.PLANTER_QUAD_REGISTRY, PlanterRecipe.class);
    newRegistry(RegistryNamesFL.NUT_TREES_REGISTRY, NutRecipe.class);
    newRegistry(RegistryNamesFL.CRACKING_RECIPE, CrackingRecipe.class);
    newRegistry(RegistryNamesFL.STRAINING_RECIPE, StrainingRecipe.class);
  }

  private static <T extends IForgeRegistryEntry<T>> void newRegistry(ResourceLocation name, Class<T> tClass) {
    IForgeRegistry<T> r = new RegistryBuilder<T>().setName(name).allowModification().setType(tClass).create();
  }
}
