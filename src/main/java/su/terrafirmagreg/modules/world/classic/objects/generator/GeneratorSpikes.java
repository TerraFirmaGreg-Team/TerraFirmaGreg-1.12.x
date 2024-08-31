package su.terrafirmagreg.modules.world.classic.objects.generator;

import su.terrafirmagreg.api.util.BlockUtils;
import su.terrafirmagreg.modules.rock.init.BlocksRock;
import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockRaw;
import su.terrafirmagreg.modules.world.classic.WorldTypeClassic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;


import net.dries007.tfc.objects.blocks.stone.BlockRockSpike;

import java.util.Random;

public class GeneratorSpikes implements IWorldGenerator {

    private final boolean ceiling; //Is this a stalactite generator?
    private final int rarity;

    public GeneratorSpikes(boolean ceiling, int rarity) {
        this.ceiling = ceiling;
        this.rarity = rarity;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        for (int k5 = 0; k5 < rarity; ++k5) {
            int x = random.nextInt(16) + 8;
            int z = random.nextInt(16) + 8;
            int y = random.nextInt(WorldTypeClassic.SEALEVEL - 50) + 30;
            BlockPos basePos = new BlockPos(chunkX << 4, y, chunkZ << 4).add(x, 0, z);
            BlockPos topPos = ceiling ? basePos.down() : basePos.up();
            BlockPos stoneAttach = ceiling ? basePos.up() : basePos.down();
            BlockPos freeSpace = ceiling ? topPos.down() : topPos.up();
            if (!BlockUtils.isRawStone(world.getBlockState(stoneAttach)) || !world.isAirBlock(basePos) || !world.isAirBlock(topPos) ||
                    !world.isAirBlock(freeSpace)) {
                continue;
            }
            boolean canPlace = true;
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                if (!world.isAirBlock(basePos.offset(facing)) || !world.isAirBlock(topPos.offset(facing))) {
                    canPlace = false;
                    break;
                }
            }
            if (canPlace) {
                var rockBlock = (BlockRockRaw) world.getBlockState(stoneAttach).getBlock();
                IBlockState baseState = BlocksRock.SPELEOTHEM.get(rockBlock.getType())
                        .getDefaultState()
                        .withProperty(BlockRockSpike.BASE, true)
                        .withProperty(BlockRockSpike.CEILING, ceiling);
                IBlockState topState = BlocksRock.SPELEOTHEM.get(rockBlock.getType())
                        .getDefaultState()
                        .withProperty(BlockRockSpike.BASE, false)
                        .withProperty(BlockRockSpike.CEILING, ceiling);
                world.setBlockState(basePos, baseState, 2);
                world.setBlockState(topPos, topState, 2);
            }
        }
    }
}
