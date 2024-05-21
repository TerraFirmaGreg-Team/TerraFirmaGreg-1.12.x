package su.terrafirmagreg.modules.wood.api.types.variant.item;

import su.terrafirmagreg.api.registry.provider.IModelProvider;
import su.terrafirmagreg.api.spi.item.IItemSettings;
import su.terrafirmagreg.api.spi.item.provider.IItemColorProvider;
import su.terrafirmagreg.api.spi.types.IType;
import su.terrafirmagreg.api.spi.types.IVariant;
import su.terrafirmagreg.api.util.ModUtils;
import su.terrafirmagreg.modules.wood.api.types.type.WoodType;

import net.minecraft.util.ResourceLocation;


import org.jetbrains.annotations.NotNull;

/**
 * Интерфейс ICropItem представляет деревянный предмет.
 */
public interface IWoodItem extends IType<WoodType>, IVariant<WoodItemVariant>, IItemSettings, IModelProvider, IItemColorProvider {

    /**
     * Возвращает расположение в реестре для данного подтипа предмета.
     *
     * @return Расположение в реестре
     */
    @NotNull
    default String getName() {
        return String.format("wood/%s/%s", getVariant(), getType());
    }

    /**
     * Возвращает расположение ресурса для данного подтипа предмета.
     *
     * @return Расположение ресурса
     */
    @NotNull
    default ResourceLocation getResourceLocation() {
        return ModUtils.id(String.format("wood/%s", getVariant()));
    }
}
