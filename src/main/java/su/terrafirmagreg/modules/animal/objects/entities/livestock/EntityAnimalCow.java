package su.terrafirmagreg.modules.animal.objects.entities.livestock;

import net.dries007.tfc.api.capability.food.CapabilityFood;
import net.dries007.tfc.api.capability.food.IFood;
import net.dries007.tfc.util.calendar.CalendarTFC;
import net.dries007.tfc.util.climate.BiomeHelper;
import net.dries007.tfc.world.classic.biomes.BiomesTFC;
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
import su.terrafirmagreg.Tags;
import su.terrafirmagreg.api.lib.Constants;
import su.terrafirmagreg.modules.animal.ModuleAnimal;
import su.terrafirmagreg.modules.animal.ModuleAnimalConfig;
import su.terrafirmagreg.modules.animal.api.type.ILivestock;
import su.terrafirmagreg.modules.animal.api.util.AnimalGroupingRules;
import su.terrafirmagreg.modules.animal.data.LootTablesAnimal;
import su.terrafirmagreg.modules.animal.objects.entities.EntityAnimalMammal;
import su.terrafirmagreg.modules.animal.objects.entities.TFCEntities;
import su.terrafirmagreg.modules.core.network.SCPacketSimpleMessage;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;


public class EntityAnimalCow extends EntityAnimalMammal implements ILivestock {
	private static final DataParameter<Long> MILKED = EntityDataManager.createKey(EntityAnimalCow.class, TFCEntities.getLongDataSerializer());

	@SuppressWarnings("unused")
	public EntityAnimalCow(World worldIn) {
		this(worldIn, Gender.valueOf(Constants.RANDOM.nextBoolean()), getRandomGrowth(ModuleAnimalConfig.ENTITIES.COW.adulthood, ModuleAnimalConfig.ENTITIES.COW.elder));
	}

	public EntityAnimalCow(World worldIn, Gender gender, int birthDay) {
		super(worldIn, gender, birthDay);
		setSize(1.2F, 1.3F);
		setMilkedTick(0);
	}

	@Override
	public int getSpawnWeight(Biome biome, float temperature, float rainfall, float floraDensity, float floraDiversity) {
		BiomeHelper.BiomeType biomeType = BiomeHelper.getBiomeType(temperature, rainfall, floraDensity);
		if (!BiomesTFC.isOceanicBiome(biome) && !BiomesTFC.isBeachBiome(biome) &&
				(biomeType == BiomeHelper.BiomeType.PLAINS)) {
			return ModuleAnimalConfig.ENTITIES.COW.rarity;
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
	public void birthChildren() {
		int numberOfChildren = ModuleAnimalConfig.ENTITIES.COW.babies; //one always
		for (int i = 0; i < numberOfChildren; i++) {
			EntityAnimalCow baby = new EntityAnimalCow(world, Gender.valueOf(Constants.RANDOM.nextBoolean()), (int) CalendarTFC.PLAYER_TIME.getTotalDays());
			baby.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);
			baby.setFamiliarity(getFamiliarity() < 0.9F ? getFamiliarity() / 2.0F : getFamiliarity() * 0.9F);
			world.spawnEntity(baby);
		}
	}

	@Override
	public long gestationDays() {
		return ModuleAnimalConfig.ENTITIES.COW.gestation;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		getDataManager().register(MILKED, 0L);
	}

	@Override
	public void writeEntityToNBT(@NotNull NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setLong("milkedTick", getMilkedTick());
	}

	@Override
	public void readEntityFromNBT(@NotNull NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		setMilkedTick(compound.getLong("milkedTick"));
	}

	@Override
	public boolean processInteract(@NotNull EntityPlayer player, @NotNull EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		FluidActionResult fillResult = FluidUtil.tryFillContainer(itemstack, FluidUtil.getFluidHandler(new ItemStack(Items.MILK_BUCKET)),
				Fluid.BUCKET_VOLUME, player, false);

		// First check if it is possible to fill the player's held item with milk
		if (fillResult.isSuccess()) {
			if (getFamiliarity() > 0.15f && isReadyForAnimalProduct()) {
				player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
				setProductsCooldown();
				player.setHeldItem(hand, FluidUtil.tryFillContainerAndStow(itemstack, FluidUtil.getFluidHandler(new ItemStack(Items.MILK_BUCKET)),
						new PlayerInvWrapper(player.inventory), Fluid.BUCKET_VOLUME, player, true).getResult());
			} else if (!world.isRemote) {
				//Return chat message indicating why this entity isn't giving milk
				TextComponentTranslation tooltip = getTooltip();
				if (tooltip != null) {
					ModuleAnimal.PACKET_SERVICE.sendTo(new SCPacketSimpleMessage(SCPacketSimpleMessage.MessageCategory.ANIMAL, tooltip), (EntityPlayerMP) player);
				}
			}
			return true;
		} else {
			return super.processInteract(player, hand);
		}
	}

	@Override
	public double getOldDeathChance() {
		return ModuleAnimalConfig.ENTITIES.COW.oldDeathChance;
	}

	@Override
	protected boolean eatFood(@NotNull ItemStack stack, EntityPlayer player) {
		// Refuses to eat rotten stuff
		IFood cap = stack.getCapability(CapabilityFood.CAPABILITY, null);
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
		return ModuleAnimalConfig.ENTITIES.COW.adulthood;
	}

	@Override
	public int getDaysToElderly() {
		return ModuleAnimalConfig.ENTITIES.COW.elder;
	}

	@Override
	public boolean isReadyForAnimalProduct() {
		return getFamiliarity() > 0.15f && hasMilk();
	}

	@Override
	public void setProductsCooldown() {
		setMilkedTick(CalendarTFC.PLAYER_TIME.getTicks());
	}

	@Override
	public long getProductsCooldown() {
		return Math.max(0, ModuleAnimalConfig.ENTITIES.COW.milkTicks + getMilkedTick() - CalendarTFC.PLAYER_TIME.getTicks());
	}

	@Override
	public TextComponentTranslation getTooltip() {
		if (getGender() == Gender.MALE) {
			return new TextComponentTranslation(Tags.MOD_ID + ".tooltip.animal.product.male_milk");
		} else if (getAge() == Age.OLD) {
			return new TextComponentTranslation(Tags.MOD_ID + ".tooltip.animal.product.old", getAnimalName());
		} else if (getAge() == Age.CHILD) {
			return new TextComponentTranslation(Tags.MOD_ID + ".tooltip.animal.product.young", getAnimalName());
		} else if (getFamiliarity() <= 0.15f) {
			return new TextComponentTranslation(Tags.MOD_ID + ".tooltip.animal.product.low_familiarity", getAnimalName());
		} else if (!hasMilk()) {
			return new TextComponentTranslation(Tags.MOD_ID + ".tooltip.animal.product.no_milk", getAnimalName());
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

	protected long getMilkedTick() {
		return dataManager.get(MILKED);
	}

	protected void setMilkedTick(long tick) {
		dataManager.set(MILKED, tick);
	}
}
