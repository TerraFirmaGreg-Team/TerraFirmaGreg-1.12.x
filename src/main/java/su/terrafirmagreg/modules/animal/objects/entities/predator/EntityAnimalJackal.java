package su.terrafirmagreg.modules.animal.objects.entities.predator;

import su.terrafirmagreg.modules.animal.ModuleAnimalConfig;
import su.terrafirmagreg.modules.animal.data.LootTablesAnimal;
import net.dries007.tfc.util.climate.BiomeHelper;
import net.dries007.tfc.world.classic.biomes.BiomesTFC;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import su.terrafirmagreg.api.lib.Constants;
import su.terrafirmagreg.modules.animal.api.type.IPredator;
import su.terrafirmagreg.modules.animal.data.SoundAnimal;
import su.terrafirmagreg.modules.animal.objects.entities.ai.EntityAnimalAIAttackMelee;
import su.terrafirmagreg.modules.animal.objects.entities.ai.EntityAnimalAIWanderHuntArea;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class EntityAnimalJackal extends EntityAnimalCoyote implements IPredator {
	private static final int DAYS_TO_ADULTHOOD = 112;

	@SuppressWarnings("unused")
	public EntityAnimalJackal(World worldIn) {
		this(worldIn, Gender.valueOf(Constants.RANDOM.nextBoolean()),
				getRandomGrowth(DAYS_TO_ADULTHOOD, 0));
	}

	public EntityAnimalJackal(World worldIn, Gender gender, int birthDay) {
		super(worldIn, gender, birthDay);
		this.setSize(0.8F, 0.9F);
	}

	@Override
	public int getSpawnWeight(Biome biome, float temperature, float rainfall, float floraDensity, float floraDiversity) {
		BiomeHelper.BiomeType biomeType = BiomeHelper.getBiomeType(temperature, rainfall, floraDensity);
		if (!BiomesTFC.isOceanicBiome(biome) && !BiomesTFC.isBeachBiome(biome) &&
				(biomeType == BiomeHelper.BiomeType.DESERT)) {
			return ModuleAnimalConfig.ENTITIES.JACKAL.rarity;
		}
		return 0;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundAnimal.ANIMAL_JACKAL_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundAnimal.ANIMAL_JACKAL_DEATH;
	}

	@Override
	protected void initEntityAI() {
		EntityAIWander wander = new EntityAnimalAIWanderHuntArea(this, 1.0D);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(3, new EntityAnimalAIAttackMelee<>(this, 1.2D, 1.25D, EntityAnimalAIAttackMelee.AttackBehavior.NIGHTTIME_ONLY).setWanderAI(wander));
		this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
		this.tasks.addTask(5, wander); // Move within hunt area
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
		// Avoid players at daytime
		this.tasks.addTask(4, new EntityAIAvoidEntity<>(this, EntityPlayer.class, 16.0F, 1.0D, 1.25D));

		int priority = 2;
		for (String input : ModuleAnimalConfig.ENTITIES.JACKAL.huntCreatures) {
			ResourceLocation key = new ResourceLocation(input);
			EntityEntry entityEntry = ForgeRegistries.ENTITIES.getValue(key);
			if (entityEntry != null) {
				Class<? extends Entity> entityClass = entityEntry.getEntityClass();
				if (EntityLivingBase.class.isAssignableFrom(entityClass)) {
					//noinspection unchecked
					this.targetTasks.addTask(priority++, new EntityAINearestAttackableTarget<>(this, (Class<EntityLivingBase>) entityClass, false));
				}
			}
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return Constants.RANDOM.nextInt(100) < 5 ? SoundAnimal.ANIMAL_JACKAL_CRY : SoundAnimal.ANIMAL_JACKAL_SAY;
	}

	@Nullable
	@Override
	protected ResourceLocation getLootTable() {
		return LootTablesAnimal.ANIMALS_JACKAL;
	}

	@Override
	protected void playStepSound(BlockPos pos, Block blockIn) {
		this.playSound(SoundEvents.ENTITY_WOLF_STEP, 0.14F, 0.9F); // Close enough
	}
}
