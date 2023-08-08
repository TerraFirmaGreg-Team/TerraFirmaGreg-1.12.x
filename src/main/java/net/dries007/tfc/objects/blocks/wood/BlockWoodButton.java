package net.dries007.tfc.objects.blocks.wood;

import net.dries007.tfc.api.types.wood.IWoodBlock;
import net.dries007.tfc.api.types.wood.type.WoodType;
import net.dries007.tfc.api.types.wood.variant.WoodVariant_old;
import net.dries007.tfc.client.CustomStateMap;
import net.dries007.tfc.objects.CreativeTabsTFC;
import net.dries007.tfc.objects.items.itemblock.ItemBlockTFC;
import net.minecraft.block.BlockButtonWood;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import static net.dries007.tfc.TerraFirmaCraft.MOD_ID;

public class BlockWoodButton extends BlockButtonWood implements IWoodBlock {
    private final WoodVariant_old woodVariant;
    private final WoodType woodType;
    private final ResourceLocation modelLocation;

    public BlockWoodButton(WoodVariant_old woodVariant, WoodType woodType) {

        this.woodVariant = woodVariant;
        this.woodType = woodType;
        this.modelLocation = new ResourceLocation(MOD_ID, "wood/" + woodVariant);

        var blockRegistryName = String.format("wood/%s/%s", woodVariant, woodType);
        setRegistryName(MOD_ID, blockRegistryName);
        setTranslationKey(MOD_ID + "." + blockRegistryName.toLowerCase().replace("/", "."));
        setCreativeTab(CreativeTabsTFC.WOOD);
        setHardness(0.5F);
        setSoundType(SoundType.WOOD);
        Blocks.FIRE.setFireInfo(this, 5, 20);
    }

    @Override
    public WoodVariant_old getWoodVariant() {
        return woodVariant;
    }

    @Override
    public WoodType getWood() {
        return woodType;
    }

    @Nullable
    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockTFC(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onModelRegister() {
        ModelLoader.setCustomStateMapper(this, new CustomStateMap.Builder().customPath(modelLocation).build());


        for (IBlockState state : this.getBlockState().getValidStates()) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this),
                    this.getMetaFromState(state),
                    new ModelResourceLocation(modelLocation, "inventory"));
        }
    }
}
