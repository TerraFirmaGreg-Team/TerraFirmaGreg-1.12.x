package su.terrafirmagreg.mixin.minecraft.block;

import su.terrafirmagreg.api.base.block.spi.IBlockSettings;
import su.terrafirmagreg.modules.core.capabilities.size.spi.Size;
import su.terrafirmagreg.modules.core.capabilities.size.spi.Weight;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;


import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(value = Block.class, remap = false)
public abstract class MixinBlock extends IForgeRegistryEntry.Impl<Block> implements IBlockSettings {


  @Shadow
  @Final
  public Material material;

  @Shadow
  public boolean fullBlock = getSettings().isOpaque();


  @Final
  public Settings core_Vintage$settings = Settings.of(Material.AIR);

  @Override
  public Settings getSettings() {
    return core_Vintage$settings;
  }

  /**
   * @author Xikaro
   * @reason Адаптация под ISettingsBlock
   */
  @Override
  @Overwrite
  public boolean isOpaqueCube(IBlockState state) {
    return getSettings() != null && getSettings().isOpaque();
  }

  /**
   * @author Xikaro
   * @reason Адаптация под ISettingsBlock
   */
  @Override
  @Overwrite
  public boolean getUseNeighborBrightness(IBlockState state) {
    return getSettings().isUseNeighborBrightness();
  }

  /**
   * @author Xikaro
   * @reason Адаптация под ISettingsBlock
   */
  @Override
  @Overwrite
  public boolean isFullCube(IBlockState state) {
    return getSettings().isFullCube();
  }

  /**
   * @author Xikaro
   * @reason Адаптация под ISettingsBlock
   */
  @Override
  @Overwrite
  public boolean isCollidable() {
    return getSettings().isCollidable();
  }

  /**
   * @author Xikaro
   * @reason Адаптация под ISettingsBlock
   */
  @Override
  @Overwrite
  public SoundType getSoundType() {
    return getSettings().getSoundType();
  }

  /**
   * @author Xikaro
   * @reason Адаптация под ISettingsBlock
   */
  @Override
  @Overwrite
  public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos,
      EnumFacing face) {
    return isOpaqueCube(state) ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
  }

  /**
   * @author Xikaro
   * @reason Адаптация под ISettingsBlock
   */
  @Override
  @Overwrite
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getRenderLayer() {
    return getSettings().getRenderLayer();
  }

  /**
   * @author Xikaro
   * @reason Адаптация под ISettingsBlock
   */
  @Override
  @Overwrite
  public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
    return getSettings().getHardness().apply(blockState, worldIn, pos);
  }

  /**
   * @author Xikaro
   * @reason Адаптация под ISettingsBlock
   */
  @Override
  @Overwrite
  public float getExplosionResistance(Entity exploder) {
    return getSettings().getResistance() / 5.0F;
  }

  /**
   * @author Xikaro
   * @reason Адаптация под ISettingsBlock
   */
  @Override
  @Overwrite
  public float getSlipperiness(IBlockState state, IBlockAccess world, BlockPos pos,
      @Nullable Entity entity) {
    return getSettings().getSlipperiness().apply(state, world, pos);
  }

  /**
   * @author Xikaro
   * @reason Адаптация под ISettingsBlock
   */
  @Override
  @Overwrite
  public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
    return getSettings().getLightValue().apply(state, world, pos);
  }

  @Override
  public Item asItem() {
    return Item.getItemFromBlock((Block) (Object) this);
  }

  @Override
  public boolean getHasItemSubtypes() {
    return getSettings().isHasItemSubtypes();
  }

  @Override
  public Size getSize(ItemStack stack) {
    return getSettings().getSize();
  }

  @Override
  public Weight getWeight(ItemStack stack) {
    return getSettings().getWeight();
  }

  @Override
  public boolean canStack(ItemStack stack) {
    return getSettings().isCanStack();
  }

}
