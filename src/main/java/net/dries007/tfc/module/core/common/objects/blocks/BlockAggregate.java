package net.dries007.tfc.module.core.common.objects.blocks;

import net.dries007.tfc.Tags;
import net.dries007.tfc.api.util.IItemProvider;
import net.dries007.tfc.module.core.common.objects.CreativeTabsTFC;
import net.dries007.tfc.module.core.common.objects.items.itemblocks.ItemBlockTFC;
import net.minecraft.block.BlockGravel;
import net.minecraft.block.SoundType;
import net.minecraft.item.ItemBlock;

import javax.annotation.Nullable;


public class BlockAggregate extends BlockGravel implements IItemProvider {
    public BlockAggregate() {


        setSoundType(SoundType.SAND);
        setHardness(0.4f);

        setCreativeTab(CreativeTabsTFC.ROCK);
        setRegistryName(Tags.MOD_ID, "aggregate");
        setTranslationKey(Tags.MOD_ID + ".aggregate");
    }

    @Nullable
    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockTFC(this);
    }
}
