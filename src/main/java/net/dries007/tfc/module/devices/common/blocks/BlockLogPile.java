package net.dries007.tfc.module.devices.common.blocks;


import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.api.util.IHasModel;
import net.dries007.tfc.api.util.property.ILightableBlock;
import net.dries007.tfc.client.util.CustomStateMap;
import net.dries007.tfc.client.util.TFCGuiHandler;
import net.dries007.tfc.module.core.common.blocks.BlockBase;
import net.dries007.tfc.common.objects.items.ItemFireStarter;
import net.dries007.tfc.common.objects.tileentities.TEInventory;
import net.dries007.tfc.config.ConfigTFC;
import net.dries007.tfc.module.devices.common.tile.TELogPile;
import net.dries007.tfc.module.devices.init.BlocksDevice;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.OreDictionaryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockLogPile extends BlockBase implements ILightableBlock, IHasModel {

    public static final String NAME = "device.log_pile";
    private static final PropertyEnum<EnumFacing.Axis> AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class, EnumFacing.Axis.X, EnumFacing.Axis.Z);

    public BlockLogPile() {
        super(Material.WOOD);

        setHardness(2.0F);
        setSoundType(SoundType.WOOD);
        setTickRandomly(true);
        setHarvestLevel("axe", 0);
        setDefaultState(this.getDefaultState().withProperty(AXIS, EnumFacing.Axis.X).withProperty(LIT, false));
    }

    // A simplified check for display (Patchouli) purposes
    public static boolean isValidCoverBlock(IBlockState state) {
        if (state.getBlock() == BlocksDevice.LOG_PILE || state.getBlock() == BlocksDevice.CHARCOAL_PILE) {
            return true;
        } else if (ConfigTFC.Devices.CHARCOAL_PIT.canAcceptGlass) {
            return state.getMaterial() == Material.GLASS && !(state.getBlock() instanceof BlockPane); // Not enough context to query IBlockProperties#isSideSolid, IBlockProperties#getBlockFaceShape
        }
        return !state.getMaterial().getCanBurn() && state.isNormalCube(); // Not enough context to query IBlockProperties#isSideSolid, IBlockProperties#getBlockFaceShape
    }

    private static boolean isValidCoverBlock(IBlockState offsetState, World world, BlockPos pos, EnumFacing side) {
        if (offsetState.getBlock() instanceof BlockLogPile || offsetState.getBlock() == BlocksDevice.CHARCOAL_PILE) {
            return true;
        } else if (offsetState.getMaterial() == Material.GLASS && ConfigTFC.Devices.CHARCOAL_PIT.canAcceptGlass) {
            return offsetState.getBlockFaceShape(world, pos, side) == BlockFaceShape.SOLID || offsetState.isSideSolid(world, pos, side);
        }
        return !offsetState.getMaterial().getCanBurn() && (offsetState.getBlockFaceShape(world, pos, side) == BlockFaceShape.SOLID) || offsetState.isSideSolid(world, pos, side);
    }

//    @Nullable
//    @Override
//    public ItemBlock getItemBlock() {
//        return null;
//    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(AXIS, meta == 0 ? EnumFacing.Axis.Z : EnumFacing.Axis.X).withProperty(LIT, meta >= 2);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(AXIS) == EnumFacing.Axis.Z ? 0 : 1) + (state.getValue(LIT) ? 2 : 0);
    }

    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
        if (!worldIn.isRemote && state.getValue(LIT)) {
            for (EnumFacing side : EnumFacing.values()) {
                final BlockPos offsetPos = pos.offset(side);
                IBlockState offsetState = worldIn.getBlockState(offsetPos);
                if (isValidCoverBlock(offsetState, worldIn, offsetPos, side.getOpposite())) {
                    if (offsetState.getBlock() instanceof BlockLogPile && !offsetState.getValue(LIT)) {
                        worldIn.setBlockState(offsetPos, offsetState.withProperty(LIT, true));
                    }
                } else {
                    worldIn.setBlockState(pos, Blocks.FIRE.getDefaultState());
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.getValue(LIT)) {
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + rand.nextFloat(), pos.getY() + 1, pos.getZ() + rand.nextFloat(),
                    0f, 0.1f + 0.1f * rand.nextFloat(), 0f);
            if (worldIn.getTotalWorldTime() % 80 == 0) {
                worldIn.playSound((double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_FIRE_AMBIENT, SoundCategory.BLOCKS, 0.5F, 0.6F, false);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (worldIn.getBlockState(pos.up()).getBlock() == Blocks.FIRE) {
            worldIn.setBlockState(pos, state.withProperty(LIT, true));
            TELogPile te = Helpers.getTE(worldIn, pos, TELogPile.class);
            if (te != null) {
                te.light();
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        TELogPile te = Helpers.getTE(world, pos, TELogPile.class);
        if (te != null) {
            // Special Interactions
            // 1. Try and put a log inside (happens on right click event when sneaking)
            // 2. Try and light the TE
            // 3. Open the GUI
            ItemStack stack = player.getHeldItem(hand);
            if (!state.getValue(LIT) && side == EnumFacing.UP && world.getBlockState(pos.up()).getBlock().isReplaceable(world, pos) && ItemFireStarter.onIgnition(stack)) {
                // Light the Pile
                if (!world.isRemote) {
                    world.setBlockState(pos, state.withProperty(LIT, true));
                    te.light();
                    world.setBlockState(pos.up(), Blocks.FIRE.getDefaultState());
                    world.playSound(null, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.PLAYERS, 1.0F, 1.0F);
                }
                return true;
            }
            if (OreDictionaryHelper.doesStackMatchOre(stack, "logWood")) {
                // Copy from InteractionManager since this is called first when player is not sneaking
                if (!player.isSneaking()) {
                    if (te.insertLog(stack.copy())) {
                        if (!world.isRemote) {
                            world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            stack.shrink(1);
                            player.setHeldItem(hand, stack);
                        }
                        return true;
                    }
                } else {
                    int inserted = te.insertLogs(stack.copy());
                    if (inserted > 0) {
                        if (!world.isRemote) {
                            world.playSound(null, pos, SoundEvents.BLOCK_WOOD_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                            stack.shrink(inserted);
                            player.setHeldItem(hand, stack);
                        }
                        return true;
                    }
                }
            }

            if (!player.isSneaking() && !state.getValue(LIT)) {
                if (!world.isRemote) {
                    TFCGuiHandler.openGui(world, pos, player, TFCGuiHandler.Type.LOG_PILE);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (placer.getHorizontalFacing().getAxis().isHorizontal()) {
            return getDefaultState().withProperty(AXIS, placer.getHorizontalFacing().getAxis());
        }
        return getDefaultState();
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack) {
        // This can't use breakBlock as it needs to not drop when broken in order to create a charcoal pile
        if (!worldIn.isRemote && te instanceof TEInventory) {
            ((TEInventory) te).onBreakBlock(worldIn, pos, state);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AXIS, LIT);
    }

    @Override
    public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 60;
    }

    @Override
    public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
        return 30;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TELogPile();
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.clear();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        TELogPile te = Helpers.getTE(world, pos, TELogPile.class);
        if (te != null) {
            return te.getLog().copy();
        }
        return ItemStack.EMPTY;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onModelRegister() {

        var resourceLocation = TerraFirmaCraft.getID(NAME.replaceAll("\\.", "/"));

        ModelLoader.setCustomStateMapper(this,
                new CustomStateMap.Builder().customPath(resourceLocation).build());

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(resourceLocation, "normal"));
    }

}
