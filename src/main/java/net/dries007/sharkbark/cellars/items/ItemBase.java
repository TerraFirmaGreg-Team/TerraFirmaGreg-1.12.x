package net.dries007.sharkbark.cellars.items;

import net.dries007.tfc.objects.CreativeTabsTFC;
import net.dries007.tfc.objects.items.ItemTFC;
import net.dries007.sharkbark.cellars.Main;
import net.dries007.sharkbark.cellars.init.ModItems;
import net.dries007.sharkbark.cellars.util.IHasModel;

public abstract class ItemBase extends ItemTFC implements IHasModel {

  public ItemBase(String name) {
    //setUnlocalizedName(name);
    setTranslationKey(name);
    setRegistryName(name);
    setCreativeTab(CreativeTabsTFC.CT_METAL);

    ModItems.ITEMS.add(this);
  }

  @Override
  public void registerModels() {
    Main.proxy.registerItemRenderer(this, 0, "inventory");
  }

}
