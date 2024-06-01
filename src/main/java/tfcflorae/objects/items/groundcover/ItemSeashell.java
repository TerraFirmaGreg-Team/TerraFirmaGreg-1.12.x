package tfcflorae.objects.items.groundcover;

import net.minecraft.item.ItemStack;


import mcp.MethodsReturnNonnullByDefault;


import su.terrafirmagreg.api.capabilities.size.spi.Size;

import su.terrafirmagreg.api.capabilities.size.spi.Weight;


import net.dries007.tfc.objects.items.itemblock.ItemBlockTFC;
import tfcflorae.objects.blocks.groundcover.BlockSurfaceSeashells;
import tfcflorae.util.OreDictionaryHelper;

import org.jetbrains.annotations.NotNull;

@MethodsReturnNonnullByDefault
public class ItemSeashell extends ItemBlockTFC {

    public ItemSeashell(BlockSurfaceSeashells block) {
        super(block);
        OreDictionaryHelper.register(this, "seashell");
        OreDictionaryHelper.register(this, "seashells");
    }

    @Override
    public @NotNull Size getSize(ItemStack stack) {
        return Size.SMALL;
    }

    @Override
    public @NotNull Weight getWeight(ItemStack stack) {
        return Weight.LIGHT;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return getStackSize(stack);
    }
}
