package su.terrafirmagreg.modules.rock.object.block;

import su.terrafirmagreg.api.util.StackUtils;
import su.terrafirmagreg.modules.rock.ConfigRock;
import su.terrafirmagreg.modules.rock.api.types.type.RockType;
import su.terrafirmagreg.modules.rock.api.types.variant.block.RockBlockVariant;
import su.terrafirmagreg.modules.rock.init.BlocksRock;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import gregtech.common.items.ToolItems;
import net.dries007.tfc.ConfigTFC;
import net.dries007.tfc.objects.Gem;
import net.dries007.tfc.objects.items.ItemGem;

import java.util.Random;

import static su.terrafirmagreg.api.data.Properties.BoolProp.CAN_FALL;
import static su.terrafirmagreg.api.data.Properties.BoolProp.MOSSY;

@SuppressWarnings("deprecation")
public class BlockRockRaw extends BlockRock {


  /* This is for the not-surrounded-on-all-sides-pop-off mechanic. It's a dirty fix to the stack overflow caused by placement during water / lava collisions in world gen */

  public BlockRockRaw(RockBlockVariant variant, RockType type) {
    super(variant, type);

    getSettings()
      .ignoresProperties(CAN_FALL)
      .renderLayer(BlockRenderLayer.CUTOUT)
      .fallable(this, variant.getSpecification(), BlocksRock.COBBLE.get(type).getDefaultState())
      .oreDict(variant)
      .oreDict(variant, type)
      .oreDict("stone");

    setDefaultState(blockState.getBaseState()
                              .withProperty(CAN_FALL, true)
                              .withProperty(MOSSY, false));
  }


  @Override
  public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
    super.neighborChanged(state, worldIn, pos, blockIn, fromPos);

    // Raw blocks that can't fall also can't pop off
    if (state.getValue(CAN_FALL)) {
      for (EnumFacing face : EnumFacing.VALUES) {
        BlockPos offsetPos = pos.offset(face);
        IBlockState faceState = worldIn.getBlockState(offsetPos);
        if (faceState.getBlock().isSideSolid(faceState, worldIn, offsetPos, face.getOpposite())) {
          return;
        }
      }

      // No supporting solid blocks, so pop off as an item
      worldIn.setBlockToAir(pos);
      StackUtils.spawnItemStack(worldIn, pos, new ItemStack(state.getBlock(), 1));
    }
  }

  @Override
  public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

    ItemStack stack = playerIn.getHeldItemMainhand();
    if (ConfigRock.BLOCK.ANVIL.enableStoneAnvil && stack.getItem() == ToolItems.HARD_HAMMER.get() && !worldIn.isBlockNormalCube(pos.up(), true)) {
      if (!worldIn.isRemote) {

        // Create a stone anvil
        var anvil = BlocksRock.ANVIL.get(getType());
        if (anvil instanceof BlockRockAnvil) {
          worldIn.setBlockState(pos, anvil.getDefaultState());
        }
      }
      return true;
    }
    return false;
  }

  @Override
  protected BlockStateContainer createBlockState() {
    return new BlockStateContainer(this, CAN_FALL, MOSSY);
  }

  @Override
  public int quantityDropped(IBlockState state, int fortune, Random random) {
    return 1 + random.nextInt(3);
  }

  @Override
  public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    super.getDrops(drops, world, pos, state, fortune);

    // Raw rocks drop random gems
    if (RANDOM.nextDouble() < ConfigTFC.General.MISC.stoneGemDropChance) {
      drops.add(ItemGem.get(Gem.getRandomDropGem(RANDOM), Gem.Grade.randomGrade(RANDOM), 1));
    }

    // TODO
    //		if (RANDOM.nextDouble() < ModuleRockConfig.MISC.stoneGemDropChance) {
    //			drops.add(GemsFromRawRocks.getRandomGem());
    //		}
  }

}
