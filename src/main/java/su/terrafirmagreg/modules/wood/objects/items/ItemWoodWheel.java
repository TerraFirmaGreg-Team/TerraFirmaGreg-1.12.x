package su.terrafirmagreg.modules.wood.objects.items;

import su.terrafirmagreg.api.spi.item.BaseItem;
import su.terrafirmagreg.modules.wood.api.types.type.WoodType;
import su.terrafirmagreg.modules.wood.api.types.variant.item.IWoodItem;
import su.terrafirmagreg.modules.wood.api.types.variant.item.WoodItemVariant;
import su.terrafirmagreg.modules.wood.api.types.variant.item.WoodItemVariants;

import net.minecraft.client.renderer.color.IItemColor;


import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;

import lombok.Getter;

@Getter
public class ItemWoodWheel extends BaseItem implements IWoodItem {

    private final WoodItemVariant variant;
    private final WoodType type;

    public ItemWoodWheel(WoodItemVariant variant, WoodType type) {
        this.variant = variant;
        this.type = type;

        getSettings()
                .weight(Weight.HEAVY)
                .size(Size.NORMAL)
                .addOreDict(variant)
                .addOreDict(variant, type);
    }

    @Override
    public IItemColor getItemColor() {
        return (s, i) -> this.getType().getColor();
    }
}
