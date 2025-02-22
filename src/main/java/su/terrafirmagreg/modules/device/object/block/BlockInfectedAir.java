package su.terrafirmagreg.modules.device.object.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.dries007.sharkbark.cellars.Main;
import net.dries007.sharkbark.cellars.blocks.BlockBase;

import javax.annotation.Nullable;

public class BlockInfectedAir extends BlockBase {

  public BlockInfectedAir(String name, Material material) {
    super(name, material);
  }

  public EnumBlockRenderType getRenderType(IBlockState p_getRenderType_1_) {
    return EnumBlockRenderType.MODEL;
  }

  @Nullable
  public AxisAlignedBB getCollisionBoundingBox(IBlockState p_getCollisionBoundingBox_1_, IBlockAccess p_getCollisionBoundingBox_2_, BlockPos p_getCollisionBoundingBox_3_) {
    return NULL_AABB;
  }

  public boolean isOpaqueCube(IBlockState p_isOpaqueCube_1_) {
    return false;
  }

  public boolean canCollideCheck(IBlockState p_canCollideCheck_1_, boolean p_canCollideCheck_2_) {
    return false;
  }

  public void dropBlockAsItemWithChance(World p_dropBlockAsItemWithChance_1_, BlockPos p_dropBlockAsItemWithChance_2_, IBlockState p_dropBlockAsItemWithChance_3_, float p_dropBlockAsItemWithChance_4_, int p_dropBlockAsItemWithChance_5_) {
  }

  public boolean isReplaceable(IBlockAccess p_isReplaceable_1_, BlockPos p_isReplaceable_2_) {
    return true;
  }

  public boolean isFullCube(IBlockState p_isFullCube_1_) {
    return false;
  }

  public BlockFaceShape getBlockFaceShape(IBlockAccess p_getBlockFaceShape_1_, IBlockState p_getBlockFaceShape_2_, BlockPos p_getBlockFaceShape_3_, EnumFacing p_getBlockFaceShape_4_) {
    return BlockFaceShape.UNDEFINED;
  }

  @Override
  public void registerModels() {
    Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
  }

}
