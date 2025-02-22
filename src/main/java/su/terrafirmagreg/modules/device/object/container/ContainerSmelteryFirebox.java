package su.terrafirmagreg.modules.device.object.container;

import su.terrafirmagreg.modules.device.object.tile.TileSmelteryFirebox;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import net.dries007.tfc.objects.container.ContainerTE;
import net.dries007.tfc.objects.inventory.slot.SlotCallback;

public class ContainerSmelteryFirebox extends ContainerTE<TileSmelteryFirebox> {

  public ContainerSmelteryFirebox(InventoryPlayer playerInv, TileSmelteryFirebox te) {
    super(playerInv, te);
  }

  @Override
  protected void addContainerSlots() {
    IItemHandler inventory = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    if (inventory != null) {
      for (int i = 0; i < 8; i++) {
        int row = 1 - (i / 4);
        int column = i % 4;
        int x = 53 + column * 18;
        int y = 29 + row * 18;
        addSlotToContainer(new SlotCallback(inventory, i, x, y, tile));
      }
    }
  }
}
