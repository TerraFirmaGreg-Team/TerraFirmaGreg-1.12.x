package net.dries007.tfc.module.wood.objects.tiles;

import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.module.core.api.capability.size.CapabilityItemSize;
import net.dries007.tfc.common.objects.inventory.capability.ISlotCallback;
import net.dries007.tfc.module.core.api.capability.size.IItemSizeAndWeight;
import net.dries007.tfc.module.core.api.capability.size.Size;
import net.dries007.tfc.module.wood.api.types.type.WoodType;
import net.dries007.tfc.module.wood.objects.blocks.BlockWoodChest;
import net.dries007.tfc.module.wood.objects.container.ContainerWoodChest;
import net.dries007.tfc.module.wood.objects.inventory.capability.WoodDoubleChestItemHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryLargeChest;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TEWoodChest extends TileEntityChest implements ISlotCallback {
    public static final int SIZE = 18;

    private WoodType cachedWoodType;
    private int shadowTicksSinceSync;

    {
        chestContents = NonNullList.withSize(SIZE, ItemStack.EMPTY);
        shadowTicksSinceSync = 0;
    }

    @Nullable
    public WoodType getWood() {
        if (cachedWoodType == null) {
            if (world != null) {
                cachedWoodType = ((BlockWoodChest) world.getBlockState(pos).getBlock()).getType();
            }
        }
        return cachedWoodType;
    }

    @Override
    public int getSizeInventory() {
        return SIZE;
    }

    @Override
    protected boolean isChestAt(@Nonnull BlockPos posIn) {
        if (world == null) return false;

        Block block = this.world.getBlockState(posIn).getBlock();
        return block instanceof BlockWoodChest && ((BlockWoodChest) block).getType() == getWood() && ((BlockChest) block).chestType == getChestType();
    }

    @Override
    public void update() {
        checkForAdjacentChests();
        shadowTicksSinceSync++;

        if (!world.isRemote && numPlayersUsing != 0 && (shadowTicksSinceSync + pos.getX() + pos.getY() + pos.getZ()) % 200 == 0) {
            numPlayersUsing = 0;

            for (EntityPlayer player : world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(pos.add(-5, -5, -5), pos.add(6, 6, 6)))) {
                if (player.openContainer instanceof ContainerWoodChest) {
                    IInventory iinventory = ((ContainerWoodChest) player.openContainer).getLowerChestInventory();
                    if (iinventory == this || iinventory instanceof InventoryLargeChest && ((InventoryLargeChest) iinventory).isPartOfLargeChest(this)) {
                        ++numPlayersUsing;
                    }
                }
            }
        }

        prevLidAngle = lidAngle;

        if (numPlayersUsing > 0 && lidAngle == 0.0F && adjacentChestZNeg == null && adjacentChestXNeg == null) {
            double centerX = pos.getX() + 0.5D;
            double centerZ = pos.getZ() + 0.5D;

            if (adjacentChestZPos != null) {
                centerZ += 0.5D;
            }

            if (adjacentChestXPos != null) {
                centerX += 0.5D;
            }

            world.playSound(null, centerX, pos.getY() + 0.5D, centerZ, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F) {
            float initialAngle = this.lidAngle;
            if (numPlayersUsing > 0) {
                lidAngle += 0.1F;
            } else {
                lidAngle -= 0.1F;
            }

            if (lidAngle > 1.0F) {
                lidAngle = 1.0F;
            }

            if (lidAngle < 0.5F && initialAngle >= 0.5F && adjacentChestZNeg == null && adjacentChestXNeg == null) {
                double centerX = pos.getX() + 0.5D;
                double centerZ = pos.getZ() + 0.5D;

                if (adjacentChestZPos != null) {
                    centerZ += 0.5D;
                }

                if (adjacentChestXPos != null) {
                    centerX += 0.5D;
                }

                world.playSound(null, centerX, pos.getY() + 0.5D, centerZ, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (lidAngle < 0.0F) {
                lidAngle = 0.0F;
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (doubleChestHandler == null || doubleChestHandler.needsRefresh()) {
                doubleChestHandler = WoodDoubleChestItemHandler.get(this);
            }
            if (doubleChestHandler != null && doubleChestHandler != WoodDoubleChestItemHandler.NO_ADJACENT_CHESTS_INSTANCE) {
                return (T) doubleChestHandler;
            }
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    @SideOnly(Side.CLIENT)
    @Nonnull
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos().add(-1, 0, -1), getPos().add(2, 2, 2));
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        // Blocks input from hopper
        IItemSizeAndWeight cap = CapabilityItemSize.getIItemSize(stack);
        if (cap != null) {
            return cap.getSize(stack).isSmallerThan(Size.VERY_LARGE);
        }
        return true;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return isItemValidForSlot(slot, stack);
    }
}
