package su.terrafirmagreg.modules.wood.api.types.variant.block;

import su.terrafirmagreg.api.model.CustomStateMap;
import su.terrafirmagreg.api.model.ICustomModel;
import su.terrafirmagreg.api.spi.block.IBlockColorProvider;
import su.terrafirmagreg.api.spi.block.ISettingsBlock;
import su.terrafirmagreg.api.spi.block.IStateMapperProvider;
import su.terrafirmagreg.api.spi.types.IType;
import su.terrafirmagreg.api.spi.types.IVariant;
import su.terrafirmagreg.api.util.ModUtils;
import su.terrafirmagreg.api.util.ModelUtils;
import su.terrafirmagreg.modules.wood.api.types.type.WoodType;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


import org.jetbrains.annotations.NotNull;

/**
 * Интерфейс IWoodBlock представляет деревянный блок.
 */
public interface IWoodBlock extends IType<WoodType>, IVariant<WoodBlockVariant>, ISettingsBlock, ICustomModel, IStateMapperProvider, IBlockColorProvider {

    /**
     * Возвращает расположение в реестре для данного деревянного блока.
     *
     * @return расположение в реестре
     */
    @NotNull
    default String getName() {
        return String.format("wood/%s/%s", getVariant(), getType());
    }

    /**
     * Возвращает расположение ресурса для данного деревянного блока.
     *
     * @return расположение ресурса
     */
    @NotNull
    default ResourceLocation getResourceLocation() {
        return ModUtils.id(String.format("wood/%s", getVariant()));
    }

    @Override
    @SideOnly(Side.CLIENT)
    default void onModelRegister() {
        ModelUtils.registerBlockInventoryModel((Block) this, getResourceLocation());
    }

    @Override
    @SideOnly(Side.CLIENT)
    default void onRegisterState() {
        ModelUtils.registerStateMapper((Block) this, new CustomStateMap.Builder().customResource(getResourceLocation()).build());
    }

    @Override
    default IBlockColor getBlockColor() {
        return (s, w, p, i) -> this.getType().getColor();
    }

    @Override
    default IItemColor getItemColor() {
        return (s, i) -> this.getType().getColor();
    }
}
