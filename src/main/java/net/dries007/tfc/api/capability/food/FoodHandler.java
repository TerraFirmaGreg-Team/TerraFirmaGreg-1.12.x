package net.dries007.tfc.api.capability.food;

import su.terrafirmagreg.modules.core.capabilities.food.spi.FoodData;
import su.terrafirmagreg.modules.core.capabilities.food.spi.FoodTrait;
import su.terrafirmagreg.modules.food.ConfigFood;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import net.dries007.tfc.util.agriculture.Food;
import su.terrafirmagreg.modules.core.feature.calendar.Calendar;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FoodHandler implements IFood, ICapabilitySerializable<NBTTagCompound> {

  private static final long ROTTEN_DATE = Long.MIN_VALUE;
  private static final long NEVER_DECAY_DATE = Long.MAX_VALUE;
  private static final long UNKNOWN_CREATION_DATE = 0;

  private static boolean markStacksNonDecaying = true;
  protected final List<FoodTrait> foodTraits;
  protected FoodData data;
  protected long creationDate;
  protected boolean isNonDecaying; // This is intentionally not serialized, as we don't want it to preserve over `ItemStack.copy()` operations

  public FoodHandler() {
    this(null, new FoodData(4, 0, 0, 0, 0, 0, 0, 0, 1));
  }

  public FoodHandler(@Nullable NBTTagCompound nbt, FoodData data) {
    this.foodTraits = new ArrayList<>(2);
    this.data = data;
    this.isNonDecaying = FoodHandler.markStacksNonDecaying;

    deserializeNBT(nbt);
  }

  public FoodHandler(@Nullable NBTTagCompound nbt, @NotNull Food food) {
    this(nbt, food.getData());
  }

  public static void setNonDecaying(boolean markStacksNonDecaying) {
    FoodHandler.markStacksNonDecaying = markStacksNonDecaying;
  }

  /**
   * This marks if the food data should be serialized. For normal food items, it isn't, because all values are provided on construction via CapabilityFood. Only
   * mark this if food data will change per item stack
   */
  protected boolean isDynamic() {
    return false;
  }

  @Override
  public long getCreationDate() {
    if (isNonDecaying) {
      return UNKNOWN_CREATION_DATE;
    }
    if (calculateRottenDate(creationDate) < Calendar.PLAYER_TIME.getTicks()) {
      this.creationDate = ROTTEN_DATE;
    }
    return creationDate;
  }

  @Override
  public void setCreationDate(long creationDate) {
    this.creationDate = creationDate;
  }

  @Override
  public boolean hasCapability(@NotNull Capability<?> capability, @Nullable EnumFacing facing) {
    return capability == CapabilityFood.CAPABILITY;
  }

  @Nullable
  @Override
  @SuppressWarnings("unchecked")
  public <T> T getCapability(@NotNull Capability<T> capability, @Nullable EnumFacing facing) {
    return capability == CapabilityFood.CAPABILITY ? (T) this : null;
  }

  @Override
  public long getRottenDate() {
    if (isNonDecaying) {
      return NEVER_DECAY_DATE;
    }
    if (creationDate == ROTTEN_DATE) {
      return ROTTEN_DATE;
    }
    long rottenDate = calculateRottenDate(creationDate);
    if (rottenDate < Calendar.PLAYER_TIME.getTicks()) {
      return ROTTEN_DATE;
    }
    return rottenDate;
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound nbt = new NBTTagCompound();
    nbt.setLong("creationDate", getCreationDate());
    if (isDynamic()) {
      nbt.setTag("foodData", data.serializeNBT());
    }
    // Traits are sorted so they match when trying to stack them
    NBTTagList traitList = new NBTTagList();
    for (FoodTrait trait : foodTraits) {
      traitList.appendTag(new NBTTagString(trait.getName()));
    }
    nbt.setTag("traits", traitList);
    return nbt;
  }

  @Override
  @NotNull
  public FoodData getData() {
    return data;
  }

  @Override
  public void deserializeNBT(@Nullable NBTTagCompound nbt) {
    foodTraits.clear();
    if (nbt != null) {
      if (isDynamic()) {
        data = new FoodData(nbt.getCompoundTag("foodData"));
      }
      NBTTagList traitList = nbt.getTagList("traits", 8 /* String */);
      for (int i = 0; i < traitList.tagCount(); i++) {
        foodTraits.add(FoodTrait.getTraits().get(traitList.getStringTagAt(i)));
      }
      creationDate = nbt.getLong("creationDate");
    }
    if (creationDate == 0) {
      // Stop defaulting to zero, in cases where the item stack is cloned or copied from one that was initialized at load (and thus was before the calendar was initialized)
      creationDate = CapabilityFood.getRoundedCreationDate();
    }
  }

  @Override
  public float getDecayDateModifier() {
    // Decay modifiers are higher = shorter
    float mod = data.getDecayModifier() * (float) ConfigFood.MISC.DECAY.modifier;
    for (FoodTrait trait : foodTraits) {
      mod *= trait.getDecayModifier();
    }
    // The modifier returned is used to calculate time, so higher = longer
    return mod == 0 ? Float.POSITIVE_INFINITY : 1 / mod;
  }

  @Override
  public void setNonDecaying() {
    isNonDecaying = true;
  }

  @NotNull
  @Override
  public List<FoodTrait> getTraits() {
    return foodTraits;
  }


  private long calculateRottenDate(long creationDateIn) {
    float decayMod = getDecayDateModifier();
    if (decayMod == Float.POSITIVE_INFINITY) {
      // Infinite decay modifier
      return Long.MAX_VALUE;
    }
    return creationDateIn + (long) (decayMod * CapabilityFood.DEFAULT_ROT_TICKS);
  }

}
