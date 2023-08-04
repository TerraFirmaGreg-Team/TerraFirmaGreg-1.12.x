package net.dries007.tfc.objects.blocks.soil;

import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.api.registries.TFCStorage;
import net.dries007.tfc.api.types.soil.Soil;
import net.dries007.tfc.api.types.soil.SoilVariant;
import net.dries007.tfc.api.types.soil.util.ISoilBlock;
import net.dries007.tfc.api.util.FallingBlockManager;
import net.dries007.tfc.objects.CreativeTabsTFC;
import net.dries007.tfc.objects.items.itemblock.ItemBlockTFC;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockGrassPath;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

import static net.dries007.tfc.TerraFirmaCraft.MOD_ID;
import static net.dries007.tfc.api.types.soil.SoilVariant.DIRT;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockSoilPath extends BlockGrassPath implements ISoilBlock {
    private static final AxisAlignedBB GRASS_PATH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9375D, 1.0D);

    private final SoilVariant soilVariant;
    private final Soil soil;
    private final ResourceLocation modelLocation;

    public BlockSoilPath(SoilVariant soilVariant, Soil soil) {


        this.soilVariant = soilVariant;
        this.soil = soil;
        this.modelLocation = new ResourceLocation(MOD_ID, "soil/" + soilVariant);
        this.useNeighborBrightness = true;

        var blockRegistryName = String.format("soil/%s/%s", soilVariant, soil);
        setRegistryName(MOD_ID, blockRegistryName);
        setTranslationKey(MOD_ID + "." + blockRegistryName.toLowerCase().replace("/", "."));
        setCreativeTab(CreativeTabsTFC.EARTH);
        setSoundType(SoundType.PLANT);
        setHardness(2.0F);
        setHarvestLevel("shovel", 0);
        setLightOpacity(255);
    }

    @Override
    public SoilVariant getSoilVariant() {
        return soilVariant;
    }

    @Override
    public Soil getSoil() {
        return soil;
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockTFC(this);
    }

    @Override
    @SuppressWarnings("deprecation")
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return GRASS_PATH_AABB;
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        if (fromPos.getY() == pos.getY() + 1) {
            IBlockState up = world.getBlockState(fromPos);
            if (up.isSideSolid(world, fromPos, EnumFacing.DOWN) && FallingBlockManager.getSpecification(up) == null) {
                BlockSoilFarmland.turnToDirt(world, pos);
            }
        }
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        BlockPos upPos = pos.up();
        IBlockState up = world.getBlockState(upPos);
        if (up.isSideSolid(world, upPos, EnumFacing.DOWN) && FallingBlockManager.getSpecification(up) == null) {
            BlockSoilFarmland.turnToDirt(world, pos);
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(TFCStorage.getSoilBlock(DIRT, soil));
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("deprecation")
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
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
    @SideOnly(Side.CLIENT)
    public void onModelRegister() {
        ModelLoader.setCustomStateMapper(this, new DefaultStateMapper() {
            @Nonnull
            protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
                return new ModelResourceLocation(modelLocation, "soiltype=" + soil.getName());
            }
        });


        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(this),
                this.getMetaFromState(this.getBlockState().getBaseState()),
                new ModelResourceLocation(modelLocation, "soiltype=" + soil.getName()));
    }
}
