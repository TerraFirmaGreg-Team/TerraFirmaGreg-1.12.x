package net.dries007.tfc.module.wood.common.items;

import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.module.core.api.item.ItemBase;
import net.dries007.tfc.module.wood.api.type.WoodType;
import net.dries007.tfc.module.wood.api.variant.item.IWoodItem;
import net.dries007.tfc.module.wood.api.variant.item.WoodItemVariant;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

import javax.annotation.Nonnull;

public class ItemWoodLumber extends ItemBase implements IWoodItem {

    private final WoodType type;
    private final WoodItemVariant variant;

    public ItemWoodLumber(WoodItemVariant variant, WoodType type) {
        this.type = type;
        this.variant = variant;

//        OreDictionaryHelper.register(this, variant.toString());
//        OreDictionaryHelper.register(this, variant.toString(), type.toString());
    }


    @Nonnull
    @Override
    public WoodItemVariant getItemVariant() {
        return variant;
    }

    @Nonnull
    @Override
    public WoodType getType() {
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
                new ModelResourceLocation(getResourceLocation(), "inventory"));
    }
}
