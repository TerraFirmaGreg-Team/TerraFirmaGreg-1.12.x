package net.dries007.tfc.objects.items;

import su.terrafirmagreg.api.registry.provider.IProviderModel;
import su.terrafirmagreg.modules.core.capabilities.food.spi.FoodData;

import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import net.dries007.tfc.api.capability.food.FoodHandler;
import net.dries007.tfc.objects.CreativeTabsTFC;
import net.dries007.caffeineaddon.CaffeineAddon;
import net.dries007.caffeineaddon.init.ModItems;

import org.jetbrains.annotations.Nullable;

public class ItemCoffeeBerries extends ItemFood implements IProviderModel {

  public ItemCoffeeBerries(String name) {
    super(0, 0, false);
    this.setTranslationKey(name);
    this.setRegistryName(name);
    this.setCreativeTab(CreativeTabsTFC.CT_FOOD);
    this.setPotionEffect(new PotionEffect(MobEffects.SPEED, 800, 0), 1f);

    ModItems.ITEMS.add(this);
  }

  @Override
  public void onModelRegister() {
    CaffeineAddon.proxy.registerItemRenderer(this, 0, "inventory");
  }

  @Nullable
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
    return new FoodHandler(nbt, new FoodData(4, 0f, 0f, 0f, 0f, 0.75f, 0f, 0f, 4f));
  }
}
