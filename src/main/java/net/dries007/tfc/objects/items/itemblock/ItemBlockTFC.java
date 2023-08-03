package net.dries007.tfc.objects.items.itemblock;

import javax.annotation.Nonnull;
import net.dries007.tfc.api.capability.size.IItemSize;
import net.dries007.tfc.api.capability.size.ItemSizeHandler;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockTFC extends ItemBlock implements IItemSize {
    private final IItemSize size;

    public ItemBlockTFC(Block block) {
        this(block, block instanceof IItemSize ? (IItemSize) block : ItemSizeHandler.getDefault());
    }

    public ItemBlockTFC(Block block, IItemSize size) {
        super(block);

        this.size = size;
    }

    @Nonnull
    @Override
    public Size getSize(@Nonnull ItemStack stack) {
        return size.getSize(stack);
    }

    @Nonnull
    @Override
    public Weight getWeight(@Nonnull ItemStack stack) {
        return size.getWeight(stack);
    }

    @Override
    public boolean canStack(@Nonnull ItemStack stack) {
        return size.canStack(stack);
    }

    /**
     * @see net.dries007.tfc.objects.items.ItemTFC#getItemStackLimit(ItemStack)
     */
    @Override
    public int getItemStackLimit(ItemStack stack) {
        return getWeight(stack).getStackSize();
    }
}
