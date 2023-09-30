package net.dries007.tfc.module.core.objects.blocks.fluid;

import com.ferreusveritas.dynamictrees.systems.DirtHelper;
import net.dries007.tfc.module.core.init.BlocksCore;
import net.dries007.tfc.util.climate.ClimateTFC;
import net.dries007.tfc.util.climate.ITemperatureBlock;
import net.dries007.tfc.util.climate.IceMeltHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidWater extends BlockFluidBase implements ITemperatureBlock {
    private final boolean isSalt;
    private final float freezeThreshold;

    public BlockFluidWater(Fluid fluid, Material material, boolean isSalt) {
        super(fluid, material, true);

        this.isSalt = isSalt;
        this.freezeThreshold = isSalt ? IceMeltHandler.SALT_WATER_FREEZE_THRESHOLD : IceMeltHandler.WATER_FREEZE_THRESHOLD;

        setLightOpacity(3);
        disableStats();

        DirtHelper.registerSoil(fluid.getBlock(), DirtHelper.WATERLIKE);
    }

    @Override
    public void onTemperatureUpdateTick(World world, BlockPos pos, IBlockState state) {
        if (world.getLightFor(EnumSkyBlock.BLOCK, pos) < 10 && ClimateTFC.getActualTemp(world, pos) < freezeThreshold && state.getValue(LEVEL) == 0) {
            for (EnumFacing face : EnumFacing.HORIZONTALS) {
                if (world.getBlockState(pos.offset(face)).getBlock() != this) {
                    world.setBlockState(pos, isSalt ? BlocksCore.SEA_ICE.getDefaultState() : Blocks.ICE.getDefaultState());
                    break;
                }
            }
        }
    }
}
