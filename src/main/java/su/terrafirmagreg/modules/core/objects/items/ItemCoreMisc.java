package su.terrafirmagreg.modules.core.objects.items;

import su.terrafirmagreg.api.spi.item.BaseItem;

import su.terrafirmagreg.api.capabilities.size.spi.Size;

import su.terrafirmagreg.api.capabilities.size.spi.Weight;

public class ItemCoreMisc extends BaseItem {

    public ItemCoreMisc(String name, Size size, Weight weight, Object... oreNameParts) {

        getSettings()
                .registryKey("core/" + name)
                .size(size)
                .weight(weight)
                .addOreDict(oreNameParts);
    }

}
