package net.dries007.tfc.objects.blocks.agriculture;

import su.terrafirmagreg.api.util.BlockUtils;
import su.terrafirmagreg.api.util.TileUtils;
import su.terrafirmagreg.modules.core.capabilities.chunkdata.ProviderChunkData;
import su.terrafirmagreg.modules.core.capabilities.player.CapabilityPlayer;
import su.terrafirmagreg.modules.world.classic.ChunkGenClassic;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;


import net.dries007.tfc.ConfigTFC;
import net.dries007.tfc.api.types.ICrop;
import net.dries007.tfc.objects.blocks.plants.BlockEmergentTallWaterPlantTFC;
import net.dries007.tfc.objects.blocks.plants.BlockWaterPlantTFC;
import net.dries007.tfc.objects.blocks.plants.BlockWaterPlantTFCF;
import net.dries007.tfc.objects.blocks.stone.BlockFarmlandTFC;
import net.dries007.tfc.objects.items.ItemSeedsTFC;
import net.dries007.tfc.objects.te.TECropBase;
import net.dries007.tfc.util.agriculture.Crop;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.skills.SimpleSkill;
import net.dries007.tfc.util.skills.SkillType;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public abstract class BlockCropTFC extends BlockBush { //implements IGrowingPlant

    // stage properties
    public static final PropertyInteger STAGE_8 = PropertyInteger.create("stage", 0, 7);
    public static final PropertyInteger STAGE_7 = PropertyInteger.create("stage", 0, 6);
    public static final PropertyInteger STAGE_6 = PropertyInteger.create("stage", 0, 5);
    public static final PropertyInteger STAGE_5 = PropertyInteger.create("stage", 0, 4);

    // static map for conversion from maxValue to Stage Property
    public static final HashMap<Integer, PropertyInteger> STAGE_MAP = new HashMap<>();

    /* true if the crop spawned in the wild, means it ignores growth conditions i.e. farmland */
    public static final PropertyBool WILD = PropertyBool.create("wild");

    // model boxes
    private static final AxisAlignedBB[] CROPS_AABB = new AxisAlignedBB[] {
            new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.125D, 0.875D),
            new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.25D, 0.875D),
            new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.375D, 0.875D),
            new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.5D, 0.875D),
            new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.625D, 0.875D),
            new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.75D, 0.875D),
            new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 0.875D, 0.875D),
            new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D)
    };

    // binary flags for state and metadata conversion
    private static final int META_WILD = 8;
    private static final int META_GROWTH = 7;

    // static field for conversion from crop to Block
    private static final Map<ICrop, BlockCropTFC> MAP = new HashMap<>();

    static {
        STAGE_MAP.put(5, STAGE_5);
        STAGE_MAP.put(6, STAGE_6);
        STAGE_MAP.put(7, STAGE_7);
        STAGE_MAP.put(8, STAGE_8);
    }

    private final ICrop crop;

    public BlockCropTFC(ICrop crop) {
        super(Material.PLANTS);

        this.crop = crop;
        if (MAP.put(crop, this) != null) {
            throw new IllegalStateException("There can only be one.");
        }

        setSoundType(SoundType.PLANT);
        setHardness(0.6f);
    }

    public static BlockCropTFC get(ICrop crop) {
        return MAP.get(crop);
    }

    public static Set<ICrop> getCrops() {
        return MAP.keySet();
    }

    static PropertyInteger getStagePropertyForCrop(ICrop crop) {
        return STAGE_MAP.get(crop.getMaxStage() + 1);
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(WILD, (meta & META_WILD) > 0)
                .withProperty(getStageProperty(), meta & META_GROWTH);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(getStageProperty()) + (state.getValue(WILD) ? META_WILD : 0);
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        super.updateTick(worldIn, pos, state, random);
        checkGrowth(worldIn, pos, state, random);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        TECropBase tile = TileUtils.getTile(worldIn, pos, TECropBase.class);
        if (tile != null) {
            tile.resetCounter();
        }
    }

    @Override
    @NotNull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, getStageProperty(), WILD);
    }

    @Override
    @NotNull
    public Block.EnumOffsetType getOffsetType() {
        return Block.EnumOffsetType.XZ;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TECropBase();
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        EntityPlayer player = harvesters.get();
        ItemStack seedStack = new ItemStack(ItemSeedsTFC.get(crop));
        ItemStack foodStack = crop.getFoodDrop(state.getValue(getStageProperty()));

        // if player and skills are present, update skills and increase amounts of items depending on skill
        if (player != null) {
            SimpleSkill skill = CapabilityPlayer.getSkill(player, SkillType.AGRICULTURE);

            if (skill != null) {
                if (!foodStack.isEmpty()) {
                    foodStack.setCount(1 + Crop.getSkillFoodBonus(skill, RANDOM));
                    seedStack.setCount(1 + Crop.getSkillSeedBonus(skill, RANDOM));
                    skill.add(0.04f);
                }
            }
        }

        // add items to drop
        if (!foodStack.isEmpty()) {
            drops.add(foodStack);
        }
        if (!seedStack.isEmpty()) {
            drops.add(seedStack);
        }
    }

    @Override
    @NotNull
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(ItemSeedsTFC.get(crop));
    }

    @NotNull
    public ICrop getCrop() {
        return crop;
    }

    public void checkGrowth(World worldIn, BlockPos pos, IBlockState state, Random random) {
        if (!worldIn.isRemote) {
            var tile = TileUtils.getTile(worldIn, pos, TECropBase.class);
            if (tile != null) {
                // If can't see sky, or isn't moisturized, reset growth *evil laughter* >:)
                IBlockState stateFarmland = worldIn.getBlockState(pos.down());
                if (!state.getValue(WILD)) {
                    if (!worldIn.canSeeSky(pos) ||
                            (stateFarmland.getBlock() instanceof BlockFarmlandTFC && stateFarmland.getValue(BlockFarmlandTFC.MOISTURE) < 3)) {
                        tile.resetCounter();
                        return;
                    }
                }

                long growthTicks = (long) (crop.getGrowthTicks() * ConfigTFC.General.FOOD.cropGrowthTimeModifier);
                int fullGrownStages = 0;
                while (tile.getTicksSinceUpdate() > growthTicks) {
                    tile.reduceCounter(growthTicks);

                    // find stats for the time in which the crop would have grown
                    float temp = Climate.getActualTemp(worldIn, pos, -tile.getTicksSinceUpdate());
                    float rainfall = ProviderChunkData.getRainfall(worldIn, pos);

                    // check if the crop could grow, if so, grow
                    if (crop.isValidForGrowth(temp, rainfall)) {
                        grow(worldIn, pos, state, random);
                        state = worldIn.getBlockState(pos);
                        if (state.getBlock() instanceof BlockCropTFC && !state.getValue(WILD) &&
                                state.getValue(getStageProperty()) == crop.getMaxStage()) {
                            fullGrownStages++;
                            if (fullGrownStages > 2) {
                                die(worldIn, pos, state, random);
                                return;
                            }
                        }
                    } else if (!crop.isValidConditions(temp, rainfall)) {
                        die(worldIn, pos, state, random);
                        return;
                    }
                }
            }
        }
    }

    public abstract void grow(World worldIn, BlockPos pos, IBlockState state, Random random);

    public void die(World worldIn, BlockPos pos, IBlockState state, Random random) {
        if (ConfigTFC.General.FOOD.enableCropDeath) {
            worldIn.setBlockState(pos, BlockCropDead.get(crop)
                    .getDefaultState()
                    .withProperty(BlockCropDead.MATURE, state.getValue(getStageProperty()) == crop.getMaxStage()));
        }
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return CROPS_AABB[state.getValue(getStageProperty())];
    }

    @NotNull
    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Crop;
    }

    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        if (this.crop != Crop.RICE) {
            return super.canPlaceBlockAt(worldIn, pos);
        } else {
            return super.canPlaceBlockAt(worldIn, pos) && this.canBlockStay(worldIn, pos, worldIn.getBlockState(pos));
        }
    }

    protected boolean canSustainBush(IBlockState state) {
        if (this.crop != Crop.RICE) {
            return super.canSustainBush(state);
        } else {
            return BlockUtils.isWater(state) || state.getMaterial() == Material.ICE && state == ChunkGenClassic.FRESH_WATER ||
                    state.getMaterial() == Material.CORAL && !(state.getBlock() instanceof BlockEmergentTallWaterPlantTFC);
        }
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        if (this.crop != Crop.RICE) {
            return super.canBlockStay(worldIn, pos, state);
        } else {
            IBlockState soil = worldIn.getBlockState(pos.down());
            if (!(soil.getBlock() instanceof BlockWaterPlantTFCF) && !(soil.getBlock() instanceof BlockWaterPlantTFC)) {
                if (state.getBlock() != this) {
                    return this.canSustainBush(soil);
                } else {
                    IBlockState stateDown = worldIn.getBlockState(pos.down());
                    Material material = stateDown.getMaterial();
                    return soil.getBlock()
                            .canSustainPlant(soil, worldIn, pos.down(), EnumFacing.UP, this) ||
                            material == Material.WATER && stateDown.getValue(BlockLiquid.LEVEL) == 0 &&
                                    stateDown == ChunkGenClassic.FRESH_WATER || material == Material.ICE ||
                            material == Material.CORAL && !(state.getBlock() instanceof BlockEmergentTallWaterPlantTFC);
                }
            } else {
                return false;
            }
        }
    }

    public abstract PropertyInteger getStageProperty();
}
