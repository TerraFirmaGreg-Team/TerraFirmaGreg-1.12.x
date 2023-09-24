package net.dries007.tfc.module.metal.api.variant.Item;

import gregtech.api.unification.material.Material;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.api.util.IHasModel;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Интерфейс, представляющий предмет породы.
 */
public interface IMetalItem extends IHasModel {

    /**
     * Возвращает вариант блока породы.
     *
     * @return Вариант блока породы.
     */
    @Nonnull
    MetalItemVariant getItemVariant();

    /**
     * Возвращает тип породы.
     *
     * @return Тип породы.
     */
    @Nullable
    Material getMaterial();


    /**
     * Возвращает расположение в реестре для данного подтипа предмета.
     *
     * @return Расположение в реестре
     */
    @Nonnull
    default String getName() {
        return String.format("metal.%s.%s", getItemVariant(), getMaterial());
    }

    /**
     * Возвращает расположение ресурса для данного подтипа предмета.
     *
     * @return Расположение ресурса
     */
    @Nonnull
    default ResourceLocation getResourceLocation() {
        return TerraFirmaCraft.getID(String.format("metal/%s", getItemVariant()));
    }
}
