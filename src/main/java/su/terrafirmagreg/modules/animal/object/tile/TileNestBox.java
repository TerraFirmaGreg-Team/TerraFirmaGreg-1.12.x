package su.terrafirmagreg.modules.animal.object.tile;

import su.terrafirmagreg.api.base.object.inventory.api.IItemHandlerSidedCallback;
import su.terrafirmagreg.api.base.object.inventory.spi.ItemHandlerSidedWrapper;
import su.terrafirmagreg.api.base.object.tile.spi.BaseTileTickableInventory;
import su.terrafirmagreg.framework.registry.api.provider.IProviderContainer;
import su.terrafirmagreg.modules.animal.api.type.IAnimal;
import su.terrafirmagreg.modules.animal.client.gui.GuiNestBox;
import su.terrafirmagreg.modules.animal.object.container.ContainerNestBox;
import su.terrafirmagreg.modules.core.capabilities.egg.CapabilityEgg;
import su.terrafirmagreg.modules.core.feature.calendar.Calendar;
import su.terrafirmagreg.modules.core.object.entity.EntitySeatOn;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TileNestBox extends BaseTileTickableInventory
  implements IItemHandlerSidedCallback, IProviderContainer<ContainerNestBox, GuiNestBox> {

  private final IItemHandler inventoryWrapperExtract;

  public TileNestBox() {
    super(4);
    this.inventoryWrapperExtract = new ItemHandlerSidedWrapper(this, inventory, EnumFacing.DOWN);
  }

  @Override
  public void update() {
    super.update();
    if (!world.isRemote) {
      for (int i = 0; i < inventory.getSlots(); i++) {
        ItemStack stack = inventory.getStackInSlot(i);
        if (!stack.isEmpty()) {
          var cap = CapabilityEgg.get(stack);
          if (cap != null && cap.getHatchDay() > 0 && cap.getHatchDay() <= Calendar.PLAYER_TIME.getTotalDays()) {
            Entity baby = cap.getEntity(this.world);
            if (baby != null) {
              if (baby instanceof IAnimal animal) {
                animal.setBirthDay((int) Calendar.PLAYER_TIME.getTotalDays());
              }
              baby.setLocationAndAngles(this.pos.getX(), this.pos.getY() + 0.5D, this.pos.getZ(), 0.0F, 0.0F);
              world.spawnEntity(baby);
              inventory.setStackInSlot(i, ItemStack.EMPTY);
            }
          }
        }
      }
    }
  }

  @Override
  public int getSlotLimit(int slot) {
    return 1;
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stack) {
    return stack.getItem() == Items.EGG;
  }

  public void insertEgg(ItemStack stack) {
    for (int i = 0; i < inventory.getSlots(); i++) {
      if (inventory.insertItem(i, stack, false).isEmpty()) {
        return;
      }
    }
  }

  public boolean hasFreeSlot() {
    for (int i = 0; i < inventory.getSlots(); i++) {
      if (inventory.getStackInSlot(i).isEmpty()) {
        return true;
      }
    }
    return false;
  }

  public boolean hasBird() {
    return getBird() != null;
  }

  @Nullable
  public Entity getBird() {
    return EntitySeatOn.getSittingEntity(this.world, this.pos);
  }

  public void seatOnThis(EntityLiving bird) {
    EntitySeatOn.sitOnBlock(this.world, this.pos, bird, 0.0D);
  }

  @Override
  public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
    return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.DOWN) || super.hasCapability(capability, facing);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
    return (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && facing == EnumFacing.DOWN)
           ? (T) inventoryWrapperExtract
           : super.getCapability(capability, facing);
  }

  @Override
  public boolean canInsert(int slot, ItemStack stack, EnumFacing side) {
    return false;
  }

  @Override
  public boolean canExtract(int slot, EnumFacing side) {
    return side == EnumFacing.DOWN;
  }

  @Override
  public ContainerNestBox getContainer(InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos) {
    return new ContainerNestBox(inventoryPlayer, this);
  }

  @Override
  public GuiNestBox getGuiContainer(InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos) {
    return new GuiNestBox(getContainer(inventoryPlayer, world, state, pos), inventoryPlayer);
  }
}
