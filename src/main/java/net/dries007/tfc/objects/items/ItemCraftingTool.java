package net.dries007.tfc.objects.items;

import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ItemCraftingTool extends ItemMisc {
    public ItemCraftingTool(String string, int durability, Size size, Weight weight, Object... oreNameParts) {
        super(string, size, weight, oreNameParts);
        setMaxDamage(durability);
        setMaxStackSize(1);
        setNoRepair();
    }

    @Override
    public boolean canStack(@Nonnull ItemStack stack) {
        return false;
    }
}
