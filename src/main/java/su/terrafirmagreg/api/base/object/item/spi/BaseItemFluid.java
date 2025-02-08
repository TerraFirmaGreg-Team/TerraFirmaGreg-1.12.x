package su.terrafirmagreg.api.base.object.item.spi;


import su.terrafirmagreg.api.base.object.item.api.IItemSettings;
import su.terrafirmagreg.api.util.ModUtils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.ItemFluidContainer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;

@Getter
public abstract class BaseItemFluid extends ItemFluidContainer implements IItemSettings {

  protected final Settings settings;
  protected final int capacity;

  public BaseItemFluid(int capacity) {
    super(capacity);

    this.settings = Settings.of();
    this.capacity = capacity;
  }


  @Override
  public String getTranslationKey(ItemStack stack) {
    return this.getTranslationKey();
  }

  @Override
  public String getTranslationKey() {
    return ModUtils.localize("item", this.getRegistryName());
  }


  @Override
  public @Nullable ICapabilityProvider initCapabilities(@NotNull ItemStack stack, @Nullable NBTTagCompound nbt) {
    if (getSettings().getCapability().isEmpty()) {
      return null;
    }
    return definition$initCapabilities(stack, nbt);
  }
}
