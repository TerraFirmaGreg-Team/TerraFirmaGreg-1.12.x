package su.terrafirmagreg.api.base.object.block.spi;

import su.terrafirmagreg.api.base.object.block.api.IBlockSettings;
import su.terrafirmagreg.api.base.object.item.spi.BaseItemSlab;
import su.terrafirmagreg.api.data.enums.EnumDefault;
import su.terrafirmagreg.api.util.ModUtils;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import lombok.Getter;

import java.util.Random;

import static su.terrafirmagreg.api.data.Properties.EnumProp.DEFAULT;

@Getter
@SuppressWarnings("deprecation")
public abstract class BaseBlockSlab extends BlockSlab implements IBlockSettings {

  protected final Settings settings;

  public BaseBlockSlab(Settings settings) {
    super(settings.getMaterial());

    this.settings = settings;

    getSettings()
      .ignoresProperties(DEFAULT)
      .itemBlock(this.isDouble() ? null : BaseItemSlab::new)
      .useNeighborBrightness();

    var state = getBlockState().getBaseState();
    if (!isDouble()) {
      state = state.withProperty(HALF, EnumBlockHalf.BOTTOM);
    }
    setDefaultState(state.withProperty(DEFAULT, EnumDefault.DEFAULT));
  }

  @Override
  public String getTranslationKey(int meta) {
    return super.getTranslationKey();
  }

  @Override
  public String getTranslationKey() {
    return ModUtils.localize("block", this.getRegistryName());
  }

  public abstract boolean isDouble();

  @Override
  public IProperty<?> getVariantProperty() {
    return DEFAULT; // why is this not null-tolerable ...
  }

  @Override
  public Comparable<?> getTypeForItem(ItemStack stack) {
    return EnumDefault.DEFAULT;
  }

  @Override
  public boolean getUseNeighborBrightness(IBlockState state) {
    return getSettings().isUseNeighborBrightness();
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    IBlockState iblockstate = this.getDefaultState().withProperty(DEFAULT, EnumDefault.DEFAULT);

    if (!this.isDouble()) {
      iblockstate = iblockstate.withProperty(BlockSlab.HALF, (meta & 8) == 0 ? EnumBlockHalf.BOTTOM : EnumBlockHalf.TOP);
    }

    return iblockstate;
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    int i = 0;

    if (!this.isDouble() && state.getValue(BlockSlab.HALF) == EnumBlockHalf.TOP) {
      i |= 8;
    }

    return i;
  }

  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return Item.getItemFromBlock(getHalfSlab());
  }

  @Override
  public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    return new ItemStack(getHalfSlab());
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return this.isDouble() ? new BlockStateContainer(this, DEFAULT) : new BlockStateContainer(this, HALF, DEFAULT);
  }

  public abstract BaseBlockSlab getHalfSlab();

  public abstract BaseBlockSlab getDoubleSlab();

}
