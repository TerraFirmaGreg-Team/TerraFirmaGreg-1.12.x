package su.terrafirmagreg.modules.device.objects.blocks;

import su.terrafirmagreg.api.spi.block.BlockBase;
import su.terrafirmagreg.api.spi.tile.ITEBlock;
import su.terrafirmagreg.api.util.TileUtils;
import su.terrafirmagreg.modules.core.client.GuiHandler;
import su.terrafirmagreg.modules.device.init.BlocksDevice;
import su.terrafirmagreg.modules.device.objects.items.ItemFireStarter;
import su.terrafirmagreg.modules.device.objects.tiles.TEBellows;
import su.terrafirmagreg.modules.device.objects.tiles.TECharcoalForge;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.dries007.tfc.api.util.IBellowsConsumerBlock;
import net.dries007.tfc.util.block.Multiblock;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.function.BiPredicate;

import static su.terrafirmagreg.api.util.PropertyUtils.LIT;

public class BlockCharcoalForge extends BlockBase implements IBellowsConsumerBlock, ITEBlock {

    private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.875D, 1.0D);
    private static final Multiblock CHARCOAL_FORGE_MULTIBLOCK;

    static {
        BiPredicate<World, BlockPos> skyMatcher = World::canBlockSeeSky;
        BiPredicate<World, BlockPos> isValidSide = (world, pos) -> BlockCharcoalForge.isValidSide(world.getBlockState(pos));
        CHARCOAL_FORGE_MULTIBLOCK = new Multiblock()
                // Top block
                .match(new BlockPos(0, 1, 0), state -> state.getBlock() == BlocksDevice.CRUCIBLE || state.getBlock() == Blocks.AIR)
                // Chimney
                .matchOneOf(new BlockPos(0, 1, 0), new Multiblock()
                        .match(new BlockPos(0, 0, 0), skyMatcher)
                        .match(new BlockPos(0, 0, 1), skyMatcher)
                        .match(new BlockPos(0, 0, 2), skyMatcher)
                        .match(new BlockPos(0, 0, -1), skyMatcher)
                        .match(new BlockPos(0, 0, -2), skyMatcher)
                        .match(new BlockPos(1, 0, 0), skyMatcher)
                        .match(new BlockPos(2, 0, 0), skyMatcher)
                        .match(new BlockPos(-1, 0, 0), skyMatcher)
                        .match(new BlockPos(-2, 0, 0), skyMatcher)
                )
                // Underneath
                .match(new BlockPos(1, 0, 0), isValidSide)
                .match(new BlockPos(-1, 0, 0), isValidSide)
                .match(new BlockPos(0, 0, 1), isValidSide)
                .match(new BlockPos(0, 0, -1), isValidSide)
                .match(new BlockPos(0, -1, 0), isValidSide);
    }

    public BlockCharcoalForge() {
        super(BlockCharcoalPile.CHARCOAL_MATERIAL);

        setSoundType(SoundType.GROUND);
        setHarvestLevel("shovel", 0);
        setHardness(1.0F);
        setTickRandomly(true); // Used for chimney checks -> extinguish
        this.setDefaultState(this.blockState.getBaseState().withProperty(LIT, false));
    }

    public static boolean isValid(World world, BlockPos pos) {
        return CHARCOAL_FORGE_MULTIBLOCK.test(world, pos);
    }

    public static boolean isValidSide(IBlockState state) {
        return state.getMaterial() == Material.ROCK && state.isOpaqueCube() && state.isNormalCube();
    }

    @Override
    public boolean canIntakeFrom(Vec3i offset, EnumFacing facing) {
        return offset.equals(TEBellows.OFFSET_INSET);
    }

    @Override
    public void onAirIntake(World world, BlockPos pos, int airAmount) {
        TECharcoalForge teForge = TileUtils.getTile(world, pos, TECharcoalForge.class);
        if (teForge != null) {
            teForge.onAirIntake(airAmount);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isTopSolid(IBlockState state) {
        return false;
    }

    @NotNull
    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(LIT, meta == 1);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LIT) ? 1 : 0;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB;
    }

    @Override
    @NotNull
    @SuppressWarnings("deprecation")
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return face.getAxis() == EnumFacing.Axis.Y ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
    }

    @Override
    @Nullable
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return AABB;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        TECharcoalForge te = TileUtils.getTile(worldIn, pos, TECharcoalForge.class);
        // Have to check the above block, since minecraft think this block is "roof"
        if (te != null && state.getValue(LIT) && worldIn.isRainingAt(pos.up())) {
            te.onRainDrop();
        }
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (!isValid(worldIn, pos)) {
            worldIn.setBlockState(pos, state.withProperty(LIT, false));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rng) {
        if (state.getValue(LIT)) {
            double x = pos.getX() + 0.5;
            double y = pos.getY() + 0.1;
            double z = pos.getZ() + 0.5;

            world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, x + rng.nextFloat() - 0.5, y, z + rng.nextFloat() - 0.5, 0.0D, 0.2D, 0.0D);
            if (rng.nextInt(3) == 1)
                world.spawnParticle(EnumParticleTypes.LAVA, x + rng.nextFloat() - 0.5, y, z + rng.nextFloat() - 0.5, 0.0D, 0.2D, 0.0D);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!worldIn.isRemote) {
            if (state.getValue(LIT) && !isValid(worldIn, pos)) {
                // This is not a valid pit, therefor extinguish it
                worldIn.setBlockState(pos, state.withProperty(LIT, false));
            }
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        TECharcoalForge te = TileUtils.getTile(worldIn, pos, TECharcoalForge.class);
        if (te != null) {
            te.onBreakBlock(worldIn, pos, state);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int quantityDropped(Random random) {
        return 7;
    }

    @Override
    @NotNull
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.COAL;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 1;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX,
                                    float hitY, float hitZ) {
        if (!world.isRemote) {
            if (!state.getValue(LIT)) {
                ItemStack held = player.getHeldItem(hand);
                if (isValid(world, pos) && ItemFireStarter.onIgnition(held)) {
                    world.setBlockState(pos, state.withProperty(LIT, true));
                    return true;
                }
            }
            if (!player.isSneaking()) {
                GuiHandler.openGui(world, pos, player, GuiHandler.Type.CHARCOAL_FORGE);
            }
        }
        return true;
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        IBlockState state = worldIn.getBlockState(pos);
        if (state.getValue(LIT) && !entityIn.isImmuneToFire() && entityIn instanceof EntityLivingBase && state.getValue(LIT)) {
            entityIn.attackEntityFrom(DamageSource.IN_FIRE, 2.0F);
        }
        super.onEntityWalk(worldIn, pos, entityIn);
    }

    @Override
    @NotNull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LIT);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TECharcoalForge();
    }

    @Override
    @NotNull
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(Items.COAL, 1, 1);
    }

    @Nullable
    @Override
    public PathNodeType getAiPathNodeType(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EntityLiving entity) {
        return state.getValue(LIT) && (entity == null || !entity.isImmuneToFire()) ? net.minecraft.pathfinding.PathNodeType.DAMAGE_FIRE : null;
    }

    @Override
    public @NotNull String getName() {
        return "device/charcoal_forge";
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TECharcoalForge.class;
    }
}
