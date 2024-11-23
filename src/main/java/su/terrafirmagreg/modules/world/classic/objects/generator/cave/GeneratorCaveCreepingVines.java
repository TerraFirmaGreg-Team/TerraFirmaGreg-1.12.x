package su.terrafirmagreg.modules.world.classic.objects.generator.cave;

import su.terrafirmagreg.modules.core.capabilities.chunkdata.ProviderChunkData;
import su.terrafirmagreg.modules.core.feature.climate.Climate;
import su.terrafirmagreg.modules.flora.api.types.type.FloraType;
import su.terrafirmagreg.modules.flora.init.BlocksFlora;
import su.terrafirmagreg.modules.flora.object.block.BlockPlantHangingCreeping;
import su.terrafirmagreg.modules.world.classic.WorldTypeClassic;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

import static su.terrafirmagreg.api.data.Properties.IntProp.AGE_4;

public class GeneratorCaveCreepingVines extends WorldGenerator {

  private FloraType plant;

  public void setGeneratedPlant(FloraType plantIn) {
    this.plant = plantIn;
  }

  @Override
  public boolean generate(World worldIn, Random rng, BlockPos pos) {
    var plantBlock = (BlockPlantHangingCreeping) BlocksFlora.PLANT.get(plant);
    IBlockState state = plantBlock.getDefaultState();

    for (int i = 0; i < ProviderChunkData.getRainfall(worldIn, pos) / 4; ++i) {
      BlockPos blockpos = pos.add(rng.nextInt(7) - rng.nextInt(7), rng.nextInt(16), rng.nextInt(7) - rng.nextInt(7));

      int j = 1 + rng.nextInt(plant.getMaxHeight());

      for (int k = 0; k < j; ++k) {
        if (plant.isValidTemp(Climate.getActualTemp(worldIn, blockpos)) &&
            plant.isValidSunlight(worldIn.getLightFor(EnumSkyBlock.SKY, blockpos.down(k))) &&
            worldIn.isAirBlock(blockpos.down(k)) &&
            pos.getY() < WorldTypeClassic.SEALEVEL - 3 &&
            worldIn.getLightFor(EnumSkyBlock.SKY, blockpos) < 14 &&
            plantBlock.canBlockStay(worldIn, blockpos.down(k), state) &&
            plantBlock.canPlaceBlockAt(worldIn, blockpos.down(k))) {
          int plantAge = plant.getAgeForWorldgen(rng, Climate.getActualTemp(worldIn, blockpos));
          setBlockAndNotifyAdequately(worldIn, blockpos.down(k), state.withProperty(AGE_4, plantAge));
        }
      }
    }
    return true;
  }
}
