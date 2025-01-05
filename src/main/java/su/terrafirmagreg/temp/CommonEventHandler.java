package su.terrafirmagreg.temp;

import su.terrafirmagreg.Tags;
import su.terrafirmagreg.modules.core.plugin.gregtech.recipes.RecipeCoreHandlerList;
import su.terrafirmagreg.modules.device.object.tile.multi.MetaTileEntityGreenhouse;

import net.minecraft.block.Block;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Tags.MOD_ID)
public final class CommonEventHandler {


  @SubscribeEvent
  public static void onRegisterRecipes(RegistryEvent.Register<IRecipe> event) {
    RecipeCoreHandlerList.register();
  }

  @SubscribeEvent
  public static void onNetherPortalActivating(BlockEvent.PortalSpawnEvent event) {
    event.setCanceled(true);
  }

  @SubscribeEvent
  public static void registerBlocks(RegistryEvent.Register<Block> event) {

    MetaTileEntityGreenhouse.addGrasses();
  }
}
