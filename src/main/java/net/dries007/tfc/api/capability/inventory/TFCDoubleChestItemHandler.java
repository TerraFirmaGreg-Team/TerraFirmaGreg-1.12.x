package net.dries007.tfc.api.capability.inventory;

import su.terrafirmagreg.modules.wood.objects.tiles.TEWoodChest;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.VanillaDoubleChestItemHandler;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("WeakerAccess")
public class TFCDoubleChestItemHandler extends VanillaDoubleChestItemHandler {

    public TFCDoubleChestItemHandler(@Nullable TileEntityChest mainChest, @Nullable TileEntityChest other, boolean mainChestIsUpper) {
        super(mainChest, other, mainChestIsUpper);
    }

    @Nullable
    public static VanillaDoubleChestItemHandler get(TileEntityChest chest) {
        World world = chest.getWorld();
        BlockPos pos = chest.getPos();
        //noinspection ConstantConditions
        if (world == null || pos == null || !world.isBlockLoaded(pos))
            return null; // Still loading

        Block blockType = chest.getBlockType();

        EnumFacing[] horizontals = EnumFacing.HORIZONTALS;
        for (int i = horizontals.length - 1; i >= 0; i--)   // Use reverse order so we can return early
        {
            EnumFacing enumfacing = horizontals[i];
            BlockPos blockpos = pos.offset(enumfacing);
            Block block = world.getBlockState(blockpos).getBlock();

            if (block == blockType) {
                TileEntity otherTE = world.getTileEntity(blockpos);

                if (otherTE instanceof TileEntityChest otherChest) {
                    return new TFCDoubleChestItemHandler(chest, otherChest,
                            enumfacing != EnumFacing.WEST && enumfacing != EnumFacing.NORTH);

                }
            }
        }
        return NO_ADJACENT_CHESTS_INSTANCE; //All alone
    }

    @Override
    public int getSlots() {
        return TEWoodChest.SIZE * 2;
    }

    @Override
    @NotNull
    public ItemStack getStackInSlot(int slot) {
        boolean accessingUpperChest = slot < TEWoodChest.SIZE;
        int targetSlot = accessingUpperChest ? slot : slot - TEWoodChest.SIZE;
        TileEntityChest chest = getChest(accessingUpperChest);
        return chest != null ? chest.getStackInSlot(targetSlot) : ItemStack.EMPTY;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        boolean accessingUpperChest = slot < TEWoodChest.SIZE;
        int targetSlot = accessingUpperChest ? slot : slot - TEWoodChest.SIZE;
        TileEntityChest chest = getChest(accessingUpperChest);
        if (chest != null) {
            IItemHandler singleHandler = chest.getSingleChestHandler();
            if (singleHandler instanceof IItemHandlerModifiable) {
                ((IItemHandlerModifiable) singleHandler).setStackInSlot(targetSlot, stack);
            }
        }

        chest = getChest(!accessingUpperChest);
        if (chest != null) {
            chest.markDirty();
        }
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        boolean accessingUpperChest = slot < TEWoodChest.SIZE;
        int targetSlot = accessingUpperChest ? slot : slot - TEWoodChest.SIZE;
        TileEntityChest chest = getChest(accessingUpperChest);
        if (chest == null) {
            return stack;
        }
        if (chest instanceof ISlotCallback && !((ISlotCallback) chest).isItemValid(slot, stack)) {
            return stack;
        }

        int starting = stack.getCount();
        ItemStack ret = chest.getSingleChestHandler().insertItem(targetSlot, stack, simulate);
        if (ret.getCount() != starting && !simulate) {
            chest = getChest(!accessingUpperChest);
            if (chest != null) {
                chest.markDirty();
            }
        }

        return ret;
    }

    @Override
    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        boolean accessingUpperChest = slot < TEWoodChest.SIZE;
        int targetSlot = accessingUpperChest ? slot : slot - TEWoodChest.SIZE;
        TileEntityChest chest = getChest(accessingUpperChest);
        if (chest == null) {
            return ItemStack.EMPTY;
        }

        ItemStack ret = chest.getSingleChestHandler().extractItem(targetSlot, amount, simulate);
        if (!ret.isEmpty() && !simulate) {
            chest = getChest(!accessingUpperChest);
            if (chest != null) {
                chest.markDirty();
            }
        }

        return ret;
    }

    @Override
    public int getSlotLimit(int slot) {
        boolean accessingUpperChest = slot < TEWoodChest.SIZE;
        //noinspection ConstantConditions
        return getChest(accessingUpperChest).getInventoryStackLimit();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        boolean accessingUpperChest = slot < TEWoodChest.SIZE;
        int targetSlot = accessingUpperChest ? slot : slot - TEWoodChest.SIZE;
        TileEntityChest chest = getChest(accessingUpperChest);
        if (chest != null) {
            return chest.getSingleChestHandler().isItemValid(targetSlot, stack);
        }
        return true;
    }
}
