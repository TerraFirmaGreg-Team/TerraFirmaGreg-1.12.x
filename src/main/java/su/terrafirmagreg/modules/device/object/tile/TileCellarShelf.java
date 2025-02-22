package su.terrafirmagreg.modules.device.object.tile;

import su.terrafirmagreg.modules.core.capabilities.food.CapabilityFood;
import su.terrafirmagreg.modules.core.capabilities.food.spi.FoodTrait;
import su.terrafirmagreg.modules.core.capabilities.size.CapabilitySize;
import su.terrafirmagreg.modules.core.capabilities.size.ICapabilitySize;
import su.terrafirmagreg.modules.core.capabilities.size.spi.Size;

import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import net.dries007.sharkbark.cellars.ModConfig;
import net.dries007.sharkbark.cellars.util.Reference;
import net.dries007.tfc.objects.inventory.capability.IItemHandlerSidedCallback;
import net.dries007.tfc.objects.inventory.capability.ItemHandlerSidedWrapper;
import net.dries007.tfc.objects.items.ceramics.ItemSmallVessel;
import net.dries007.tfc.objects.te.TEInventory;

import javax.annotation.Nullable;

public class TileCellarShelf extends TEInventory implements IItemHandlerSidedCallback, ITickable {

  public float temperature = -1;
  //private NonNullList<ItemStack> chestContents = NonNullList.<ItemStack>withSize(14, ItemStack.EMPTY);
  private int cellarTick = -240;    //Because a bunker may be not in the same chunk
  //public int isOpen = 0;
  private int updateTickCounter = 120;
  private int lastUpdate = -1;


  public TileCellarShelf() {
    super(new CellarShelfItemStackHandler(14));
  }

  @Override
  public void update() {
    if (world.isRemote || !Reference.initialized) {
      //System.out.println("I shouldn't update.");
      return;
    }

    if (updateTickCounter % 100 == 0 || lastUpdate == 240) {
      handleItemTicking();
    }
    updateTickCounter++;

    if (lastUpdate >= 0) {
      lastUpdate--;
    }
    if (lastUpdate == -1 && temperature > -1000) {
      temperature = -1000;
    }
  }

  private void handleItemTicking() {
    if (cellarTick >= 0) {
      if (cellarTick > 0) {
        cellarTick--;

        //Syncing
        if (cellarTick == 0) {
          world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
        }
      }
      //Updates shelf contents.
      updateTraits();
    } else {
      cellarTick++;

      //Syncing syncing
      if (cellarTick == 0) {
        world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
      }
    }
  }

  private String getTrait(ItemStack stack, NBTTagCompound nbt) {
    String string = nbt.getString("CellarAddonTemperature");
    if (string.compareTo("cool") == 0) {
      return "cool";
    }
    if (string.compareTo("icy") == 0) {
      return "icy";
    }
    if (string.compareTo("freezing") == 0) {
      return "freezing";
    }
    return "";
  }

  private void removeTrait(ItemStack stack, NBTTagCompound nbt) {
    String string = nbt.getString("CellarAddonTemperature");
    if (string.compareTo("cool") == 0) {
      CapabilityFood.removeTrait(stack, FoodTrait.COOL);
    }
    if (string.compareTo("icy") == 0) {
      CapabilityFood.removeTrait(stack, FoodTrait.ICY);
    }
    if (string.compareTo("freezing") == 0) {
      CapabilityFood.removeTrait(stack, FoodTrait.FREEZING);
    }
    nbt.removeTag("CellarAddonTemperature");
    stack.setTagCompound(nbt);
  }

  private void applyTrait(ItemStack stack, NBTTagCompound nbt, String string, FoodTrait trait) {
    nbt.setString("CellarAddonTemperature", string);
    CapabilityFood.applyTrait(stack, trait);
    stack.setTagCompound(nbt);
  }

  private void updateTraits() {
    for (int x = 0; x < inventory.getSlots(); x++) {
      ItemStack stack = inventory.getStackInSlot(x);
      NBTTagCompound nbt;
      if (stack.hasTagCompound()) {
        nbt = stack.getTagCompound();
      } else {
        nbt = new NBTTagCompound();
      }

      String string = getTrait(stack, nbt);

      if (temperature > ModConfig.coolMaxThreshold || temperature <= -1000) {
        removeTrait(stack, nbt);
      } else if ((temperature <= ModConfig.frozenMaxThreshold && temperature > -1000) && string.compareTo("freezing") != 0) {
        removeTrait(stack, nbt);
        applyTrait(stack, nbt, "freezing", FoodTrait.FREEZING);
      } else if ((temperature <= ModConfig.icyMaxThreshold && temperature > ModConfig.frozenMaxThreshold) && string.compareTo("icy") != 0) {
        removeTrait(stack, nbt);
        applyTrait(stack, nbt, "icy", FoodTrait.ICY);
      } else if ((temperature <= ModConfig.coolMaxThreshold && temperature > ModConfig.icyMaxThreshold) && string.compareTo("cool") != 0) {
        removeTrait(stack, nbt);
        applyTrait(stack, nbt, "cool", FoodTrait.COOL);
      }
    }
  }

  public void updateShelf(float temp) {
    cellarTick = 100;
    temperature = temp;
    lastUpdate = 240;
    //Syncing syncing diving diving
    world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 2);
  }

  public float getTemperature() {
    return temperature;
  }

  private void writeSyncData(NBTTagCompound tagCompound) {
    float temp = (lastUpdate < 0) ? -1000 : temperature;
    tagCompound.setFloat("Temperature", temp);
    tagCompound.setTag("Items", super.serializeNBT());
  }

  private void readSyncData(NBTTagCompound tagCompound) {
    temperature = tagCompound.getFloat("Temperature");
    super.deserializeNBT(tagCompound.getCompoundTag("Items"));
  }

  @Nullable
  @Override
  public SPacketUpdateTileEntity getUpdatePacket() {
    NBTTagCompound tagCompound = new NBTTagCompound();
    writeToNBT(tagCompound);
    writeSyncData(tagCompound);
    return new SPacketUpdateTileEntity(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), 1, tagCompound);
  }

  @Override
  public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
    readFromNBT(packet.getNbtCompound());
    readSyncData(packet.getNbtCompound());
  }

  @Override
  public void onBreakBlock(World world, BlockPos pos, IBlockState state) {
    for (int i = 0; i < 14; ++i) {
      ItemStack stack = inventory.getStackInSlot(i);
      NBTTagCompound nbt;
      if (stack.hasTagCompound()) {
        nbt = stack.getTagCompound();
      } else {
        nbt = new NBTTagCompound();
      }

      removeTrait(stack, nbt);
      stack.setTagCompound(null);
      InventoryHelper.spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), stack);
    }
  }

  @Override
  public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return (T) new ItemHandlerSidedWrapper(this, inventory, facing);
    }
    return super.getCapability(capability, facing);
  }

  @Override
  public boolean canInsert(int i, ItemStack itemStack, EnumFacing enumFacing) {
    return (itemStack.getItem() instanceof ItemFood) || (itemStack.getItem() instanceof ItemSmallVessel);
  }

  @Override
  public boolean canExtract(int i, EnumFacing enumFacing) {
    return true;
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stack) {
    ICapabilitySize sizeCap = CapabilitySize.get(stack);
    if (sizeCap != null) {
      return sizeCap.getSize(stack).isSmallerThan(Size.LARGE);
    }
    return true;
  }


  private static class CellarShelfItemStackHandler extends ItemStackHandler implements IItemHandlerModifiable, IItemHandler, INBTSerializable<NBTTagCompound> {

    public CellarShelfItemStackHandler(int size) {
      super(size);
      this.deserializeNBT(new NBTTagCompound());
    }

    public ItemStack extractItem(int slot, int amount, boolean simulate) {
      ItemStack stack = super.extractItem(slot, amount, simulate);

      NBTTagCompound nbt;
      if (stack.hasTagCompound()) {
        nbt = stack.getTagCompound();
      } else {
        nbt = new NBTTagCompound();
      }

      String string = nbt.getString("CellarAddonTemperature");
      if (string.compareTo("cool") == 0) {
        CapabilityFood.removeTrait(stack, FoodTrait.COOL);
      }
      if (string.compareTo("icy") == 0) {
        CapabilityFood.removeTrait(stack, FoodTrait.ICY);
      }
      if (string.compareTo("freezing") == 0) {
        CapabilityFood.removeTrait(stack, FoodTrait.FREEZING);
      }
      nbt.removeTag("CellarAddonTemperature");
      stack.setTagCompound(null);
      return stack;
    }

  }
}

