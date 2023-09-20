package net.dries007.tfc.module.soil.common.items;

import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.module.core.common.objects.CreativeTabsTFC;
import net.dries007.tfc.module.core.common.objects.items.TFCItem;
import net.dries007.tfc.module.soil.api.type.SoilType;
import net.dries007.tfc.module.soil.api.variant.item.ISoilItem;
import net.dries007.tfc.module.soil.api.variant.item.SoilItemVariant;
import net.dries007.tfc.util.OreDictionaryHelper;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;

public class ItemSoilPile extends TFCItem implements ISoilItem {

    private final SoilItemVariant variant;
    private final SoilType type;

    public ItemSoilPile(SoilItemVariant variant, SoilType type) {

        this.variant = variant;
        this.type = type;

        setCreativeTab(CreativeTabsTFC.SOIL);
        setRegistryName(getRegistryLocation());
        setTranslationKey(getTranslationName());

        OreDictionaryHelper.register(this, variant.toString());
        OreDictionaryHelper.register(this, variant.toString(), type.toString());
    }

    @Nonnull
    @Override
    public SoilItemVariant getItemVariant() {
        return variant;
    }

    @Override
    @Nonnull
    public SoilType getType() {
        return type;
    }

    @Nonnull
    @Override
    public Size getSize(@Nonnull ItemStack stack) {
        return Size.SMALL;
    }

    @Nonnull
    @Override
    public Weight getWeight(@Nonnull ItemStack stack) {
        return Weight.VERY_LIGHT;
    }


    @Override
    public void onModelRegister() {
        ModelLoader.setCustomModelResourceLocation(this, 0,
                new ModelResourceLocation(getRegistryLocation(), "normal"));
    }
}
