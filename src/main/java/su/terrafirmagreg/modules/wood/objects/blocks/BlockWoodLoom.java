package su.terrafirmagreg.modules.wood.objects.blocks;


import lombok.Getter;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.dries007.tfc.util.Helpers;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import su.terrafirmagreg.api.spi.itemblock.ItemBlockBase;
import su.terrafirmagreg.api.util.CustomStateMap;
import su.terrafirmagreg.modules.wood.api.types.type.WoodType;
import su.terrafirmagreg.modules.wood.api.types.variant.block.IWoodBlock;
import su.terrafirmagreg.modules.wood.api.types.variant.block.WoodBlockVariant;
import su.terrafirmagreg.modules.wood.objects.tiles.TEWoodLoom;

@Getter
public class BlockWoodLoom extends BlockContainer implements IWoodBlock {

    protected static final AxisAlignedBB LOOM_EAST_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.0625D, 0.5625D, 1.0D, 0.9375D);
    protected static final AxisAlignedBB LOOM_WEST_AABB = new AxisAlignedBB(0.4375D, 0.0D, 0.0625D, 0.875D, 1.0D, 0.9375D);
    protected static final AxisAlignedBB LOOM_SOUTH_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.125D, 0.9375D, 1.0D, 0.5625D);
    protected static final AxisAlignedBB LOOM_NORTH_AABB = new AxisAlignedBB(0.0625D, 0.0D, 0.4375D, 0.9375D, 1.0D, 0.875D);

    private final WoodBlockVariant blockVariant;
    private final WoodType type;

    public BlockWoodLoom(WoodBlockVariant blockVariant, WoodType type) {
        super(Material.WOOD, MapColor.AIR);

        this.blockVariant = blockVariant;
        this.type = type;

        setSoundType(SoundType.WOOD);
        setHarvestLevel("axe", 0);
        setHardness(0.5f);
        setResistance(3f);
        setDefaultState(this.blockState.getBaseState()
                .withProperty(BlockHorizontal.FACING, EnumFacing.NORTH));

        //OreDictionaryHelper.register(this, variant.toString(), type.toString());
    }

    @Nullable
    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockBase(this);
    }

    @NotNull
    @Override
    public Size getSize(@NotNull ItemStack stack) {
        return Size.LARGE;
    }

    @NotNull
    @Override
    public Weight getWeight(@NotNull ItemStack stack) {
        return Weight.VERY_HEAVY;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@NotNull World worldIn, int meta) {
        return new TEWoodLoom();
    }

    @Override
    @SuppressWarnings("deprecation")
    @NotNull
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(BlockHorizontal.FACING, EnumFacing.byHorizontalIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BlockHorizontal.FACING).getHorizontalIndex();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    @NotNull
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return switch (state.getValue(BlockHorizontal.FACING)) {
            default -> LOOM_NORTH_AABB;
            case SOUTH -> LOOM_SOUTH_AABB;
            case WEST -> LOOM_WEST_AABB;
            case EAST -> LOOM_EAST_AABB;
        };
    }

    @Override
    @SuppressWarnings("deprecation")
    @NotNull
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        var te = Helpers.getTE(worldIn, pos, TEWoodLoom.class);
        if (te != null) {
            return te.onRightClick(playerIn);
        }
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    @NotNull
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if (facing.getAxis() == EnumFacing.Axis.Y) {
            facing = placer.getHorizontalFacing().getOpposite();
        }
        return getDefaultState().withProperty(BlockHorizontal.FACING, facing);
    }

    @Override
    @NotNull
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BlockHorizontal.FACING);
    }

    @Override
    @SuppressWarnings("deprecation")
    @NotNull
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public void breakBlock(World worldIn, @NotNull BlockPos pos, @NotNull IBlockState state) {
        TEWoodLoom te = Helpers.getTE(worldIn, pos, TEWoodLoom.class);
        if (te != null) {
            te.onBreakBlock(worldIn, pos, state);
        }
        super.breakBlock(worldIn, pos, state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onModelRegister() {
        ModelLoader.setCustomStateMapper(this,
                new CustomStateMap.Builder()
                        .customPath(getResourceLocation())
                        .build());

        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getResourceLocation(), "normal"));
    }
}
