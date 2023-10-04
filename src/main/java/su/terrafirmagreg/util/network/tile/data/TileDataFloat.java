package su.terrafirmagreg.util.network.tile.data;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.MathHelper;
import su.terrafirmagreg.util.network.tile.spi.TileDataBase;
import su.terrafirmagreg.util.util.MathConstants;

public class TileDataFloat
        extends TileDataBase {

    private float value;

    public TileDataFloat(float initialValue) {

        this(initialValue, 1);
    }

    public TileDataFloat(float initialValue, int updateInterval) {

        super(updateInterval);
        this.set(initialValue);
    }

    public void set(float value) {

        if (MathHelper.abs(value - this.value) > MathConstants.FLT_EPSILON) {
            this.value = value;
            this.setDirty(true);
        }
    }

    public float get() {

        return this.value;
    }

    public float add(float value) {

        this.set(this.value + value);
        return this.value;
    }

    @Override
    public void read(PacketBuffer buffer) {

        this.value = buffer.readFloat();
    }

    @Override
    public void write(PacketBuffer buffer) {

        buffer.writeFloat(this.value);
    }

}
