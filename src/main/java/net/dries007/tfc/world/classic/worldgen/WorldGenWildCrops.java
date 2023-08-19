package net.dries007.tfc.world.classic.worldgen;

import net.dries007.tfc.api.registries.TFCStorage;
import net.dries007.tfc.api.types.crop.type.CropType;
import net.dries007.tfc.common.objects.blocks.BlocksTFC;
import net.dries007.tfc.common.objects.blocks.crop.BlockCrop;
import net.dries007.tfc.config.ConfigTFC;
import net.dries007.tfc.util.calendar.CalendarTFC;
import net.dries007.tfc.util.climate.ClimateTFC;
import net.dries007.tfc.world.classic.ChunkGenTFC;
import net.dries007.tfc.world.classic.chunkdata.ChunkDataTFC;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static net.dries007.tfc.api.types.crop.variant.CropBlockVariants.GROWING;

/**
 * Генератор мира для дикорастущих культур.
 */
@ParametersAreNonnullByDefault
public class WorldGenWildCrops implements IWorldGenerator {

    private List<CropType> types = new ArrayList<>(CropType.getCropTypes());

    /**
     * Генерирует дикорастущие культуры в мире.
     *
     * @param random         генератор случайных чисел
     * @param chunkX         координата X чанка
     * @param chunkZ         координата Z чанка
     * @param world          экземпляр мира
     * @param chunkGenerator генератор чанков
     * @param chunkProvider  поставщик чанков
     */
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (chunkGenerator instanceof ChunkGenTFC && world.provider.getDimension() == 0 && !types.isEmpty() && ConfigTFC.General.FOOD.cropRarity > 0) {
            if (random.nextInt(ConfigTFC.General.FOOD.cropRarity) == 0) {
                // Гарантирует генерацию культур, если это возможно (легче настроить через файл конфигурации, сохраняя при этом случайность)
                BlockPos chunkBlockPos = new BlockPos(chunkX << 4, 0, chunkZ << 4);

                Collections.shuffle(types);
                float temperature = ClimateTFC.getAvgTemp(world, chunkBlockPos);
                float rainfall = ChunkDataTFC.getRainfall(world, chunkBlockPos);

                var type = types.stream().filter(x -> x.isValidConditions(temperature, rainfall)).findFirst().orElse(null);
                if (type != null) {
                    BlockCrop cropBlock = (BlockCrop) TFCStorage.getCropBlock(GROWING, type);
                    int cropsInChunk = 3 + random.nextInt(5);
                    for (int i = 0; i < cropsInChunk; i++) {
                        final int x = (chunkX << 4) + random.nextInt(16) + 8;
                        final int z = (chunkZ << 4) + random.nextInt(16) + 8;
                        final BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z));
                        if (isValidPosition(world, pos)) {
                            double yearProgress = CalendarTFC.CALENDAR_TIME.getMonthOfYear().ordinal() / 11.0;
                            int maxStage = type.getMaxStage();
                            int growth = (int) (yearProgress * maxStage) + 3 - random.nextInt(2);
                            if (growth > maxStage)
                                growth = maxStage;
                            world.setBlockState(pos, cropBlock.getDefaultState().withProperty(cropBlock.getAgeProperty(), growth).withProperty(BlockCrop.WILD, true), 2);

                        }
                    }
                }
            }
        }
    }

    /**
     * Проверяет, является ли позиция допустимой для генерации культуры.
     *
     * @param world мир
     * @param pos   позиция
     * @return {@code true}, если позиция допустима, иначе {@code false}
     */
    protected boolean isValidPosition(World world, BlockPos pos) {
        return world.isAirBlock(pos) && BlocksTFC.isSoil(world.getBlockState(pos.down()));
    }
}
