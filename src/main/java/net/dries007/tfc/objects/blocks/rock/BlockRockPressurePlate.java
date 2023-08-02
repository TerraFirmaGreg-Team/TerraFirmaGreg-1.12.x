package net.dries007.tfc.objects.blocks.rock;

import net.dries007.tfc.api.registries.TFCStorage;
import net.dries007.tfc.api.types2.rock.RockType;
import net.dries007.tfc.api.types2.rock.RockVariant;
import net.dries007.tfc.api.types2.rock.util.IRockBlock;
import net.dries007.tfc.objects.CreativeTabsTFC;
import net.dries007.tfc.util.OreDictionaryHelper;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.dries007.tfc.TerraFirmaCraft.MOD_ID;
import static net.dries007.tfc.api.types2.rock.RockBlockType.ORDINARY;

public class BlockRockPressurePlate extends BlockPressurePlate implements IRockBlock {
	private final RockVariant rockVariant;
	private final RockType rockType;
	private final ResourceLocation modelLocation;

	public BlockRockPressurePlate(RockVariant rockVariant, RockType rockType) {
		super(Material.ROCK, Sensitivity.MOBS);

		TFCStorage.addRockBlock(ORDINARY, rockVariant, rockType, this);

		this.rockVariant = rockVariant;
		this.rockType = rockType;
		this.modelLocation = new ResourceLocation(MOD_ID, "rock/" + rockVariant);

		String blockRegistryName = String.format("rock/%s/%s", rockVariant, rockType);
		this.setCreativeTab(CreativeTabsTFC.ROCK_STUFFS);
		this.setSoundType(SoundType.STONE);
		this.setHardness(0.5f);
		this.setRegistryName(MOD_ID, blockRegistryName);
		this.setTranslationKey(MOD_ID + "." + blockRegistryName.toLowerCase().replace("/", "."));

		OreDictionaryHelper.register(this, "pressure_plate_stone");
		//OreDictionaryModule.register(this, rockBlockType.getName(), rockVariant.getName(), rockVariant.getName() + WordUtils.capitalize(rockType.getName()));
	}

	@Nonnull
	@Override
	public RockVariant getRockVariant() {
		return rockVariant;
	}

	@Nonnull
    @Override
	public RockType getRockType() {
		return rockType;
	}

	@Override
	public ItemBlock getItemBlock() {
		return new ItemBlock(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onModelRegister() {
		ModelLoader.setCustomStateMapper(this, new DefaultStateMapper() {
			@Nonnull
			protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
				return new ModelResourceLocation(modelLocation,
						"powered=" + state.getValue(POWERED) + "," +
								"rocktype=" + rockType.getName());
			}
		});

		for (IBlockState state : this.getBlockState().getValidStates()) {
			ModelLoader.setCustomModelResourceLocation(
					Item.getItemFromBlock(this),
					this.getMetaFromState(state),
					new ModelResourceLocation(modelLocation, "inventory=" + rockType.getName()));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);

		tooltip.add(new TextComponentTranslation("stonecategory.name").getFormattedText() + ": " + getRockType().getRockCategory().getLocalizedName());
	}
}
