package net.dries007.tfc.api.types2;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import java.util.function.Function;

//public enum FossilType implements IStringSerializable {
//    STICK(BlockLooseStickTFG::new),
//    MIXED_STONE(BlockLooseMixedStoneTFG::new),
//    FLINT(BlockLooseFlintTFG::new);
//
//    private final Function<FossilType, BlockLooseTFG> blockFactory;
//
//    FossilType(Function<FossilType, BlockLooseTFG> blockFactory) {
//        this.blockFactory = blockFactory;
//    }
//
//    public BlockLooseTFG createBlock() {
//        return this.blockFactory.apply(this);
//    }
//
//    @Nonnull
//    @Override
//    public String getName() {
//        return name().toLowerCase();
//    }
//}
