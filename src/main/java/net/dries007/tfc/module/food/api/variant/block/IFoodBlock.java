package net.dries007.tfc.module.food.api.variant.block;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.api.util.IHasModel;
import net.dries007.tfc.api.util.IItemProvider;
import net.dries007.tfc.module.food.api.type.FoodType;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;


/**
 * Интерфейс IWoodBlock представляет деревянный блок.
 */
public interface IFoodBlock extends IHasModel, IItemProvider {

    /**
     * Возвращает вариант деревянного блока.
     *
     * @return вариант деревянного блока
     */
    FoodBlockVariant getBlockVariant();

    /**
     * Возвращает тип дерева блока.
     *
     * @return тип дерева
     */
    FoodType getType();

    /**
     * Возвращает местоположение регистрации блока почвы.
     *
     * @return Местоположение регистрации блока почвы.
     */
    @Nonnull
    default String getName() {
        return String.format("food.%s.%s", getBlockVariant(), getType());
    }

    /**
     * Возвращает местоположение ресурса блока почвы.
     *
     * @return Местоположение ресурса блока почвы.
     */
    @Nonnull
    default ResourceLocation getResourceLocation() {
        return TerraFirmaCraft.getID(String.format("food/%s", getType()));
    }

}
