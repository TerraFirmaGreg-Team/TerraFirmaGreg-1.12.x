package su.terrafirmagreg.modules.device.object.block;

import su.terrafirmagreg.modules.device.object.tile.TileIceBunker;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.dries007.sharkbark.cellars.Main;
import net.dries007.sharkbark.cellars.init.ModBlocks;
import net.dries007.sharkbark.cellars.init.ModItems;
import net.dries007.sharkbark.cellars.util.IHasModel;
import net.dries007.sharkbark.cellars.util.Reference;
import net.dries007.tfc.objects.CreativeTabsTFC;

import javax.annotation.Nullable;

public class BlockIceBunker extends BlockContainer implements IHasModel {

  public BlockIceBunker(String name, Material material) {
    super(Material.WOOD);
    setTranslationKey(name);
    setRegistryName(name);
    setCreativeTab(CreativeTabsTFC.CT_MISC);
    setHardness(2F);
    ModBlocks.BLOCKS.add(this);
    ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(name));
  }

  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing playerFacing, float hitX, float hitY, float hitZ) {

    if (!worldIn.isRemote) {
      player.openGui(Main.INSTANCE, Reference.GUI_ICE_BUNKER, worldIn, pos.getX(), pos.getY(), pos.getZ());
    }

    return true;

  }

  @Override
  public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
    TileIceBunker tile = (TileIceBunker) worldIn.getTileEntity(pos);
    InventoryHelper.dropInventoryItems(worldIn, pos, tile);
    super.breakBlock(worldIn, pos, state);
  }

  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if (stack.hasDisplayName()) {
      TileEntity entity = worldIn.getTileEntity(pos);

      if (entity instanceof TileIceBunker) {
        //((TECellarShelf)entity).setCustomName(stack.getDisplayName());
      }
    }

  }

  @Nullable
  @Override
  public TileEntity createNewTileEntity(World world, int i) {
    return new TileIceBunker();
  }

  @Override
  public EnumBlockRenderType getRenderType(IBlockState state) {
    return EnumBlockRenderType.MODEL;
  }

  @Override
  public void registerModels() {
    Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
  }
}
