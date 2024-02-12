package su.terrafirmagreg.modules.wood.objects.blocks;

import net.minecraft.init.Blocks;

import su.terrafirmagreg.modules.wood.api.types.type.WoodType;
import su.terrafirmagreg.modules.wood.api.types.variant.block.WoodBlockVariant;


public class BlockWoodPlanks extends BlockWood {

    public BlockWoodPlanks(WoodBlockVariant blockVariant, WoodType type) {
        super(blockVariant, type);

        setHardness(2.0F);
        setResistance(5.0F);
        setHarvestLevel("axe", 0);

        Blocks.FIRE.setFireInfo(this, 5, 20);
    }
}
