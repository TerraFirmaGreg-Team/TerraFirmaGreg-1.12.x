package net.dries007.tfc.api.types.food;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.api.types.food.type.FoodType;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Интерфейс ICropItem представляет предмет урожая.
 */
public interface IFoodItem {

    /**
     * Возвращает тип предмета.
     *
     * @return тип
     */
    @Nonnull
    FoodType getType();

    /**
     * Возвращает расположение в реестре для данного подтипа предмета.
     *
     * @param subType подтип предмета
     * @return расположение в реестре
     */
    @Nonnull
    default ResourceLocation getRegistryLocation() {
        return TerraFirmaCraft.identifier(String.format("food/%s", getType()));
    }

    /**
     * Возвращает расположение ресурса для данного подтипа предмета.
     *
     * @param subType подтип предмета
     * @return расположение ресурса
     */
    @Nonnull
    default ResourceLocation getResourceLocation() {
        return TerraFirmaCraft.identifier(String.format("food/%s", getType()));
    }

    /**
     * Возвращает локализованное имя для данного подтипа предмета.
     *
     * @param subType подтип предмета
     * @return локализованное имя
     */
    @Nonnull
    default String getTranslationName() {
        return getRegistryLocation().toString().toLowerCase().replace(":", ".").replace("/", ".");
    }
}
