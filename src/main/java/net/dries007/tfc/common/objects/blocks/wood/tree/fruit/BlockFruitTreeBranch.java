//package net.dries007.tfc.common.objects.blocks.tree.fruit;
//
//import net.dries007.tfc.api.types.wood.variant.block.IWoodBlock;
//import net.dries007.tfc.api.types.wood.type.WoodType;
//import net.dries007.tfc.api.types.wood.variant.block.WoodBlockVariant;
//import net.dries007.tfc.api.util.IGrowingPlant;
//import net.dries007.tfc.client.util.CustomStateMap;
//import net.dries007.tfc.common.objects.CreativeTabsTFC;
//import net.dries007.tfc.common.objects.blocks.TFCBlocks;
//import net.dries007.tfc.common.objects.items.itemblocks.ItemBlockTFC;
//import net.dries007.tfc.util.climate.ClimateTFC;
//import net.dries007.tfc.world.classic.chunkdata.ChunkDataTFC;
//import net.minecraft.block.Block;
//import net.minecraft.block.SoundType;
//import net.minecraft.block.material.Material;
//import net.minecraft.block.properties.PropertyEnum;
//import net.minecraft.block.properties.PropertyInteger;
//import net.minecraft.block.state.BlockFaceShape;
//import net.minecraft.block.state.BlockStateContainer;
//import net.minecraft.block.state.IBlockState;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.init.Blocks;
//import net.minecraft.init.Items;
//import net.minecraft.inventory.InventoryHelper;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemBlock;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.EnumFacing;
//import net.minecraft.util.math.AxisAlignedBB;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.RayTraceResult;
//import net.minecraft.world.IBlockAccess;
//import net.minecraft.world.World;
//import net.minecraftforge.client.model.ModelLoader;
//
//import javax.annotation.Nonnull;
//import javax.annotation.Nullable;
//import javax.annotation.ParametersAreNonnullByDefault;
//import java.util.Random;
//
//import static net.dries007.tfc.api.types.wood.variant.block.WoodBlockVariants.FRUIT_SAPLING;
//import static net.dries007.tfc.api.types.wood.variant.block.WoodBlockVariants.FRUIT_TRUNK;
//
//@ParametersAreNonnullByDefault
//public class BlockFruitTreeBranch extends Block implements IGrowingPlant, IWoodBlock {
//    /* Facing of this branch */
//    public static final PropertyEnum<EnumFacing> FACING = PropertyEnum.create("facing", EnumFacing.class);
//
//    /* Connection sides
//     * 0 = no connection
//     * 1 = connected, use vertical model
//     * 2 = connected=, use horizontal model */
//    public static final PropertyInteger NORTH = PropertyInteger.create("north", 0, 2);
//    public static final PropertyInteger EAST = PropertyInteger.create("east", 0, 2);
//    public static final PropertyInteger SOUTH = PropertyInteger.create("south", 0, 2);
//    public static final PropertyInteger WEST = PropertyInteger.create("west", 0, 2);
//    public static final PropertyInteger UP = PropertyInteger.create("up", 0, 2);
//
//    private static final AxisAlignedBB TRUNK_N_AABB = new AxisAlignedBB(0.375D, 0.375D, 0.375D, 0.625D, 0.625D, 1.0D);
//    private static final AxisAlignedBB TRUNK_E_AABB = new AxisAlignedBB(0.0D, 0.375D, 0.375D, 0.625D, 0.625D, 0.625D);
//    private static final AxisAlignedBB TRUNK_S_AABB = new AxisAlignedBB(0.375D, 0.375D, 0.0D, 0.625D, 0.625D, 0.625D);
//    private static final AxisAlignedBB TRUNK_W_AABB = new AxisAlignedBB(0.375D, 0.375D, 0.375D, 1.0D, 0.625D, 0.625D);
//
//
//    private static final AxisAlignedBB TRUNK_U_AABB = new AxisAlignedBB(0.3125D, 0.0D, 0.3125D, 0.6875D, 1.0D, 0.6875D);
//
//    private static final AxisAlignedBB CONNECTION_N_AABB = new AxisAlignedBB(0.3125D, 0.375D, 0.0D, 0.0D, 0.625D, 0.3125D);
//    private static final AxisAlignedBB CONNECTION_S_AABB = new AxisAlignedBB(0.3125D, 0.375D, 0.6875D, 0.0D, 0.625D, 1.0D);
//    private static final AxisAlignedBB CONNECTION_W_AABB = new AxisAlignedBB(0.0D, 0.375D, 0.3125D, 0.3125D, 0.625D, 0.6875D);
//    private static final AxisAlignedBB CONNECTION_E_AABB = new AxisAlignedBB(0.6875D, 0.375D, 0.3125D, 1.0D, 0.625D, 0.6875D);
//
//
//    private final WoodBlockVariant variant;
//    private final WoodType type;
//
//    public BlockFruitTreeBranch(WoodBlockVariant variant, WoodType type) {
//        super(Material.WOOD, Material.WOOD.getMaterialMapColor());
//
//        this.variant = variant;
//        this.type = type;
//
//        setRegistryName(getRegistryLocation());
//        setTranslationKey(getTranslationName());
//        setCreativeTab(CreativeTabsTFC.WOOD);
//
//        setHardness(2.0F);
//        setHarvestLevel("axe", 0);
//        setSoundType(SoundType.WOOD);
//        setDefaultState(blockState.getBaseState()
//                .withProperty(FACING, EnumFacing.UP)
//                .withProperty(NORTH, 0)
//                .withProperty(EAST, 0)
//                .withProperty(SOUTH, 0)
//                .withProperty(WEST, 0)
//                .withProperty(UP, 0));
//
//        Blocks.FIRE.setFireInfo(this, 5, 20);
//    }
//
//    @Override
//    public WoodBlockVariant getBlockVariant() {
//        return variant;
//    }
//
//    @Override
//    public WoodType getTree() {
//        return type;
//    }
//
//    @Nullable
//    @Override
//    public ItemBlock getItemBlock() {
//        return new ItemBlockTFC(this);
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public boolean isTopSolid(IBlockState state) {
//        return false;
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public boolean isFullBlock(IBlockState state) {
//        return false;
//    }
//
//    @Override
//    public int getMetaFromState(IBlockState state) {
//        return 0;
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    @Nonnull
//    public IBlockState getActualState(@Nonnull IBlockState state, IBlockAccess worldIn, BlockPos pos) {
//        int connectedValue;
//        EnumFacing face = getFacing(worldIn, pos);
//        if (face == null || face == EnumFacing.UP || face == EnumFacing.DOWN) {
//            // Vertical branch
//            state = state.withProperty(FACING, EnumFacing.UP);
//            connectedValue = 1;
//        } else {
//            // Horizontal branch
//            state = state.withProperty(FACING, face);
//            connectedValue = 2;
//        }
//        for (EnumFacing facing : EnumFacing.VALUES) {
//            if (worldIn.getBlockState(pos.offset(facing)).getBlock() instanceof BlockFruitTreeLeaves) {
//                if (facing == EnumFacing.NORTH) {
//                    state = state.withProperty(NORTH, connectedValue);
//                } else if (facing == EnumFacing.SOUTH) {
//                    state = state.withProperty(SOUTH, connectedValue);
//                } else if (facing == EnumFacing.EAST) {
//                    state = state.withProperty(EAST, connectedValue);
//                } else if (facing == EnumFacing.WEST) {
//                    state = state.withProperty(WEST, connectedValue);
//                } else if (facing == EnumFacing.UP) {
//                    state = state.withProperty(UP, connectedValue);
//                }
//            }
//        }
//        return state;
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public boolean isBlockNormalCube(IBlockState state) {
//        return false;
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public boolean isNormalCube(IBlockState state) {
//        return false;
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public boolean isFullCube(IBlockState state) {
//        return false;
//    }
//
//    @Override
//    @SuppressWarnings("deprecation")
//    @Nonnull
//    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
//        state = getActualState(state, source, pos);
//        AxisAlignedBB finalAABB = switch (state.getValue(FACING)) {
//            case NORTH -> TRUNK_N_AABB;
//            case EAST -> TRUNK_E_AABB;
//            case SOUTH -> TRUNK_S_AABB;
//            case WEST -> TRUNK_W_AABB;
//            default -> TRUNK_U_AABB;
//        };
//        if (state.getValue(NORTH) > 0) {
//            finalAABB = finalAABB.union(CONNECTION_N_AABB);
//        }
//        if (state.getValue(EAST) > 0) {
//            finalAABB = finalAABB.union(CONNECTION_E_AABB);
//        }
//        if (state.getValue(SOUTH) > 0) {
//            finalAABB = finalAABB.union(CONNECTION_S_AABB);
//        }
//        if (state.getValue(WEST) > 0) {
//            finalAABB = finalAABB.union(CONNECTION_W_AABB);
//        }
//        return finalAABB;
//    }
//
//    @Override
//    @Nonnull
//    @SuppressWarnings("deprecation")
//    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
//        return BlockFaceShape.UNDEFINED;
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public boolean isOpaqueCube(IBlockState state) {
//        return false;
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
//        super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
//        if (getFacing(worldIn, pos) == null) {
//            worldIn.setBlockToAir(pos);
//        }
//    }
//
//    @Override
//    @Nonnull
//    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
//        return Items.AIR;
//    }
//
//    @Override
//    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
//        ItemStack stack = player.getHeldItemMainhand();
//        if (stack.getItem().getToolClasses(stack).contains("axe") || stack.getItem().getToolClasses(stack).contains("saw")) {
//            if (!worldIn.isRemote && RANDOM.nextBoolean()) {
//                ItemStack dropStack = new ItemStack(TFCBlocks.getWoodBlock(FRUIT_SAPLING, type));
//                InventoryHelper.spawnItemStack(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, dropStack);
//            }
//        }
//        super.onBlockHarvested(worldIn, pos, state, player);
//    }
//
//    @Override
//    @Nonnull
//    public BlockStateContainer createBlockState() {
//        return new BlockStateContainer(this, FACING, NORTH, EAST, SOUTH, WEST, UP);
//    }
//
//    @Override
//    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
//        return false;
//    }
//
//    @SuppressWarnings("deprecation")
//    @Override
//    public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
//        return false;
//    }
//
//    @Override
//    @Nonnull
//    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
//        return new ItemStack(TFCBlocks.getWoodBlock(FRUIT_SAPLING, type));
//    }
//
//
//    private EnumFacing getFacing(IBlockAccess worldIn, BlockPos pos) {
//        for (EnumFacing facing : EnumFacing.VALUES) {
//            if (worldIn.getBlockState(pos.offset(facing)).getBlock() == TFCBlocks.getWoodBlock(FRUIT_TRUNK, type)) {
//                return facing.getOpposite();
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public GrowthStatus getGrowingStatus(IBlockState state, World world, BlockPos pos) {
//        float temp = ClimateTFC.getActualTemp(world, pos);
//        float rainfall = ChunkDataTFC.getRainfall(world, pos);
//        boolean canGrow = type.isValidForGrowth(temp, rainfall);
//        if (canGrow) {
//            return GrowthStatus.GROWING;
//        }
//        return GrowthStatus.NOT_GROWING;
//    }
//
//    @Override
//    public void onModelRegister() {
//        ModelLoader.setCustomStateMapper(this, new CustomStateMap.Builder().customPath(getRegistryLocation()).build());
//    }
//}
