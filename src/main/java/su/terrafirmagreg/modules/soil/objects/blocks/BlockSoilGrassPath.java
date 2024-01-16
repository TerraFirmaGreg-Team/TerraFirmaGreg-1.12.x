package su.terrafirmagreg.modules.soil.objects.blocks;

import net.dries007.tfc.api.util.FallingBlockManager;
import net.dries007.tfc.util.OreDictionaryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockGrassPath;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;
import su.terrafirmagreg.api.objects.itemblocks.ItemBlockBase;
import su.terrafirmagreg.modules.soil.StorageSoil;
import su.terrafirmagreg.modules.soil.api.types.type.SoilType;
import su.terrafirmagreg.modules.soil.api.types.variant.block.ISoilBlock;
import su.terrafirmagreg.modules.soil.api.types.variant.block.SoilBlockVariant;
import su.terrafirmagreg.modules.soil.api.types.variant.item.SoilItemVariants;

import java.util.Random;

public class BlockSoilGrassPath extends BlockGrassPath implements ISoilBlock {

    private static final AxisAlignedBB GRASS_PATH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);

    private final SoilBlockVariant variant;
    private final SoilType type;

    public BlockSoilGrassPath(SoilBlockVariant variant, SoilType type) {


        this.variant = variant;
        this.type = type;
        this.useNeighborBrightness = true;

        setSoundType(SoundType.PLANT);
        setHardness(2.0F);
        setHarvestLevel("shovel", 0);
        setLightOpacity(255);

        OreDictionaryHelper.register(this, variant.toString(), type.toString());
    }

    @NotNull
    @Override
    public SoilBlockVariant getBlockVariant() {
        return variant;
    }

    @NotNull
    @Override
    public SoilType getType() {
        return type;
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockBase(this);
    }

    @NotNull
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @NotNull
    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(@NotNull IBlockState state, @NotNull IBlockAccess source, @NotNull BlockPos pos) {
        return GRASS_PATH_AABB;
    }

    @Override
    public void neighborChanged(@NotNull IBlockState state, @NotNull World world, BlockPos pos, @NotNull Block blockIn, BlockPos fromPos) {
        if (fromPos.getY() == pos.getY() + 1) {
            IBlockState up = world.getBlockState(fromPos);
            if (up.isSideSolid(world, fromPos, EnumFacing.DOWN) && FallingBlockManager.getSpecification(up) == null) {
                BlockSoilFarmland.turnToDirt(world, pos);
            }
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, @NotNull IBlockState state) {
        BlockPos upPos = pos.up();
        IBlockState up = world.getBlockState(upPos);
        if (up.isSideSolid(world, upPos, EnumFacing.DOWN) && FallingBlockManager.getSpecification(up) == null) {
            BlockSoilFarmland.turnToDirt(world, pos);
        }
    }

    @NotNull
    @Override
    public Item getItemDropped(@NotNull IBlockState state, @NotNull Random rand, int fortune) {
        return StorageSoil.getItem(SoilItemVariants.PILE, getType());
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("deprecation")
    public boolean shouldSideBeRendered(@NotNull IBlockState blockState, @NotNull IBlockAccess blockAccess, @NotNull BlockPos pos, EnumFacing side) {
        switch (side) {
            case UP:
                return true;
            case NORTH:
            case SOUTH:
            case WEST:
            case EAST:
                IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
                Block block = iblockstate.getBlock();
                if (iblockstate.isOpaqueCube()) return false;
                if (block instanceof BlockFarmland || block instanceof BlockGrassPath) return false;
            default:
                return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
        }
    }

    @Override
    public void registerMeshModels() {
        ModelLoader.setCustomStateMapper(this, new DefaultStateMapper() {
            @NotNull
            protected ModelResourceLocation getModelResourceLocation(@NotNull IBlockState state) {
                return new ModelResourceLocation(getResourceLocation(), "soil_type=" + getType());
            }
        });


        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getResourceLocation(), "soil_type=" + getType()));
    }
}
