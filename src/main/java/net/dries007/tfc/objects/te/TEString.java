package net.dries007.tfc.objects.te;

import su.terrafirmagreg.modules.core.capabilities.food.CapabilityFood;
import su.terrafirmagreg.modules.core.capabilities.food.spi.FoodTrait;
import su.terrafirmagreg.modules.core.feature.calendar.Calendar;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.dries007.tfc.api.recipes.heat.HeatRecipe;

import javax.annotation.Nonnull;

public class TEString extends TEInventory {

  private long lastUpdateTick;

  public TEString() {
    super(1);
  }

  public void tryCook() {
    ItemStack input = inventory.getStackInSlot(0);
    HeatRecipe recipe = HeatRecipe.get(input);
    ItemStack output = recipe != null ? recipe.getOutputStack(input) : input.copy();
    CapabilityFood.updateFoodDecayOnCreate(output);
    CapabilityFood.applyTrait(output, FoodTrait.SMOKED);
    CapabilityFood.removeTrait(output, FoodTrait.BRINED);
    inventory.setStackInSlot(0, output);
    markForSync();
    resetCounter();
  }

  public long getTicksSinceUpdate() {
    return Calendar.PLAYER_TIME.getTicks() - lastUpdateTick;
  }

  public void resetCounter() {
    lastUpdateTick = Calendar.PLAYER_TIME.getTicks();
    markForSync();
  }

  public void reduceCounter(long amount) {
    lastUpdateTick += amount;
    markForSync();
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    lastUpdateTick = nbt.getLong("tick");
    super.readFromNBT(nbt);
  }

  @Nonnull
  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
    nbt.setLong("tick", lastUpdateTick);
    return super.writeToNBT(nbt);
  }

  @Override
  public int getSlotLimit(int slot) {
    return 1;
  }
}
