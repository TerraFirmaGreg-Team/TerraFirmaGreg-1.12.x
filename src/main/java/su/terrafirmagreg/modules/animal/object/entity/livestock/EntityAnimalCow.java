package su.terrafirmagreg.modules.animal.object.entity.livestock;

import su.terrafirmagreg.api.helper.BiomeHelper;
import su.terrafirmagreg.api.util.BiomeUtils;
import su.terrafirmagreg.api.util.ModUtils;
import su.terrafirmagreg.api.util.NBTUtils;
import su.terrafirmagreg.framework.network.spi.datasync.DataSerializers;
import su.terrafirmagreg.modules.animal.ConfigAnimal;
import su.terrafirmagreg.modules.animal.ModuleAnimal;
import su.terrafirmagreg.modules.animal.api.type.ILivestock;
import su.terrafirmagreg.modules.animal.api.util.AnimalGroupingRules;
import su.terrafirmagreg.modules.animal.init.LootTablesAnimal;
import su.terrafirmagreg.modules.animal.object.entity.EntityAnimalMammal;
import su.terrafirmagreg.modules.core.capabilities.food.CapabilityFood;
import su.terrafirmagreg.modules.core.capabilities.food.ICapabilityFood;
import su.terrafirmagreg.modules.core.feature.calendar.Calendar;
import su.terrafirmagreg.modules.core.network.SCPacketSimple;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

import static su.terrafirmagreg.api.util.MathUtils.RNG;

public class EntityAnimalCow extends EntityAnimalMammal implements ILivestock {

  private static final DataParameter<Long> MILKED = EntityDataManager.createKey(
    EntityAnimalCow.class, DataSerializers.LONG);

  @SuppressWarnings("unused")
  public EntityAnimalCow(World worldIn) {
    this(worldIn, Gender.valueOf(RNG.nextBoolean()),
      getRandomGrowth(ConfigAnimal.ENTITY.COW.adulthood, ConfigAnimal.ENTITY.COW.elder));
  }

  public EntityAnimalCow(World worldIn, Gender gender, int birthDay) {
    super(worldIn, gender, birthDay);
    setSize(1.2F, 1.3F);
    setMilkedTick(0);
  }

  @Override
  public int getSpawnWeight(Biome biome, float temperature, float rainfall, float floraDensity,
                            float floraDiversity) {
    BiomeUtils.BiomeType biomeType = BiomeUtils.getBiomeType(temperature, rainfall, floraDensity);
    if (!BiomeHelper.isOceanic(biome) && !BiomeHelper.isBeach(biome) &&
        (biomeType == BiomeUtils.BiomeType.PLAINS)) {
      return ConfigAnimal.ENTITY.COW.rarity;
    }
    return 0;
  }

  @Override
  public BiConsumer<List<EntityLiving>, Random> getGroupingRules() {
    return AnimalGroupingRules.MALE_AND_FEMALES;
  }

  @Override
  public int getMinGroupSize() {
    return 3;
  }

  @Override
  public int getMaxGroupSize() {
    return 4;
  }

  @Override
  protected void entityInit() {
    super.entityInit();
    getDataManager().register(MILKED, 0L);
  }

  @Override
  public long gestationDays() {
    return ConfigAnimal.ENTITY.COW.gestation;
  }

  @Override
  public void birthChildren() {
    int numberOfChildren = ConfigAnimal.ENTITY.COW.babies; //one always
    for (int i = 0; i < numberOfChildren; i++) {
      EntityAnimalCow baby = new EntityAnimalCow(world, Gender.valueOf(RNG.nextBoolean()),
        (int) Calendar.PLAYER_TIME.getTotalDays());
      baby.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
      baby.setFamiliarity(
        getFamiliarity() < 0.9F ? getFamiliarity() / 2.0F : getFamiliarity() * 0.9F);
      world.spawnEntity(baby);
    }
  }

  @Override
  public void writeEntityToNBT(@NotNull NBTTagCompound nbt) {
    super.writeEntityToNBT(nbt);
    NBTUtils.setGenericNBTValue(nbt, "milkedTick", getMilkedTick());
  }

  @Override
  public void readEntityFromNBT(@NotNull NBTTagCompound nbt) {
    super.readEntityFromNBT(nbt);
    setMilkedTick(nbt.getLong("milkedTick"));
  }

  protected long getMilkedTick() {
    return dataManager.get(MILKED);
  }

  protected void setMilkedTick(long tick) {
    dataManager.set(MILKED, tick);
  }

  @Override
  public double getOldDeathChance() {
    return ConfigAnimal.ENTITY.COW.oldDeathChance;
  }

  @Override
  public boolean processInteract(@NotNull EntityPlayer player, @NotNull EnumHand hand) {
    ItemStack itemstack = player.getHeldItem(hand);
    FluidActionResult fillResult = FluidUtil.tryFillContainer(itemstack,
      FluidUtil.getFluidHandler(new ItemStack(Items.MILK_BUCKET)),
      Fluid.BUCKET_VOLUME, player, false);

    // First check if it is possible to fill the player's held item with milk
    if (fillResult.isSuccess()) {
      if (getFamiliarity() > 0.15f && isReadyForAnimalProduct()) {
        player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
        setProductsCooldown();
        player.setHeldItem(hand, FluidUtil.tryFillContainerAndStow(itemstack,
          FluidUtil.getFluidHandler(new ItemStack(Items.MILK_BUCKET)),
          new PlayerInvWrapper(player.inventory), Fluid.BUCKET_VOLUME, player, true).getResult());
      } else if (!world.isRemote) {
        //Return chat message indicating why this entity isn't giving milk
        TextComponentTranslation tooltip = getTooltip();
        ModuleAnimal.NETWORK.sendTo(
          new SCPacketSimple(SCPacketSimple.MessageCategory.ANIMAL, tooltip),
          (EntityPlayerMP) player);
      }
      return true;
    } else {
      return super.processInteract(player, hand);
    }
  }

  @Override
  protected boolean eatFood(@NotNull ItemStack stack, EntityPlayer player) {
    // Refuses to eat rotten stuff
    ICapabilityFood cap = stack.getCapability(CapabilityFood.CAPABILITY, null);
    if (cap != null) {
      if (cap.isRotten()) {
        return false;
      }
    }
    return super.eatFood(stack, player);
  }

  @Override
  public float getAdultFamiliarityCap() {
    return 0.35F;
  }

  @Override
  public int getDaysToAdulthood() {
    return ConfigAnimal.ENTITY.COW.adulthood;
  }

  @Override
  public int getDaysToElderly() {
    return ConfigAnimal.ENTITY.COW.elder;
  }

  @Override
  public boolean isReadyForAnimalProduct() {
    return getFamiliarity() > 0.15f && hasMilk();
  }

  @Override
  public void setProductsCooldown() {
    setMilkedTick(Calendar.PLAYER_TIME.getTicks());
  }

  @Override
  public long getProductsCooldown() {
    return Math.max(0,
      ConfigAnimal.ENTITY.COW.milkTicks + getMilkedTick() - Calendar.PLAYER_TIME.getTicks());
  }

  @Override
  public TextComponentTranslation getTooltip() {
    if (getGender() == Gender.MALE) {
      return new TextComponentTranslation(ModUtils.localize("tooltip", "animal.product.male_milk"));
    } else if (getAge() == Age.OLD) {
      return new TextComponentTranslation(ModUtils.localize("tooltip", "animal.product.old"),
        getAnimalName());
    } else if (getAge() == Age.CHILD) {
      return new TextComponentTranslation(ModUtils.localize("tooltip", "animal.product.young"),
        getAnimalName());
    } else if (getFamiliarity() <= 0.15f) {
      return new TextComponentTranslation(
        ModUtils.localize("tooltip", "animal.product.low_familiarity"), getAnimalName());
    } else if (!hasMilk()) {
      return new TextComponentTranslation(ModUtils.localize("tooltip", "animal.product.no_milk"),
        getAnimalName());
    }
    return null;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
    return SoundEvents.ENTITY_COW_HURT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_COW_DEATH;
  }

  @Override
  protected void initEntityAI() {
    addCommonLivestockAI(this, 1.2D);
    addCommonPreyAI(this, 1.2);

    tasks.addTask(5, new EntityAIFollowParent(this, 1.1D));
  }

  @Override
  protected void applyEntityAttributes() {
    super.applyEntityAttributes();
    getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
    getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20D);
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENTITY_COW_AMBIENT;
  }

  @Nullable
  protected ResourceLocation getLootTable() {
    return LootTablesAnimal.ANIMALS_COW;
  }

  @Override
  protected void playStepSound(BlockPos pos, Block blockIn) {
    playSound(SoundEvents.ENTITY_COW_STEP, 0.15F, 1.0F);
  }

  protected boolean hasMilk() {
    return getGender() == Gender.FEMALE && getAge() == Age.ADULT && getProductsCooldown() == 0;
  }
}
