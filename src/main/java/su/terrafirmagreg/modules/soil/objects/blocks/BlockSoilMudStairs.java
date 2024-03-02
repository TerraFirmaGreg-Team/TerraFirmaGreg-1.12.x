package su.terrafirmagreg.modules.soil.objects.blocks;

import lombok.Getter;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import su.terrafirmagreg.api.spi.itemblock.ItemBlockBase;
import su.terrafirmagreg.modules.soil.api.types.type.SoilType;
import su.terrafirmagreg.modules.soil.api.types.variant.block.ISoilBlockVariant;
import su.terrafirmagreg.modules.soil.api.types.variant.block.SoilBlockVariant;

@Getter
public class BlockSoilMudStairs extends BlockStairs implements ISoilBlockVariant {

	private final SoilBlockVariant blockVariant;
	private final SoilType type;

	public BlockSoilMudStairs(SoilBlockVariant blockVariant, SoilType type) {
		super(Blocks.DIRT.getDefaultState());

		this.blockVariant = blockVariant;
		this.type = type;
		this.useNeighborBrightness = true;

		setSoundType(SoundType.STONE);
		setHarvestLevel("pickaxe", 0);

		//OreDictionaryHelper.register(this, blockVariant.toString(), type.toString());
	}

	@Override
	public ItemBlock getItemBlock() {
		return new ItemBlockBase(this);
	}

	@NotNull
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}


}
