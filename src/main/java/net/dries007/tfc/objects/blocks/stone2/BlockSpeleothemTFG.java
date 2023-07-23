package net.dries007.tfc.objects.blocks.stone2;

import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.api.types2.BlockType;
import net.dries007.tfc.api.types2.BlockVariant;
import net.dries007.tfc.api.util.IStoneTypeBlock;
import net.dries007.tfc.api.types2.StoneType;
import net.dries007.tfc.api.util.Triple;
import net.dries007.tfc.objects.CreativeTabsTFC;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

import static net.dries007.tfc.TerraFirmaCraft.MOD_ID;
import static net.dries007.tfc.objects.blocks.stone2.BlockOrdinaryTFG.BLOCK_MAP;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockSpeleothemTFG extends Block implements IStoneTypeBlock {
	public static PropertyEnum<EnumSize> SIZE = PropertyEnum.create("size", EnumSize.class);

	private final BlockVariant blockVariant;
	private final StoneType stoneType;
	private final ResourceLocation modelLocation;

	public BlockSpeleothemTFG(BlockType blockType, BlockVariant blockVariant, StoneType stoneType) {
		super(Material.ROCK);

		if (BLOCK_MAP.put(new Triple<>(blockType, blockVariant, stoneType), this) != null)
			throw new RuntimeException("Duplicate registry entry detected for block: " + blockVariant + " " + stoneType);

		this.blockVariant = blockVariant;
		this.stoneType = stoneType;
		this.modelLocation = new ResourceLocation(MOD_ID, blockType + "/" + blockVariant);

		String blockRegistryName = String.format("%s/%s/%s", blockType, blockVariant, stoneType);
		this.setCreativeTab(CreativeTabsTFC.CT_ROCK_BLOCKS);
		this.setSoundType(SoundType.STONE);
		this.setHardness(getFinalHardness());
		this.setResistance(stoneType.getResistance());
		this.setHarvestLevel("pickaxe", blockVariant.getHarvestLevel());
		this.setRegistryName(MOD_ID, blockRegistryName);
		this.setTranslationKey(MOD_ID + "." + blockRegistryName.toLowerCase().replace("/", "."));
		this.setDefaultState(blockState.getBaseState().withProperty(SIZE, EnumSize.MEDIUM));

		//OreDictionaryModule.register(this, blockType.getName(), blockVariant.getName(), blockVariant.getName() + WordUtils.capitalize(stoneType.getName()));
	}

	@Override
	public BlockVariant getBlockVariant() {
		return blockVariant;
	}

	@Override
	public StoneType getStoneType() {
		return stoneType;
	}

	@Override
	public ItemBlock getItemBlock() {
		ItemBlock itemBlock = new ItemBlock(this);
		//noinspection ConstantConditions
		itemBlock.setRegistryName(this.getRegistryName());
		return itemBlock;
	}

	@Override
	public boolean canPlaceBlockAt(World worldIn, @Nonnull BlockPos pos) {
		return getBearing(worldIn, pos) > 0;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		EnumSize size = EnumSize.values()[Math.max(0, getBearing(worldIn, pos) - 1)];
		worldIn.setBlockState(pos, state.withProperty(SIZE, size));
	}

	@Override
	@SuppressWarnings("deprecation")
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		int size = state.getValue(SIZE).strength;
		if (getBearing(worldIn, pos) < size + 1) {
			worldIn.playEvent(2001, pos, Block.getStateId(worldIn.getBlockState(pos)));
			dropBlockAsItem(worldIn, pos, state, 0);
			worldIn.setBlockToAir(pos);
		}
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}


	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, @Nonnull IBlockState state, EntityPlayer player) {
		return true;
	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return false;
	}

	private int getBearing(IBlockAccess world, BlockPos pos) {
		return Math.max(getStrength(world, pos.down()), getStrength(world, pos.up()));
	}


	private int getStrength(IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		if (state.isFullBlock())
			return 3;

		if (state.getPropertyKeys().contains(SIZE))
			return state.getValue(SIZE).strength;

		return 0;
	}

	@Nonnull
	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return state.getValue(SIZE).aabb;
	}


	@Override
	@SuppressWarnings("deprecation")
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
		return getBoundingBox(blockState, worldIn, pos);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Nonnull
	@Override
	@SuppressWarnings("deprecation")
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos blockPos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean canPlaceTorchOnTop(@Nonnull IBlockState state, @Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
		return true;
	}

	@Nonnull
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, SIZE);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(SIZE).ordinal();
	}

	@Nonnull
	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(SIZE, EnumSize.values()[Math.min(EnumSize.values().length - 1, meta)]);
	}

	@Override
	@SuppressWarnings("deprecation")
	public IBlockState getActualState(final IBlockState state, final IBlockAccess worldIn, final BlockPos pos) {
		EnumSize size = EnumSize.values()[Math.max(0, getBearing(worldIn, pos) - 1)];
		if (isCenter(worldIn, pos))
			size = EnumSize.MEDIUM;
		return state.withProperty(SIZE, size);
	}

	private boolean isCenter(IBlockAccess world, BlockPos pos) {
		return isThis(world, pos.down()) && isThis(world, pos.up());
	}

	private boolean isThis(IBlockAccess world, BlockPos pos) {
		return world.getBlockState(pos).getBlock() instanceof BlockSpeleothemTFG;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onModelRegister() {
		ModelLoader.setCustomStateMapper(this, new DefaultStateMapper() {
			@Nonnull
			protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
				return new ModelResourceLocation(modelLocation,
						"size=" + state.getValue(SIZE) + "," +
								"stonetype=" + stoneType.getName());
			}
		});


		ModelLoader.setCustomModelResourceLocation(
				Item.getItemFromBlock(this),
				this.getMetaFromState(this.getBlockState().getBaseState()),
				new ModelResourceLocation(modelLocation, "size=medium,stonetype=" + stoneType.getName()));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);

		tooltip.add(new TextComponentTranslation("stonecategory.name").getFormattedText() + ": " + stoneType.getStoneCategory().getLocalizedName());
	}

	public enum EnumSize implements IStringSerializable {

		SMALL(0, 2),
		MEDIUM(1, 4),
		BIG(2, 8);

		public final int strength;
		public final AxisAlignedBB aabb;

		EnumSize(int strength, int width) {
			this.strength = strength;

			float pad = (((16 - width) / 2f) / 16F);
			aabb = new AxisAlignedBB(pad, 0F, pad, 1F - pad, 1F, 1F - pad);
		}

		@Override
		public String getName() {
			return name().toLowerCase();
		}
	}
}
