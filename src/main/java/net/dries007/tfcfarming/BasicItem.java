package net.dries007.tfcfarming;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class BasicItem extends Item {

  public BasicItem(String registryName) {
    this.setMaxDamage(0);
    this.setCreativeTab(CreativeTabs.MISC);
    setTranslationKey(registryName);
    setRegistryName(registryName);
  }

  public BasicItem(String registryName, int maxDamage) {
    this.setMaxDamage(maxDamage);
    this.setCreativeTab(CreativeTabs.MISC);
    setTranslationKey(registryName);
    setRegistryName(registryName);
  }

}
