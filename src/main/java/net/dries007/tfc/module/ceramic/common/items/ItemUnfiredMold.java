package net.dries007.tfc.module.ceramic.common.items;

import gregtech.api.unification.ore.OrePrefix;
import net.dries007.tfc.TerraFirmaGreg;
import net.dries007.tfc.common.objects.CreativeTabsTFC;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;

public class ItemUnfiredMold extends ItemPottery {

    public final OrePrefix orePrefix;

    public ItemUnfiredMold(OrePrefix orePrefix) {
        this.orePrefix = orePrefix;

        setCreativeTab(CreativeTabsTFC.POTTERY_TAB);
        setRegistryName(TerraFirmaGreg.getID("ceramics/unfired/mold/" + orePrefix.name.toLowerCase()));
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        return
                new TextComponentTranslation(
                        "item.tfc.ceramics.unfired.mold.name",
                        new TextComponentTranslation("item.material.oreprefix." + orePrefix.name).getFormattedText().replaceFirst(" ", "")
                ).getFormattedText();
    }
}
