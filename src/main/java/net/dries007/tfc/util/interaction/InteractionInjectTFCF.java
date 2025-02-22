package net.dries007.tfc.util.interaction;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import hu.lebeg134.tfc_ph_compat.util.agriculture.TPCrop;
import net.dries007.tfc.api.types.ICrop;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropTFC;
import net.dries007.tfc.objects.blocks.blocktype.farmland.FarmlandTFCF;
import net.dries007.tfc.objects.blocks.stone.BlockFarmlandTFC;
import net.dries007.tfc.objects.items.ItemSeedsTFC;
import net.dries007.tfc.util.agriculture.Crop;
import net.dries007.tfc.util.agriculture.CropTFCF;
import net.dries007.tfcflorae.TFCFlorae;

import javax.annotation.Nonnull;

public class InteractionInjectTFCF {

  @Nonnull
  public static EnumActionResult onItemUse(ItemSeedsTFC itemSeed, EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    ItemStack itemstack = player.getHeldItem(hand);
    IBlockState state = worldIn.getBlockState(pos);
    Block block = state.getBlock();
    if (block instanceof BlockFarmlandTFC) {
      return itemstack.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
    if (facing == EnumFacing.UP && player.canPlayerEdit(pos.offset(facing), facing, itemstack) &&
        block.canSustainPlant(state, worldIn, pos, EnumFacing.UP, itemSeed) &&
        worldIn.isAirBlock(pos.up()) && block instanceof FarmlandTFCF) {

      ICrop seedCrop = null;

      for (Crop crop : Crop.values()) {
        if (itemSeed == ItemSeedsTFC.get(crop)) {
          seedCrop = crop;
        }
      }
      if (seedCrop == null) {
        for (CropTFCF crop : CropTFCF.values()) {
          if (itemSeed == ItemSeedsTFC.get(crop)) {
            seedCrop = crop;
          }
        }
      }

      if (TFCFlorae.TFCPHCompatAdded) {
        if (seedCrop == null) {
          for (TPCrop crop : TPCrop.values()) {
            if (itemSeed == ItemSeedsTFC.get(crop)) {
              seedCrop = crop;
            }
          }
        }
      }

      if (seedCrop == null) {
        TFCFlorae.getLog().error("TFCFlorae: Couldn't find crop to place in TFCFlorae farmland");
        return EnumActionResult.FAIL;
      }

      worldIn.setBlockState(pos.up(), BlockCropTFC.get(seedCrop).getDefaultState());

      itemstack.shrink(1);
      return EnumActionResult.SUCCESS;
    } else {
      return EnumActionResult.FAIL;
    }
  }
}
