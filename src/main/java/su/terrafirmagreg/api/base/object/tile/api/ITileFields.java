package su.terrafirmagreg.api.base.object.tile.api;

/**
 * in 1.15, use {@link net.minecraft.world.World#notifyBlockUpdate} instead to keep client updated
 */
public interface ITileFields {

  int getFieldCount();

  void setField(int index, int value);

  int getField(int index);
}
