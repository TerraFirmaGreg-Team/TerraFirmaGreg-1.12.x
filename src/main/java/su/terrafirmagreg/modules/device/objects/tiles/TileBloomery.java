package su.terrafirmagreg.modules.device.objects.tiles;

import su.terrafirmagreg.api.base.tile.BaseTileTickableInventory;
import su.terrafirmagreg.api.util.NBTUtils;
import su.terrafirmagreg.api.util.TileUtils;
import su.terrafirmagreg.modules.core.feature.ambiental.modifiers.ModifierBase;
import su.terrafirmagreg.modules.core.feature.ambiental.modifiers.ModifierTile;
import su.terrafirmagreg.modules.core.feature.ambiental.provider.IAmbientalTileProvider;
import su.terrafirmagreg.modules.device.ConfigDevice;
import su.terrafirmagreg.modules.device.init.BlocksDevice;
import su.terrafirmagreg.modules.device.objects.blocks.BlockBloomery;
import su.terrafirmagreg.modules.device.objects.blocks.BlockCharcoalPile;
import su.terrafirmagreg.modules.device.objects.blocks.BlockMolten;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;


import com.google.common.collect.ImmutableList;
import net.dries007.tfc.api.recipes.BloomeryRecipe;
import net.dries007.tfc.util.calendar.Calendar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.minecraft.block.BlockHorizontal.FACING;
import static su.terrafirmagreg.data.Properties.LIT;

@SuppressWarnings("WeakerAccess")
public class TileBloomery extends BaseTileTickableInventory implements IAmbientalTileProvider {

  // Gets the internal block, should be charcoal pile/bloom
  private static final Vec3i OFFSET_INTERNAL = new Vec3i(1, 0, 0);
  // Gets the external block, the front of the facing to dump contents in world.
  private static final Vec3i OFFSET_EXTERNAL = new Vec3i(-1, 0, 0);
  protected final List<ItemStack> oreStacks = new ArrayList<>();
  protected final List<ItemStack> fuelStacks = new ArrayList<>();

  protected int maxFuel = 0, maxOre = 0; // Helper variables, not necessary to serialize
  protected long litTick; // Tick that started the process

  protected BlockPos internalBlock = null, externalBlock = null;
  protected BloomeryRecipe cachedRecipe = null;

  public TileBloomery() {
    super(0);
  }

  @Override
  public void readFromNBT(NBTTagCompound tag) {
    oreStacks.clear();
    NBTTagList ores = tag.getTagList("ores", Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < ores.tagCount(); i++) {
      oreStacks.add(new ItemStack(ores.getCompoundTagAt(i)));
    }

    fuelStacks.clear();
    NBTTagList fuels = tag.getTagList("fuels", Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < fuels.tagCount(); i++) {
      fuelStacks.add(new ItemStack(fuels.getCompoundTagAt(i)));
    }
    litTick = tag.getLong("litTick");
    super.readFromNBT(tag);
  }

  @Override
  public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound nbt) {
    NBTTagList ores = new NBTTagList();
    for (ItemStack stack : oreStacks) {
      ores.appendTag(stack.serializeNBT());
    }
    NBTUtils.setGenericNBTValue(nbt, "ores", ores);
    NBTTagList fuels = new NBTTagList();
    for (ItemStack stack : fuelStacks) {
      fuels.appendTag(stack.serializeNBT());
    }
    NBTUtils.setGenericNBTValue(nbt, "fuels", fuels);
    NBTUtils.setGenericNBTValue(nbt, "litTick", litTick);
    return super.writeToNBT(nbt);
  }

  @Override
  public void onBreakBlock(World worldIn, BlockPos pos, IBlockState state) {
    dumpItems();
    super.onBreakBlock(world, pos, state);
  }

  public ImmutableList<ItemStack> getFuelStacks() {
    return ImmutableList.copyOf(fuelStacks);
  }

  public ImmutableList<ItemStack> getOreStacks() {
    return ImmutableList.copyOf(oreStacks);
  }

  @Override
  public void update() {
    super.update();
    if (!world.isRemote && world.getTotalWorldTime() % 20 == 0) {
      IBlockState state = world.getBlockState(pos);
      if (state.getValue(LIT)) {
        if (this.getRemainingTicks() <= 0) {
          if (cachedRecipe == null && !oreStacks.isEmpty()) {
            cachedRecipe = BloomeryRecipe.get(oreStacks.get(0));
            if (cachedRecipe == null) {
              dumpItems();
            }
          }
          if (cachedRecipe != null) {
            world.setBlockState(getInternalBlock(), BlocksDevice.BLOOM.getDefaultState());
            var tile = TileUtils.getTile(world, getInternalBlock(), TileBloom.class);
            if (tile != null) {
              tile.setBloom(cachedRecipe.getOutput(oreStacks));
            }
          }

          oreStacks.clear();
          fuelStacks.clear();
          cachedRecipe = null; // Clear recipe

          updateSlagBlock(false);
          state = state.withProperty(LIT, false);
          world.setBlockState(pos, state);
          markDirty();
        }
      }

      // Update multiblock status
      int newMaxItems = BlockBloomery.getChimneyLevels(world, getInternalBlock()) * 8;
      EnumFacing direction = world.getBlockState(pos).getValue(FACING);
      if (!BlocksDevice.BLOOMERY.isFormed(world, getInternalBlock(), direction)) {
        newMaxItems = 0;
      }

      maxFuel = newMaxItems;
      maxOre = newMaxItems;
      boolean turnOff = false;
      while (maxOre < oreStacks.size()) {
        turnOff = true;
        // Structure lost one or more chimney levels
        InventoryHelper.spawnItemStack(world, getExternalBlock().getX(), getExternalBlock().getY(),
                getExternalBlock().getZ(),
                oreStacks.get(0));
        oreStacks.remove(0);
        markForSync();
      }
      while (maxFuel < fuelStacks.size()) {
        turnOff = true;
        InventoryHelper.spawnItemStack(world, getExternalBlock().getX(), getExternalBlock().getY(),
                getExternalBlock().getZ(),
                fuelStacks.get(0));
        fuelStacks.remove(0);
        markForSync();
      }
      // Structure became compromised, unlit if needed
      if (turnOff && state.getValue(LIT)) {
        state = state.withProperty(LIT, false);
        world.setBlockState(pos, state);
      }
      if (!BlocksDevice.BLOOMERY.canGateStayInPlace(world, pos, direction.getAxis())) {
        // Bloomery gate (the front facing) structure became compromised
        world.destroyBlock(pos, true);
        return;
      }
      if (!isInternalBlockComplete() && !fuelStacks.isEmpty()) {
        dumpItems();
      }

      if (isInternalBlockComplete()) {
        int oldFuel = fuelStacks.size();
        int oldOre = oreStacks.size();
        addItemsFromWorld();
        if (oldFuel != fuelStacks.size() || oldOre != oreStacks.size()) {
          markForSync();
        }
      }
      updateSlagBlock(state.getValue(LIT));
    }
  }

  @Override
  public void onLoad() {
    // This caches the bloomery block as otherwise it can be null when broken
    getExternalBlock();
  }

  public long getRemainingTicks() {
    return ConfigDevice.BLOCKS.BLOOMERY.ticks - (Calendar.PLAYER_TIME.getTicks() - litTick);
  }

  public boolean canIgnite() {
    if (world.isRemote) {
      return false;
    }
    if (this.fuelStacks.size() < this.oreStacks.size() || this.oreStacks.isEmpty()) {
      return false;
    }
    return isInternalBlockComplete();
  }

  public void onIgnite() {
    this.litTick = Calendar.PLAYER_TIME.getTicks();
  }

  /**
   * Gets the internal (charcoal pile / bloom) position
   *
   * @return BlockPos of the internal block
   */
  public BlockPos getInternalBlock() {
    if (internalBlock == null) {
      EnumFacing direction = world.getBlockState(pos).getValue(FACING);
      internalBlock = pos.up(OFFSET_INTERNAL.getY())
              .offset(direction, OFFSET_INTERNAL.getX())
              .offset(direction.rotateY(), OFFSET_INTERNAL.getZ());
    }
    return internalBlock;
  }

  /**
   * Gets the external (front facing) position
   *
   * @return BlockPos to dump items in world
   */
  public BlockPos getExternalBlock() {
    if (externalBlock == null) {
      EnumFacing direction = world.getBlockState(pos).getValue(FACING);
      externalBlock = pos.up(OFFSET_EXTERNAL.getY())
              .offset(direction, OFFSET_EXTERNAL.getX())
              .offset(direction.rotateY(), OFFSET_EXTERNAL.getZ());
    }
    return externalBlock;
  }

  protected void dumpItems() {
    //Dump everything in world
    for (int i = 1; i < 4; i++) {
      if (world.getBlockState(getInternalBlock().up(i)).getBlock() == BlocksDevice.MOLTEN) {
        world.setBlockToAir(getInternalBlock().up(i));
      }
    }
    oreStacks.forEach(
            i -> InventoryHelper.spawnItemStack(world, getExternalBlock().getX(),
                    getExternalBlock().getY(), getExternalBlock().getZ(), i));
    fuelStacks.forEach(
            i -> InventoryHelper.spawnItemStack(world, getExternalBlock().getX(),
                    getExternalBlock().getY(), getExternalBlock().getZ(), i));
  }

  protected boolean isInternalBlockComplete() {
    IBlockState inside = world.getBlockState(getInternalBlock());
    return inside.getBlock() == BlocksDevice.CHARCOAL_PILE
            && inside.getValue(BlockCharcoalPile.LAYERS) >= 8;
  }

  protected void addItemsFromWorld() {
    if (cachedRecipe == null && !oreStacks.isEmpty()) {
      cachedRecipe = BloomeryRecipe.get(oreStacks.get(0));
      if (cachedRecipe == null) {
        this.dumpItems();
      }
    }
    for (EntityItem entityItem : world.getEntitiesWithinAABB(EntityItem.class,
            new AxisAlignedBB(getInternalBlock().up(), getInternalBlock().add(1, 4, 1)),
            EntitySelectors.IS_ALIVE)) {
      ItemStack stack = entityItem.getItem();
      if (cachedRecipe == null) {
        cachedRecipe = BloomeryRecipe.get(stack);
      }
      if (cachedRecipe != null) {
        if (cachedRecipe.isValidInput(stack)) {
          if (oreStacks.size() < maxOre) {
            markDirty();
          }
          while (oreStacks.size() < maxOre) {
            oreStacks.add(stack.splitStack(1));
            if (stack.getCount() <= 0) {
              entityItem.setDead();
              break;
            }
          }
        } else if (cachedRecipe.isValidAdditive(stack)) {
          if (fuelStacks.size() < maxFuel) {
            markDirty();
          }
          while (fuelStacks.size() < maxFuel) {
            fuelStacks.add(stack.splitStack(1));
            if (stack.getCount() <= 0) {
              entityItem.setDead();
              break;
            }
          }
        }
      }
    }
  }

  protected void updateSlagBlock(boolean cooking) {
    int slag = fuelStacks.size() + oreStacks.size();
    //If there's at least one item, show one layer so player knows that it is holding stacks
    int slagLayers = slag > 0 && slag < 4 ? 1 : slag / 4;
    for (int i = 1; i < 4; i++) {
      if (slagLayers > 0) {
        if (slagLayers >= 4) {
          slagLayers -= 4;
          world.setBlockState(getInternalBlock().up(i), BlocksDevice.MOLTEN.getDefaultState()
                  .withProperty(LIT, cooking)
                  .withProperty(BlockMolten.LAYERS, 4));
        } else {
          world.setBlockState(getInternalBlock().up(i), BlocksDevice.MOLTEN.getDefaultState()
                  .withProperty(LIT, cooking)
                  .withProperty(BlockMolten.LAYERS, slagLayers));
          slagLayers = 0;
        }
      } else {
        //Remove any surplus slag(ie: after cooking/structure became compromised)
        if (world.getBlockState(getInternalBlock().up(i)).getBlock() == BlocksDevice.MOLTEN) {
          world.setBlockToAir(getInternalBlock().up(i));
        }
      }
    }
  }

  @Override
  public Optional<ModifierBase> getModifier(EntityPlayer player, TileEntity tile) {
    float change = this.getRemainingTicks() > 0 ? 4f : 0f;
    float potency = change;
    if (ModifierTile.hasProtection(player)) {
      change = 1.0F;
    }
    return ModifierBase.defined(this.getBlockType().getRegistryName().getPath(), change, potency);
  }
}
