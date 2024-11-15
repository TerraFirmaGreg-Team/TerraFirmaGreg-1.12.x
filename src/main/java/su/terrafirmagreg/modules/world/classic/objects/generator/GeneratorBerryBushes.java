package su.terrafirmagreg.modules.world.classic.objects.generator;

import su.terrafirmagreg.modules.core.capabilities.chunkdata.ProviderChunkData;
import su.terrafirmagreg.modules.world.classic.ChunkGenClassic;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import net.dries007.tfc.ConfigTFC;
import net.dries007.tfc.api.types.IBerryBush;
import net.dries007.tfc.objects.blocks.agriculture.BlockBerryBush;
import su.terrafirmagreg.modules.core.feature.climate.Climate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneratorBerryBushes implements IWorldGenerator {

  private static final List<IBerryBush> BUSHES = new ArrayList<>();

  public static void register(IBerryBush bush) {
    BUSHES.add(bush);
  }

  @Override
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
    if (chunkGenerator instanceof ChunkGenClassic && world.provider.getDimension() == 0 && !BUSHES.isEmpty() &&
        ConfigTFC.General.FOOD.berryBushRarity > 0) {
      if (random.nextInt(ConfigTFC.General.FOOD.berryBushRarity) == 0) {
        // Guarantees bush generation if possible (easier to balance by config file while also making it random)
        Collections.shuffle(BUSHES);
        BlockPos chunkBlockPos = new BlockPos(chunkX << 4, 0, chunkZ << 4);

        float temperature = Climate.getAvgTemp(world, chunkBlockPos);
        float rainfall = ProviderChunkData.getRainfall(world, chunkBlockPos);
        IBerryBush bush = BUSHES.stream()
                                .filter(x -> x.isValidConditions(temperature, rainfall))
                                .findFirst()
                                .orElse(null);

        if (bush != null) {
          final int x = (chunkX << 4) + random.nextInt(16) + 8;
          final int z = (chunkZ << 4) + random.nextInt(16) + 8;
          final BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z));

          if (world.getBlockState(pos).getMaterial().isLiquid() || !world.getBlockState(pos)
                                                                         .getMaterial()
                                                                         .isReplaceable()) {
            return;
          }
          BlockBerryBush block = BlockBerryBush.get(bush);
          if (block.canPlaceBlockAt(world, pos)) {
            world.setBlockState(pos, block.getDefaultState());
          }
        }
      }
    }
  }
}
