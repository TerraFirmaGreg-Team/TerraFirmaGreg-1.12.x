package tfcflorae.compat.tfcelementia.jei.wrappers;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import net.dries007.tfc.api.capability.IMoldHandler;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import tfcelementia.objects.items.metal.ItemMetalTFCE;
import tfcflorae.compat.tfcelementia.ceramics.ItemKaoliniteMoldTFCE;

public class CastingRecipeKaoliniteTFCEWrapper extends net.dries007.tfc.compat.jei.wrappers.CastingRecipeWrapper {
	private ItemStack mold;
	private FluidStack input;

	public CastingRecipeKaoliniteTFCEWrapper(Metal metal, ItemMetalTFCE.ItemType type) {
		super(metal, Metal.ItemType.INGOT); // Just so I can override
		input = new FluidStack(FluidsTFC.getFluidFromMetal(metal), type.getSmeltAmount());
		mold = new ItemStack(ItemKaoliniteMoldTFCE.get(type));
		IFluidHandler cap = mold.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
		if (cap instanceof IMoldHandler) {
			cap.fill(input, true);
		}
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInput(VanillaTypes.FLUID, input);
		ingredients.setOutput(VanillaTypes.ITEM, mold);
	}
}
