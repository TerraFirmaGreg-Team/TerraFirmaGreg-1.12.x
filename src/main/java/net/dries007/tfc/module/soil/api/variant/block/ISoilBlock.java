package net.dries007.tfc.module.soil.api.variant.block;

import net.dries007.tfc.TerraFirmaGreg;
import net.dries007.tfc.api.util.IHasModel;
import net.dries007.tfc.api.util.IItemProvider;
import net.dries007.tfc.module.soil.api.type.SoilType;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Интерфейс, представляющий блок почвы.
 */
public interface ISoilBlock extends IItemProvider, IHasModel {
    /**
     * Возвращает вариант блока почвы.
     *
     * @return Вариант блока почвы.
     */
    @Nonnull
    SoilBlockVariant getBlockVariant();

    /**
     * Возвращает тип почвы.
     *
     * @return Тип почвы.
     */
    @Nonnull
    SoilType getType();

    /**
     * Возвращает местоположение регистрации блока почвы.
     *
     * @return Местоположение регистрации блока почвы.
     */
    @Nonnull
    default String getName() {
        return String.format("soil.%s.%s", getBlockVariant(), getType());
    }

    /**
     * Возвращает местоположение ресурса блока почвы.
     *
     * @return Местоположение ресурса блока почвы.
     */
    @Nonnull
    default ResourceLocation getResourceLocation() {
        return TerraFirmaGreg.getID(String.format("soil/%s", getBlockVariant()));
    }
}
