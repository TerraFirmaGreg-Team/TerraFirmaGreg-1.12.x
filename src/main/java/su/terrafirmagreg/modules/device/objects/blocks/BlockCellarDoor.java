package su.terrafirmagreg.modules.device.objects.blocks;

import su.terrafirmagreg.api.spi.block.BaseBlockDoor;
import su.terrafirmagreg.api.util.BlockUtils;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockCellarDoor extends BaseBlockDoor {

    public BlockCellarDoor() {
        super(Settings.of(Material.WOOD));

        getSettings()
                .registryKey("device/cellar/door")
                .soundType(SoundType.WOOD)
                .hardness(2F);

        BlockUtils.setFireInfo(this, 5, 20);
    }
}
