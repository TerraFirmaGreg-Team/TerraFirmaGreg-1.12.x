package net.dries007.tfc.objects.te;

import su.terrafirmagreg.api.base.object.tile.spi.BaseTile;

import net.minecraft.util.ITickable;

/**
 * Base class for tickable tile entities Batches sync requests into single packets per tick
 */
public class TETickableBase extends BaseTile implements ITickable {

  private boolean needsClientUpdate;

  @Override
  public void update() {
    if (!world.isRemote && needsClientUpdate) {
      // Batch sync requests into single packets rather than sending them every time markForSync is called
      needsClientUpdate = false;
      super.markForSync();
    }
  }

  @Override
  public void markForSync() {
    needsClientUpdate = true;
  }
}
