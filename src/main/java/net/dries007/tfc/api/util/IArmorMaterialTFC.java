package net.dries007.tfc.api.util;

import net.dries007.tfc.api.capability.damage.DamageType;
import net.dries007.tfc.module.core.common.objects.ArmorMaterialTFC;
import net.minecraft.item.ItemArmor;

import javax.annotation.Nonnull;

/**
 * @see ArmorMaterialTFC
 */
public interface IArmorMaterialTFC {
    /**
     * Returns the crushing modifier this armor has
     *
     * @return float value with the modifier. To check how damage calculation is done, see {@link DamageType}
     */
    float getCrushingModifier();

    /**
     * Returns the crushing modifier this armor has
     *
     * @return float value with the modifier. To check how damage calculation is done, see {@link DamageType}
     */
    float getPiercingModifier();

    /**
     * Returns the crushing modifier this armor has
     *
     * @return float value with the modifier. To check how damage calculation is done, see {@link DamageType}
     */
    float getSlashingModifier();

    /**
     * The enum(register one using forge {@link net.minecraftforge.common.util.EnumHelper}) of this armor
     *
     * @return Vanilla "extended" ArmorMaterial
     */
    @Nonnull
    ItemArmor.ArmorMaterial getMaterial();
}
