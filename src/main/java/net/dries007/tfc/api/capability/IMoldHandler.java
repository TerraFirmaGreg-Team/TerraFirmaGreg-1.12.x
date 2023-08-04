package net.dries007.tfc.api.capability;

import gregtech.api.unification.material.Material;
import net.dries007.tfc.api.capability.heat.IItemHeat;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

/**
 * This is an interface for objects that implement both {@link IFluidHandler} and {@link IItemHeat}. Anything that wants to behave similar to an {@link net.dries007.tfc.objects.items.ceramics.ItemMold} should implement this interface on the capability object, and return this instance when queried.
 * Currently provided by:
 * {@link net.dries007.tfc.objects.items.ceramics.ItemMold}
 * {@link net.dries007.tfc.objects.items.ceramics.ItemSmallVessel}
 */
public interface IMoldHandler extends IFluidHandler, INBTSerializable<NBTTagCompound>, IItemHeat {
	/**
	 * Gets the metal currently in the mold. Null if empty. Used in model loading.
	 *
	 * @return The metal
	 */
	@Nullable
	Material getMaterial();

	/**
	 * Gets the current amount of metal in the mold. Zero if empty.
	 *
	 * @return The amount of metal, in mB / units
	 */
	int getAmount();

}
