package net.dries007.tfc.world.classic.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;


import net.dries007.tfc.api.capability.chunkdata.ChunkData;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.world.classic.ChunkGenTFC;
import net.dries007.tfc.world.classic.WorldTypeTFC;
import net.dries007.tfc.world.classic.biomes.BiomesTFC;
import tfcflorae.objects.blocks.BlocksTFCF;
import tfcflorae.objects.blocks.groundcover.BlockCoral;
import tfcflorae.objects.blocks.groundcover.BlockCoralBlock;

import java.util.Random;

public class WorldGenGlowPlant implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (!(chunkGenerator instanceof ChunkGenTFC)) return;

        generateGlowPlant(random, chunkX, chunkZ, world, chunkGenerator, chunkProvider);
    }

    private void generateGlowPlant(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        BlockPos chunkBlockPos = new BlockPos(chunkX << 4, 0, chunkZ << 4);
        ChunkData data = ChunkData.get(world, chunkBlockPos);

        Biome b = world.getBiome(chunkBlockPos);
        float avgTemperature = Climate.getAvgTemp(world, chunkBlockPos);
        float rainfall = ChunkData.getRainfall(world, chunkBlockPos);
        float floraDensity = data.getFloraDensity();
        float floraDiversity = data.getFloraDiversity();

        int glowPlantsInChunk = 5 + random.nextInt(20);
        for (int i = 0; i < (glowPlantsInChunk); i++) {
            final int x = (chunkX << 4) + random.nextInt(16) + 8;
            final int z = (chunkZ << 4) + random.nextInt(16) + 8;
            final BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z));

            if ((b == BiomesTFC.OCEAN || b == BiomesTFC.DEEP_OCEAN || b == BiomesTFC.BEACH || b == BiomesTFC.GRAVEL_BEACH) &&
                    world.provider.getDimension() == 0) {
                if (isValidPosition(world, pos) && pos.getY() < WorldTypeTFC.SEALEVEL - 1 && pos.getY() > 115 && floraDensity >= 0.4f &&
                        floraDiversity >= 0.3f && floraDensity <= 0.6f && floraDiversity <= 0.5f && avgTemperature >= 10f && avgTemperature <= 28f &&
                        rainfall >= 150f) {
                    world.setBlockState(pos, BlocksTFCF.GLOWING_SEA_BANANA.getDefaultState());
                }
            }
        }
    }

    protected boolean isValidPosition(World world, BlockPos pos) {
        IBlockState current = world.getBlockState(pos);
        IBlockState up = world.getBlockState(pos.up());
        IBlockState down = world.getBlockState(pos.down());
        IBlockState north = world.getBlockState(pos.north());
        IBlockState south = world.getBlockState(pos.south());
        IBlockState east = world.getBlockState(pos.east());
        IBlockState west = world.getBlockState(pos.west());
        return ((up.getBlock() instanceof BlockCoralBlock || down.getBlock() instanceof BlockCoralBlock ||
                north.getBlock() instanceof BlockCoralBlock || south.getBlock() instanceof BlockCoralBlock ||
                east.getBlock() instanceof BlockCoralBlock || west.getBlock() instanceof BlockCoralBlock ||
                current.getBlock() instanceof BlockCoral) && !(world.isAirBlock(pos.up())));
    }
}
