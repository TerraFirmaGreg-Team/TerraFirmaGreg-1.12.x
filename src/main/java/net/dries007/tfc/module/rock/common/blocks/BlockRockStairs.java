package net.dries007.tfc.module.rock.common.blocks;

import net.dries007.tfc.module.core.api.objects.block.itemblocks.ItemBlockBase;
import net.dries007.tfc.module.rock.api.types.type.RockType;
import net.dries007.tfc.module.rock.api.types.variant.block.IRockBlock;
import net.dries007.tfc.module.rock.api.types.variant.block.RockBlockVariant;
import net.dries007.tfc.util.OreDictionaryHelper;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Blocks;
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

public class BlockRockStairs extends BlockStairs implements IRockBlock {

    private final RockBlockVariant variant;
    private final RockType type;

    public BlockRockStairs(RockBlockVariant variant, RockType type) {
        super(Blocks.COBBLESTONE.getDefaultState());

        this.variant = variant;
        this.type = type;
        this.useNeighborBrightness = true;

        setSoundType(SoundType.STONE);
        setHardness(getFinalHardness());
        setHarvestLevel("pickaxe", 0);

        OreDictionaryHelper.register(this, variant.toString(), type.toString());
    }

    @Nonnull
    @Override
    public RockBlockVariant getBlockVariant() {
        return variant;
    }

    @Nonnull
    @Override
    public RockType getType() {
        return type;
    }

    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockBase(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onModelRegister() {
        ModelLoader.setCustomStateMapper(this, new DefaultStateMapper() {
            @Nonnull
            protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
                return new ModelResourceLocation(getResourceLocation(),
                        "facing=" + state.getValue(FACING) + "," +
                                "half=" + state.getValue(HALF) + "," +
                                "rocktype=" + getType() + "," +
                                "shape=" + state.getValue(SHAPE));
            }
        });

        ModelLoader.setCustomModelResourceLocation(
                Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(getResourceLocation(),
                        "facing=east," +
                                "half=bottom," +
                                "rocktype=" + getType() + "," +
                                "shape=straight"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);

        tooltip.add(new TextComponentTranslation("rockcategory.name").getFormattedText() + ": " + type.getCategory().getLocalizedName());
    }


}
