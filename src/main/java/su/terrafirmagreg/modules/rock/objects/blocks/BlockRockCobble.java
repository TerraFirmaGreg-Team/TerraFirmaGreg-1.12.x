package su.terrafirmagreg.modules.rock.objects.blocks;

import net.dries007.tfc.api.util.FallingBlockManager;


import su.terrafirmagreg.api.util.OreDictUtils;
import su.terrafirmagreg.modules.rock.api.types.type.RockType;
import su.terrafirmagreg.modules.rock.api.types.variant.block.RockBlockVariant;

public class BlockRockCobble extends BlockRockFallable {

    public BlockRockCobble(RockBlockVariant blockVariant, RockType type) {
        super(blockVariant, type);

        FallingBlockManager.registerFallable(this, blockVariant.getSpecification());
    }

    @Override
    public void onRegisterOreDict() {
        OreDictUtils.register(this, "cobblestone");
    }
}
