package net.dries007.tfc.objects.blocks.rock;

import net.dries007.tfc.api.types.rock.IRockBlock;
import net.dries007.tfc.api.types.rock.block.type.RockType;
import net.dries007.tfc.api.types.rock.block.variant.RockVariant;
import net.dries007.tfc.api.types.rock.type.Rock;
import net.dries007.tfc.objects.CreativeTabsTFC;
import net.dries007.tfc.objects.items.itemblock.ItemBlockTFC;
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
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.dries007.tfc.TerraFirmaCraft.MOD_ID;

public class BlockRockPressurePlate extends BlockPressurePlate implements IRockBlock {

    private final RockType rockType;
    private final RockVariant rockVariant;
    private final Rock rock;

    public BlockRockPressurePlate(RockType rockType, RockVariant rockVariant, Rock rock) {
        super(Material.ROCK, Sensitivity.MOBS);

        this.rockType = rockType;
        this.rockVariant = rockVariant;
        this.rock = rock;

        this.setCreativeTab(CreativeTabsTFC.ROCK);
        this.setSoundType(SoundType.STONE);
        this.setHardness(0.5f);
        this.setRegistryName(MOD_ID, getRegistryString());
        this.setTranslationKey(getTranslationString());
    }

    @Nonnull
    @Override
    public RockType getRockType() {
        return rockType;
    }

    @Nullable
    @Override
    public RockVariant getRockVariant() {
        return rockVariant;
    }

    @Nonnull
    @Override
    public Rock getRock() {
        return rock;
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockTFC(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onModelRegister() {
        ModelLoader.setCustomStateMapper(this, new DefaultStateMapper() {
            @Nonnull
            protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
                return new ModelResourceLocation(getResourceLocation(),
                        "powered=" + state.getValue(POWERED) + "," +
                                "rocktype=" + rock.toString());
            }
        });

        for (IBlockState state : this.getBlockState().getValidStates()) {
            ModelLoader.setCustomModelResourceLocation(
                    Item.getItemFromBlock(this),
                    this.getMetaFromState(state),
                    new ModelResourceLocation(getResourceLocation(), "inventory=" + rock.toString()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(new TextComponentTranslation("stonecategory.name").getFormattedText() + ": " + rock.getRockCategory().getLocalizedName());
    }
}
