package tfcflorae.objects.items.groundcover;

import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.objects.items.itemblock.ItemBlockTFC;
import net.minecraft.item.ItemStack;
import tfcflorae.objects.blocks.groundcover.BlockDriftwood;
import tfcflorae.util.OreDictionaryHelper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemDriftwood extends ItemBlockTFC {
	public ItemDriftwood(BlockDriftwood block) {
		super(block);
		OreDictionaryHelper.register(this, "wood");
		OreDictionaryHelper.register(this, "wood_driftwood");
		OreDictionaryHelper.register(this, "driftwood");
	}

	@Nonnull
	@Override
	public Size getSize(ItemStack stack) {
		return Size.SMALL;
	}

	@Nonnull
	@Override
	public Weight getWeight(ItemStack stack) {
		return Weight.LIGHT;
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return getStackSize(stack);
	}
}
