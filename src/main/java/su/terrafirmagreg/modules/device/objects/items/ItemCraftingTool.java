package su.terrafirmagreg.modules.device.objects.items;

import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;

public class ItemCraftingTool extends ItemDeviceMisc {

    public ItemCraftingTool(String name, int durability, Size size, Weight weight, Object... oreNameParts) {
        super(name, size, weight, oreNameParts);

        setNoRepair();
        getSettings()
                .maxCount(1)
                .notCanStack()
                .maxDamage(durability);
    }
}
