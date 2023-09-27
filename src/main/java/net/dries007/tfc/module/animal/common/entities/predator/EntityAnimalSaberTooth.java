package net.dries007.tfc.module.animal.common.entities.predator;

import net.dries007.tfc.module.core.sound.TFCSounds;
import net.dries007.tfc.common.objects.LootTablesTFC;
import net.dries007.tfc.config.ConfigTFC;
import net.dries007.tfc.module.animal.api.type.IPredator;
import net.dries007.tfc.module.animal.common.entities.AnimalGroupingRules;
import net.dries007.tfc.module.animal.common.entities.EntityAnimalMammal;
import net.dries007.tfc.module.animal.common.entities.ai.EntityAnimalAIAttackMelee;
import net.dries007.tfc.module.animal.common.entities.ai.EntityAnimalAIWanderHuntArea;
import net.dries007.tfc.util.Constants;
import net.dries007.tfc.util.climate.BiomeHelper;
import net.dries007.tfc.world.classic.biomes.BiomesTFC;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
public class EntityAnimalSaberTooth extends EntityAnimalMammal implements IPredator {
    private static final int DAYS_TO_ADULTHOOD = 192;

    @SuppressWarnings("unused")
    public EntityAnimalSaberTooth(World worldIn) {
        this(worldIn, Gender.valueOf(Constants.RNG.nextBoolean()),
                getRandomGrowth(DAYS_TO_ADULTHOOD, 0));
    }

    public EntityAnimalSaberTooth(World worldIn, Gender gender, int birthDay) {
        super(worldIn, gender, birthDay);
        this.setSize(1.3F, 1.5F);
    }

    @Override
    public int getSpawnWeight(Biome biome, float temperature, float rainfall, float floraDensity, float floraDiversity) {
        BiomeHelper.BiomeType biomeType = BiomeHelper.getBiomeType(temperature, rainfall, floraDensity);
        if (!BiomesTFC.isOceanicBiome(biome) && !BiomesTFC.isBeachBiome(biome) &&
                (biomeType == BiomeHelper.BiomeType.TROPICAL_FOREST)) {
            return ConfigTFC.Animals.SABER_TOOTH.rarity;
        }
        return 0;
    }

    @Override
    public BiConsumer<List<EntityLiving>, Random> getGroupingRules() {
        return AnimalGroupingRules.ELDER_AND_POPULATION;
    }

    @Override
    public int getDaysToAdulthood() {
        return DAYS_TO_ADULTHOOD;
    }

    @Override
    public int getDaysToElderly() {
        return 0;
    }

    @Override
    public void birthChildren() {
        // Not farmable
    }

    @Override
    public long gestationDays() {
        return 0; // not farmable
    }

    @Override
    public boolean canMateWith(EntityAnimal otherAnimal) {
        return false;
    }

    @Override
    public double getOldDeathChance() {
        return 0;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return TFCSounds.ANIMAL_SABERTOOTH_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TFCSounds.ANIMAL_SABERTOOTH_DEATH;
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        double attackDamage = this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        if (this.isChild()) {
            attackDamage /= 2;
        }
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float) attackDamage);
        if (flag) {
            this.applyEnchantments(this, entityIn);
        }
        return flag;
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (!this.hasHome()) {
            this.setHomePosAndDistance(this.getPosition(), 80);
        }
    }

    @Override
    protected void initEntityAI() {
        EntityAIWander wander = new EntityAnimalAIWanderHuntArea(this, 1.0D);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(3, new EntityAnimalAIAttackMelee<>(this, 1.2D, 1.25D, EntityAnimalAIAttackMelee.AttackBehavior.NIGHTTIME_ONLY).setWanderAI(wander));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(5, wander);
        this.tasks.addTask(7, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
        // Avoid players at daytime
        this.tasks.addTask(4, new EntityAIAvoidEntity<>(this, EntityPlayer.class, 16.0F, 1.0D, 1.25D));

        int priority = 2;
        for (String input : ConfigTFC.Animals.SABER_TOOTH.huntCreatures) {
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
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.38D);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return Constants.RNG.nextInt(100) < 5 ? TFCSounds.ANIMAL_SABERTOOTH_CRY : TFCSounds.ANIMAL_SABERTOOTH_SAY;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTablesTFC.ANIMALS_SABERTOOTH;
    }

    @Override
    protected void playStepSound(BlockPos pos, Block blockIn) {
        playSound(TFCSounds.ANIMAL_FELINE_STEP, 0.15F, 1.0F);
    }
}
