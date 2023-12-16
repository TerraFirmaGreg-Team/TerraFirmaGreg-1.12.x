package com.speeedcraft.tfgmod.mixins.gregtech;

import com.speeedcraft.tfgmod.gregtech.oreprefix.TFGOrePrefix;
import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.material.Material;
import gregtech.common.blocks.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(value = BlockOre.class, remap = false)
public class BlockOreMixin {

	@Shadow
	@Final
	public Material material;

	@Inject(method = "getItemDropped", at = @At(value = "HEAD"), remap = false, cancellable = true)
	private void getItemDropped(IBlockState state, Random rand, int fortune, CallbackInfoReturnable<Item> cir) {
		var itemStack = OreDictUnifier.get(TFGOrePrefix.oreChunk, material);

		cir.setReturnValue(itemStack.getItem());
	}

	@Inject(method = "damageDropped", at = @At(value = "HEAD"), remap = false, cancellable = true)
	private void damageDropped(IBlockState state, CallbackInfoReturnable<Integer> cir) {
		var itemStack = OreDictUnifier.get(TFGOrePrefix.oreChunk, material);

		cir.setReturnValue(itemStack.getItemDamage());
	}
}
