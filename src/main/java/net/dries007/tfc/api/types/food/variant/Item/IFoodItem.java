package net.dries007.tfc.api.types.food.variant.Item;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.api.types.food.type.FoodType;
import net.dries007.tfc.api.util.IHasModel;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Интерфейс ICropItem представляет предмет урожая.
 */
public interface IFoodItem extends IHasModel {

    /**
     * Возвращает тип предмета.
     *
     * @return тип
     */
    @Nonnull
    FoodType getType();

    @Nonnull
    FoodItemVariant getVariants();

    /**
     * Возвращает расположение в реестре для данного подтипа предмета.
     *
     * @return расположение в реестре
     */
    @Nonnull
    default ResourceLocation getRegistryLocation() {
        return TerraFirmaCraft.getID(String.format("food/%s", getType()));
    }

    /**
     * Возвращает расположение ресурса для данного подтипа предмета.
     *
     * @return расположение ресурса
     */
    @Nonnull
    default ResourceLocation getResourceLocation() {
        return TerraFirmaCraft.getID(String.format("food/%s", getType()));
    }

    /**
     * Возвращает локализованное имя для данного подтипа предмета.
     *
     * @return локализованное имя
     */
    @Nonnull
    default String getTranslationName() {
        return getRegistryLocation().toString().toLowerCase().replace(":", ".").replace("/", ".");
    }
}
