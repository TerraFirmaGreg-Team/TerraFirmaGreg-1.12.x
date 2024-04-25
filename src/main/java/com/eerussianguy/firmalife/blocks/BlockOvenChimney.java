package com.eerussianguy.firmalife.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;


import net.dries007.tfc.api.capability.size.IItemSize;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;

import org.jetbrains.annotations.NotNull;

import static com.eerussianguy.firmalife.init.StatePropertiesFL.CURED;
import static su.terrafirmagreg.api.lib.MathConstants.RNG;

public class BlockOvenChimney extends Block implements IItemSize {

    public static final AxisAlignedBB CHIMNEY_BB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.25D).union(
            new AxisAlignedBB(0.0D, 0.0D, 0.75D, 1.0D, 1.0D, 1.0D).union(new AxisAlignedBB(0.0D, 0.0D, 0.25D, 0.25D, 1.0D, 0.75D))
                    .union(new AxisAlignedBB(0.75D, 0.0D, 0.25D, 1.0D, 1.0D, 0.75D)));

    public BlockOvenChimney() {
        super(Material.ROCK, MapColor.RED_STAINED_HARDENED_CLAY);
        setHardness(2.0f);
        setResistance(3.0f);
        setLightOpacity(0);
        this.setDefaultState(this.blockState.getBaseState().withProperty(CURED, false));
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        if (state.getValue(CURED)) {
            drops.add(new ItemStack(Items.BRICK, 3 + RNG.nextInt(3)));
        } else {
            super.getDrops(drops, world, pos, state, fortune);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    @NotNull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(CURED, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(CURED) ? 1 : 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public @NotNull Size getSize(@NotNull ItemStack stack) {
        return Size.NORMAL;
    }

    @Override
    public @NotNull Weight getWeight(@NotNull ItemStack stack) {
        return Weight.HEAVY;
    }

    @Override
    @NotNull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CURED);
    }

    @Override
    @SuppressWarnings("deprecation")
    @NotNull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CHIMNEY_BB;
    }

    @Override
    @SuppressWarnings("deprecation")
    @NotNull
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
}
