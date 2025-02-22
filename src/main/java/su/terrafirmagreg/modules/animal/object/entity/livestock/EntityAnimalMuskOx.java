package su.terrafirmagreg.modules.animal.object.entity.livestock;

import su.terrafirmagreg.api.helper.BiomeHelper;
import su.terrafirmagreg.api.util.BiomeUtils;
import su.terrafirmagreg.modules.animal.ConfigAnimal;
import su.terrafirmagreg.modules.animal.api.type.ILivestock;
import su.terrafirmagreg.modules.animal.init.LootTablesAnimal;
import su.terrafirmagreg.modules.animal.init.SoundsAnimal;
import su.terrafirmagreg.modules.core.feature.calendar.Calendar;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import org.jetbrains.annotations.Nullable;

import static su.terrafirmagreg.api.util.MathUtils.RNG;

public class EntityAnimalMuskOx extends EntityAnimalSheep implements ILivestock {

  @SuppressWarnings("unused")
  public EntityAnimalMuskOx(World worldIn) {
    this(worldIn, Gender.valueOf(RNG.nextBoolean()),
      getRandomGrowth(ConfigAnimal.ENTITY.MUSKOX.adulthood, ConfigAnimal.ENTITY.MUSKOX.elder),
      EntitySheep.getRandomSheepColor(RNG));
  }

  public EntityAnimalMuskOx(World worldIn, Gender gender, int birthDay, EnumDyeColor dye) {
    super(worldIn, gender, birthDay, dye);
    this.setSize(1.4F, 1.6F);
  }

  @Override
  public int getSpawnWeight(Biome biome, float temperature, float rainfall, float floraDensity,
                            float floraDiversity) {
    BiomeUtils.BiomeType biomeType = BiomeUtils.getBiomeType(temperature, rainfall, floraDensity);
    if (!BiomeHelper.isOceanic(biome) && !BiomeHelper.isBeach(biome) &&
        (biomeType == BiomeUtils.BiomeType.TUNDRA)) {
      return ConfigAnimal.ENTITY.MUSKOX.rarity;
    }
    return 0;
  }

  @Override
  public int getMinGroupSize() {
    return 3;
  }

  @Override
  public int getMaxGroupSize() {
    return 5;
  }

  @Override
  public long gestationDays() {
    return ConfigAnimal.ENTITY.MUSKOX.gestation;
  }

  @Override
  public void birthChildren() {
    int numberOfChildren = ConfigAnimal.ENTITY.MUSKOX.babies;
    for (int i = 0; i < numberOfChildren; i++) {
      EntityAnimalMuskOx baby = new EntityAnimalMuskOx(world,
        Gender.valueOf(RNG.nextBoolean()),
        (int) Calendar.PLAYER_TIME.getTotalDays(), getDyeColor());
      baby.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
      baby.setFamiliarity(
        getFamiliarity() < 0.9F ? getFamiliarity() / 2.0F : getFamiliarity() * 0.9F);
      world.spawnEntity(baby);
    }
  }

  @Override
  public double getOldDeathChance() {
    return ConfigAnimal.ENTITY.MUSKOX.oldDeathChance;
  }

  @Override
  public float getAdultFamiliarityCap() {
    return 0.35F;
  }

  @Override
  public int getDaysToAdulthood() {
    return ConfigAnimal.ENTITY.MUSKOX.adulthood;
  }

  @Override
  public int getDaysToElderly() {
    return ConfigAnimal.ENTITY.MUSKOX.elder;
  }

  @Override
  public long getProductsCooldown() {
    return Math.max(0, ConfigAnimal.ENTITY.MUSKOX.woolTicks + getShearedTick()
                       - Calendar.PLAYER_TIME.getTicks());
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
    return SoundsAnimal.ANIMAL_MUSKOX_HURT.get();
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundsAnimal.ANIMAL_MUSKOX_DEATH.get();
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundsAnimal.ANIMAL_MUSKOX_SAY.get();
  }

  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTablesAnimal.ANIMALS_MUSKOX;
  }

  @Override
  // Equivalent sound
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound(SoundEvents.ENTITY_COW_STEP, 0.16F, 1.1F);
  }
}
