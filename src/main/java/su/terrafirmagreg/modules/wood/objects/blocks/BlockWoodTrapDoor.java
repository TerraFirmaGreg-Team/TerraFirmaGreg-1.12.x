package su.terrafirmagreg.modules.wood.objects.blocks;

import lombok.Getter;

import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.Nullable;
import su.terrafirmagreg.api.spi.block.IColorfulBlock;
import su.terrafirmagreg.api.spi.itemblock.ItemBlockBase;
import su.terrafirmagreg.api.util.CustomStateMap;
import su.terrafirmagreg.api.util.ModelRegistrationHelper;
import su.terrafirmagreg.modules.wood.api.types.type.WoodType;
import su.terrafirmagreg.modules.wood.api.types.variant.block.IWoodBlock;
import su.terrafirmagreg.modules.wood.api.types.variant.block.WoodBlockVariant;

@Getter
public class BlockWoodTrapDoor extends BlockTrapDoor implements IWoodBlock, IColorfulBlock {

    private final WoodBlockVariant blockVariant;
    private final WoodType type;

    public BlockWoodTrapDoor(WoodBlockVariant blockVariant, WoodType type) {
        super(Material.WOOD);
        this.blockVariant = blockVariant;
        this.type = type;

        setHardness(0.5F);
        setSoundType(SoundType.WOOD);

        Blocks.FIRE.setFireInfo(this, 5, 20);

        //OreDictionaryHelper.register(this, variant.toString());
        //OreDictionaryHelper.register(this, variant.toString(), type.toString());
    }

    @Nullable
    @Override
    public ItemBlock getItemBlock() {
        return new ItemBlockBase(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onModelRegister() {
        ModelRegistrationHelper.registerBlockModel(this, new CustomStateMap.Builder().customPath(getResourceLocation()).build());
        ModelRegistrationHelper.registerItemModel(Item.getItemFromBlock(this), getResourceLocation());
    }

    @Override
    public IBlockColor getColorHandler() {
        return (s, w, p, i) ->  this.getType().getColor();
    }

    @Override
    public IItemColor getItemColorHandler() {
        return (s, i) -> this.getType().getColor();
    }
}
