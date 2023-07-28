package net.dries007.tfc.objects.blocks.rock;

import net.dries007.tfc.api.types2.rock.RockBlockType;
import net.dries007.tfc.api.types2.rock.RockType;
import net.dries007.tfc.api.types2.rock.RockVariant;
import net.dries007.tfc.api.util.IRockTypeBlock;
import net.dries007.tfc.api.util.Triple;
import net.dries007.tfc.objects.CreativeTabsTFC;
import net.dries007.tfc.util.OreDictionaryHelper;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.dries007.tfc.TerraFirmaCraft.MOD_ID;
import static net.dries007.tfc.api.types2.rock.RockBlockType.ORDINARY;
import static net.dries007.tfc.objects.blocks.rock.BlockRockVatiant.BLOCK_ROCK_MAP;
import static net.dries007.tfc.objects.blocks.rock.BlockRockVatiant.getBlockRockMap;

public class BlockRockStair extends BlockStairs implements IRockTypeBlock {
	private final RockVariant rockVariant;
	private final RockType rockType;
	private final ResourceLocation modelLocation;

	public BlockRockStair(RockBlockType rockBlockType, RockVariant rockVariant, RockType rockType) {
		super(getBlockRockMap(ORDINARY, rockVariant, rockType).getDefaultState());

		if (BLOCK_ROCK_MAP.put(new Triple<>(rockBlockType, rockVariant, rockType), this) != null)
			throw new RuntimeException("Duplicate registry entry detected for block: " + rockVariant + " " + rockType);

		this.rockVariant = rockVariant;
		this.rockType = rockType;
		this.modelLocation = new ResourceLocation(MOD_ID, "rock/" + rockBlockType + "/" + rockVariant);
		useNeighborBrightness = true;

		String blockRegistryName = String.format("rock/%s/%s/%s", rockBlockType, rockVariant, rockType);

		this.setCreativeTab(CreativeTabsTFC.ROCK_STUFFS);
		this.setSoundType(SoundType.STONE);
		this.setHardness(getFinalHardness());
		this.setResistance(rockVariant.getResistance());
		this.setHarvestLevel("pickaxe", rockVariant.getHarvestLevel());
		this.setRegistryName(MOD_ID, blockRegistryName);
		this.setTranslationKey(MOD_ID + "." + blockRegistryName.toLowerCase().replace("/", "."));

		OreDictionaryHelper.register(this, "stair", "stair_" + rockType);
		//OreDictionaryModule.register(this, rockBlockType.getName(), rockVariant.getName(), rockVariant.getName() + WordUtils.capitalize(rockType.getName()));
	}

	@Override
	public RockVariant getRockVariant() {
		return rockVariant;
	}

	@Override
	public RockType getRockType() {
		return rockType;
	}

	@Override
	public ItemBlock getItemBlock() {
		return new ItemBlock(this);
	}

//	@Override
//	public boolean removedByPlayer(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull EntityPlayer player, boolean willHarvest) {
//		if (!world.isRemote) {
//			ItemStack heldItemStack = player.getHeldItem(EnumHand.MAIN_HAND);
//			Item heldItem = heldItemStack.getItem();
//
//			// Проверяем, можно ли игроку собрать блок с использованием текущего инструмента
//			if (player.canHarvestBlock(state)) {
//				// Проверяем, является ли удерживаемый предмет инструментом с классом инструмента pickaxe и кроме инструмента HARD_HAMMER
//				if ((heldItem.getToolClasses(heldItemStack).contains("pickaxe")) && !(heldItem == HARD_HAMMER.get())) {
//					switch (rockVariant) {
//						case RAW:
//						case SMOOTH:
//						case COBBLE:
//							//Block.spawnAsEntity(world, pos, new ItemStack(StoneTypeItems.ITEM_STONE_MAP.get(LOOSE.getName() + "/" + rockType.getName()), new Random().nextInt(2) + 3));
//							break;
//						case BRICK:
//							//Block.spawnAsEntity(world, pos, new ItemStack(StoneTypeItems.ITEM_STONE_MAP.get(LOOSE.getName() + "/" + rockType.getName()), new Random().nextInt(2) + 3));
//							Block.spawnAsEntity(world, pos, new ItemStack(Items.CLAY_BALL, new Random().nextInt(2))); //TODO кусочек цемента?
//							break;
//					}
//				} else if (heldItem == HARD_HAMMER.get()) {
//					switch (rockVariant) {
//						case RAW:
//						case SMOOTH:
//							Block.spawnAsEntity(world, pos, new ItemStack(getBlockRockMap(ORD, COBBLE, rockType), 1));
//							break;
//						case COBBLE:
//							//Block.spawnAsEntity(world, pos, new ItemStack(StoneTypeItems.ITEM_STONE_MAP.get(LOOSE.getName() + "/" + rockType.getName()), new Random().nextInt(2) + 3));
//							break;
//						case BRICK:
//							//Block.spawnAsEntity(world, pos, new ItemStack(StoneTypeItems.ITEM_STONE_MAP.get("brick/" + rockType.getName()), new Random().nextInt(2) + 3));
//							Block.spawnAsEntity(world, pos, new ItemStack(Items.CLAY_BALL, new Random().nextInt(2))); //TODO кусочек цемента?
//							break;
//					}
//				}
//			}
//		}
//		return super.removedByPlayer(state, world, pos, player, willHarvest);
//	}

	@Override
	public void getDrops(@Nonnull NonNullList<ItemStack> drops, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune) {
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onModelRegister() {
		ModelLoader.setCustomStateMapper(this, new DefaultStateMapper() {
			@Nonnull
			protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
				return new ModelResourceLocation(modelLocation,
						"facing=" + state.getValue(FACING) + "," +
								"half=" + state.getValue(HALF) + "," +
								"rocktype=" + rockType.getName() + "," +
								"shape=" + state.getValue(SHAPE));
			}
		});

		for (IBlockState state : this.getBlockState().getValidStates()) {
			ModelLoader.setCustomModelResourceLocation(
					Item.getItemFromBlock(this),
					this.getMetaFromState(state),
					new ModelResourceLocation(modelLocation,
							"facing=east," +
									"half=bottom," +
									"rocktype=" + rockType.getName() + "," +
									"shape=straight"));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);

		tooltip.add(new TextComponentTranslation("stonecategory.name").getFormattedText() + ": " + rockType.getRockCategory().getLocalizedName());
	}
}

