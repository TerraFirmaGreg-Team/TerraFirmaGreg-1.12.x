package tfcflorae.objects.items;

import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.api.capability.size.IItemSize;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import tfcflorae.objects.blocks.wood.fruitwood.BlockFruitDoor;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemFruitDoor extends ItemDoor implements IItemSize {
    public ItemFruitDoor(BlockFruitDoor block) {
        super(block);
    }

    @Nonnull
    @Override
    public Size getSize(ItemStack stack) {
        return Size.VERY_LARGE;
    }

    @Nonnull
    @Override
    public Weight getWeight(ItemStack stack) {
        return Weight.HEAVY;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return getStackSize(stack);
    }
}
