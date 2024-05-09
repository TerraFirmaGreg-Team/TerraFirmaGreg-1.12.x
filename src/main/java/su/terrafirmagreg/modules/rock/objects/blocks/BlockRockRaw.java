package su.terrafirmagreg.modules.rock.objects.blocks;

import su.terrafirmagreg.api.model.CustomStateMap;
import su.terrafirmagreg.api.spi.block.IStateMapperProvider;
import su.terrafirmagreg.api.util.ModelUtils;
import su.terrafirmagreg.api.util.OreDictUtils;
import su.terrafirmagreg.api.util.StackUtils;
import su.terrafirmagreg.modules.rock.ModuleRockConfig;
import su.terrafirmagreg.modules.rock.api.types.type.RockType;
import su.terrafirmagreg.modules.rock.api.types.variant.block.RockBlockVariant;
import su.terrafirmagreg.modules.rock.api.types.variant.block.RockBlockVariants;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


import gregtech.common.items.ToolItems;
import net.dries007.tfc.api.util.FallingBlockManager;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static su.terrafirmagreg.api.data.Blockstates.CAN_FALL;

@SuppressWarnings("deprecation")
public class BlockRockRaw extends BlockRock implements IStateMapperProvider {


    /* This is for the not-surrounded-on-all-sides-pop-off mechanic. It's a dirty fix to the stack overflow caused by placement during water / lava collisions in world gen */

    public BlockRockRaw(RockBlockVariant variant, RockType type) {
        super(variant, type);

        setDefaultState(getBlockState().getBaseState()
                .withProperty(CAN_FALL, true));

        // Copy as each raw stone has an unique resultingState
        var spec = new FallingBlockManager.Specification(variant.getSpecification());
        spec.setResultingState(RockBlockVariants.COBBLE.get(type).getDefaultState());

        FallingBlockManager.registerFallable(this, spec);
    }

    @Override
    public void onRegisterOreDict() {
        OreDictUtils.register(this, getVariant());
        OreDictUtils.register(this, "stone");
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(CAN_FALL, meta == 0);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getBlock() != this) {
            return 0;
        } else {
            return state.getValue(CAN_FALL) ? 0 : 1;
        }
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
        if (ModuleRockConfig.BLOCKS.enableStoneAnvil && stack.getItem() == ToolItems.HARD_HAMMER.get() && !worldIn.isBlockNormalCube(pos.up(), true)) {
            if (!worldIn.isRemote) {
                // Create a stone anvil
                var anvil = RockBlockVariants.ANVIL.get(getType());
                if (anvil instanceof BlockRockAnvil) {
                    worldIn.setBlockState(pos, anvil.getDefaultState());
                }
            }
            return true;
        }
        return false;
    }

    @NotNull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, CAN_FALL);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        super.getDrops(drops, world, pos, state, fortune);

        // TODO
        //		if (RANDOM.nextDouble() < ModuleRockConfig.MISC.stoneGemDropChance) {
        //			drops.add(GemsFromRawRocks.getRandomGem());
        //		}
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return 1 + random.nextInt(3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onRegisterState() {
        ModelUtils.registerStateMapper(this, new CustomStateMap.Builder().ignore(CAN_FALL).build());
    }
}
