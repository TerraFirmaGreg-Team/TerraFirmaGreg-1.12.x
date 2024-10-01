package net.dries007.tfc.objects.items.food;

import su.terrafirmagreg.api.util.TileUtils;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import net.dries007.tfc.api.capability.food.CapabilityFood;
import net.dries007.tfc.api.capability.food.FoodHandler;
import net.dries007.tfc.objects.items.itemblock.ItemBlockTFC;
import net.dries007.tfc.objects.te.TETickCounter;
import net.dries007.tfc.util.calendar.Calendar;

public class ItemBlockRot extends ItemBlockTFC {

  public ItemBlockRot(Block b) {
    super(b);
  }

  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
    return new FoodHandler();
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    EnumActionResult result = super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    if (worldIn.isRemote) {return result;}

    long foodCreationDate;
    ItemStack stack = player.getHeldItem(hand);
    FoodHandler handler = (FoodHandler) stack.getCapability(CapabilityFood.CAPABILITY, null);
    if (handler != null) {
      foodCreationDate = handler.getCreationDate();
    } else {
      foodCreationDate = Long.MIN_VALUE;
    }

    if (result == EnumActionResult.SUCCESS) {
      TileUtils.getTile(worldIn, pos.offset(facing), TETickCounter.class).ifPresent(tile -> {
        long currentTime = Calendar.PLAYER_TIME.getTicks();
        tile.resetCounter(); //tile counter is at currentTime
        tile.reduceCounter(foodCreationDate - currentTime); //teCounter is now at foodCrationDate
      });
    }
    return result;
  }
}
