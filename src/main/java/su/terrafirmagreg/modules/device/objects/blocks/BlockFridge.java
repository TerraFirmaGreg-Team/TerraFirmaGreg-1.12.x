package su.terrafirmagreg.modules.device.objects.blocks;

import su.terrafirmagreg.api.spi.block.BaseBlockHorizontal;
import su.terrafirmagreg.api.spi.block.ICustomStateBlock;
import su.terrafirmagreg.api.spi.tile.ITileBlock;
import su.terrafirmagreg.api.util.TileUtils;
import su.terrafirmagreg.modules.device.client.render.TESRFridge;
import su.terrafirmagreg.modules.device.objects.itemblocks.ItemBlockFridge;
import su.terrafirmagreg.modules.device.objects.tiles.TileFridge;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

import static net.minecraft.util.EnumFacing.NORTH;

@SuppressWarnings("deprecation")
public class BlockFridge extends BaseBlockHorizontal implements ITileBlock, ICustomStateBlock {

    public static final PropertyBool UPPER = PropertyBool.create("upper"); //true if this is the upper half

    private static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0D, 0D, 0.125D, 1D, 1D, 1D);
    private static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0D, 0D, 0.0D, 1D, 1D, 0.875D);
    private static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0D, 0D, 0.0D, 0.875D, 1D, 1D);
    private static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.125D, 0D, 0.0D, 1D, 1D, 1D);

    private static final Vec3d[] ITEMS;

    static {
        ITEMS = new Vec3d[8];
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 2; x++) {
                double xPos = 0.7D - 0.4D * x;
                double yPos = 0.55D + y * 0.28125D;
                double zPos = 0.5D;
                ITEMS[x + y * 2] = new Vec3d(xPos, yPos, zPos);
            }
        }
    }

    public BlockFridge() {
        super(Settings.of()
                .material(Material.IRON)
                .hardness(3.0F)
                .nonOpaque()
                .nonFullCube()
                .size(Size.HUGE)
                .weight(Weight.MEDIUM)
                .nonCanStack());

        setHarvestLevel("pickaxe", 0);
        setDefaultState(getBlockState().getBaseState()
                .withProperty(FACING, NORTH)
                .withProperty(UPPER, false));
    }

    @Override
    public @Nullable Item getItemBlock() {
        return new ItemBlockFridge(this);
    }

    public static Vec3d[] getItems(EnumFacing facing) {
        Vec3d[] items = new Vec3d[8];
        for (int i = 0; i < 8; i++) {
            Vec3d itemPos = ITEMS[i];
            if (facing == EnumFacing.EAST || facing == EnumFacing.WEST) {
                itemPos = itemPos.rotateYaw((float) Math.toRadians(90D));
                itemPos = itemPos.add(0, 0, 1);
            }
            items[i] = itemPos;
        }
        return items;
    }

    public static int getPlayerLookingItem(BlockPos bottomPos, EntityPlayer player, EnumFacing facing) {
        double length = Math.sqrt(bottomPos.distanceSqToCenter(player.posX, player.posY, player.posZ)) + 0.7D;
        Vec3d startPos = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        Vec3d endPos = startPos.add(new Vec3d(player.getLookVec().x * length, player.getLookVec().y * length, player.getLookVec().z * length));
        Vec3d[] items = getItems(facing);
        for (int i = 0; i < 8; i++) {
            Vec3d itemPos = items[i];
            AxisAlignedBB offsetAABB = new AxisAlignedBB(itemPos.x, itemPos.y, itemPos.z, itemPos.x, itemPos.y, itemPos.z).grow(0.1D)
                    .offset(bottomPos)
                    .grow(0.002D);
            if (offsetAABB.calculateIntercept(startPos, endPos) != null) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState()
                .withProperty(FACING, EnumFacing.byHorizontalIndex(meta))
                .withProperty(UPPER, meta > 3);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex() + (state.getValue(UPPER) ? 4 : 0);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasCustomBreakingProgress(IBlockState state) {
        return true;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return switch (state.getValue(FACING)) {
            case NORTH -> state.getValue(UPPER) ? NORTH_AABB.setMaxY(0.75D) : NORTH_AABB;
            case SOUTH -> state.getValue(UPPER) ? SOUTH_AABB.setMaxY(0.75D) : SOUTH_AABB;
            case EAST -> state.getValue(UPPER) ? EAST_AABB.setMaxY(0.75D) : EAST_AABB;
            case WEST -> state.getValue(UPPER) ? WEST_AABB.setMaxY(0.75D) : WEST_AABB;
            default -> NORTH_AABB;
        };
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (!state.getValue(UPPER)) {
            if (worldIn.getBlockState(pos.up()).getBlock() != this) {
                worldIn.setBlockToAir(pos);
            }
        } else if (worldIn.getBlockState(pos.down()).getBlock() != this) {
            if (!worldIn.isRemote) {
                spawnAsEntity(worldIn, pos, new ItemStack(this));
            }
            worldIn.setBlockToAir(pos);
        }
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        var tile = TileUtils.getTile(worldIn, pos, TileFridge.class);
        if (tile != null) tile.onBreakBlock(worldIn, pos, state);
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return state.getValue(UPPER) ? Item.getItemFromBlock(this) : Items.AIR;
    }

    @Override
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (state.getValue(UPPER)) {
            super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        BlockPos TEPos = pos;
        if (!state.getValue(UPPER)) {
            TEPos = pos.up();
        }
        TileFridge te = TileUtils.getTile(world, TEPos, TileFridge.class);
        if (te != null && !te.isAnimating() && hand == EnumHand.MAIN_HAND && facing == state.getValue(FACING)) {
            if (te.isOpen()) {
                int slot = getPlayerLookingItem(TEPos.down(), player, facing);
                ItemStack stack = player.getHeldItem(hand);
                if (!stack.isEmpty()) {
                    if (slot != -1) {
                        if (!world.isRemote) {
                            player.setHeldItem(hand, te.insertItem(slot, stack));
                        }
                        return true;
                    }
                } else {
                    if (slot != -1 && te.hasStack(slot)) {
                        if (!world.isRemote) {
                            player.setHeldItem(hand, te.extractItem(slot));
                        }
                        return true;
                    } else {
                        return te.setOpening(false);
                    }
                }
            } else {
                if (!player.isSneaking()) {
                    return te.setOpening(true);
                }
            }
        }
        return false;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, UPPER);
    }

    @Override
    public boolean addLandingEffects(IBlockState state, WorldServer worldObj, BlockPos blockPosition, IBlockState iblockstate, EntityLivingBase entity, int numberOfParticles) {
        return true;
    }

    @Override
    public boolean addRunningEffects(IBlockState state, World world, BlockPos pos, Entity entity) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addHitEffects(IBlockState state, World worldObj, RayTraceResult target, ParticleManager manager) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileFridge();
    }

    @Override
    public String getName() {
        return "device/fridge";
    }

    @Override
    public Class<? extends TileEntity> getTileEntityClass() {
        return TileFridge.class;
    }

    @Override
    public @Nullable TileEntitySpecialRenderer<?> getTileRenderer() {
        return new TESRFridge();
    }

    @Override
    public void onStateRegister() {
        ModelLoader.setCustomStateMapper(this, new IStateMapper() {

            @Override
            @NotNull
            public Map<IBlockState, ModelResourceLocation> putStateModelLocations(@NotNull Block blockIn) {
                return Collections.emptyMap();
            }
        });
    }
}
