package net.dries007.tfc.common.objects.items;

import net.dries007.tfc.api.registries.TFCStorage;
import net.dries007.tfc.api.types.crop.type.CropType;
import net.dries007.tfc.common.objects.blocks.crop.BlockCrop;
import net.dries007.tfc.common.objects.blocks.soil.BlockSoilFarmland;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nonnull;

import static net.dries007.tfc.api.types.crop.variant.CropBlockVariants.GROWING;

public class ItemSeedsTFC extends Item implements IPlantable {
    private final CropType type;

    public ItemSeedsTFC(CropType type) {
        this.type = type;
    }

    @Nonnull
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, @Nonnull BlockPos pos, @Nonnull EnumHand hand, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);
        IBlockState state = worldIn.getBlockState(pos);
        if (facing == EnumFacing.UP && player.canPlayerEdit(pos.offset(facing), facing, itemstack) &&
                state.getBlock().canSustainPlant(state, worldIn, pos, EnumFacing.UP, this) &&
                worldIn.isAirBlock(pos.up()) && state.getBlock() instanceof BlockSoilFarmland) {
            worldIn.setBlockState(pos.up(), TFCStorage.getCropBlock(GROWING, type).getDefaultState());

            if (player instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP) player, pos.up(), itemstack);
            }

            itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        } else {
            return EnumActionResult.FAIL;
        }
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    @Override
    public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof BlockCrop && ((BlockCrop) state.getBlock()).getType() == this.type) {
            return state;
        }
        return TFCStorage.getCropBlock(GROWING, type).getDefaultState();
    }

//    @SideOnly(Side.CLIENT)
//    @Override
//    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
//        super.addInformation(stack, worldIn, tooltip, flagIn);
//        variant.addInfo(stack, worldIn, tooltip, flagIn);
//    }
}
