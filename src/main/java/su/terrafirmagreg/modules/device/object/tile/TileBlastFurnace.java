package su.terrafirmagreg.modules.device.object.tile;

import su.terrafirmagreg.api.base.object.tile.api.ITileFields;
import su.terrafirmagreg.api.base.object.tile.spi.BaseTileTickableInventory;
import su.terrafirmagreg.api.util.NBTUtils;
import su.terrafirmagreg.api.util.OreDictUtils;
import su.terrafirmagreg.api.util.StackUtils;
import su.terrafirmagreg.api.util.TileUtils;
import su.terrafirmagreg.framework.registry.api.provider.IProviderContainer;
import su.terrafirmagreg.modules.core.capabilities.heat.CapabilityHeat;
import su.terrafirmagreg.modules.core.capabilities.metal.CapabilityMetal;
import su.terrafirmagreg.modules.device.ConfigDevice;
import su.terrafirmagreg.modules.device.ModuleDevice;
import su.terrafirmagreg.modules.device.client.gui.GuiBlastFurnace;
import su.terrafirmagreg.modules.device.init.BlocksDevice;
import su.terrafirmagreg.modules.device.object.block.BlockBlastFurnace;
import su.terrafirmagreg.modules.device.object.container.ContainerBlastFurnace;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableList;
import net.dries007.tfc.api.recipes.BlastFurnaceRecipe;
import net.dries007.tfc.api.util.IHeatConsumerBlock;
import net.dries007.tfc.util.Alloy;
import net.dries007.tfc.util.fuel.Fuel;
import net.dries007.tfc.util.fuel.FuelManager;

import java.util.ArrayList;
import java.util.List;

import static su.terrafirmagreg.api.data.Properties.BoolProp.LIT;
import static su.terrafirmagreg.api.data.Properties.IntProp.LAYERS;

public class TileBlastFurnace extends BaseTileTickableInventory
  implements ITileFields, IProviderContainer<ContainerBlastFurnace, GuiBlastFurnace> {

  public static final int SLOT_TUYERE = 0;
  public static final int FIELD_TEMPERATURE = 0;
  public static final int FIELD_ORE = 1;
  public static final int FIELD_FUEL = 2;
  public static final int FIELD_MELT = 3;
  public static final int FIELD_ORE_UNITS = 4;
  public static final int CHIMNEY_LEVELS = 5;

  private static final int MAX_AIR_TICKS = ConfigDevice.BLOCK.BELLOWS.maxTicks;

  private final List<ItemStack> oreStacks = new ArrayList<>();
  private final List<ItemStack> fuelStacks = new ArrayList<>();
  private final Alloy alloy;
  private int maxFuel = 0, maxOre = 0, delayTimer = 0, meltAmount = 0, chimney = 0;
  private long burnTicksLeft = 0;
  private int airTicks = 0;
  private int fuelCount = 0, oreCount = 0, oreUnits; // Used to show on client's GUI how much ore/fuel TE has
  private float temperature = 0;
  private float burnTemperature = 0;

  public TileBlastFurnace() {
    super(1);
    // Blast furnaces hold the same amount of crucibles, should it matter to be different?
    this.alloy = new Alloy(ConfigDevice.BLOCK.CRUCIBLE.tank);
  }

  @Override
  public int getSlotLimit(int slot) {
    return 1;
  }

  @Override
  public boolean isItemValid(int slot, ItemStack stack) {
    return OreDictUtils.contains(stack, "tuyere");
  }

  @SuppressWarnings("unused")
  public long getBurnTicksLeft() {
    return burnTicksLeft;
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    oreStacks.clear();
    NBTTagList ores = nbt.getTagList("ores", Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < ores.tagCount(); i++) {
      oreStacks.add(new ItemStack(ores.getCompoundTagAt(i)));
    }

    fuelStacks.clear();
    NBTTagList fuels = nbt.getTagList("fuels", Constants.NBT.TAG_COMPOUND);
    for (int i = 0; i < fuels.tagCount(); i++) {
      fuelStacks.add(new ItemStack(fuels.getCompoundTagAt(i)));
    }
    burnTicksLeft = nbt.getLong("burnTicksLeft");
    airTicks = nbt.getInteger("airTicks");
    burnTemperature = nbt.getFloat("burnTemperature");
    temperature = nbt.getFloat("temperature");
    alloy.deserializeNBT(nbt.getCompoundTag("alloy"));
    super.readFromNBT(nbt);
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
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
    NBTUtils.setGenericNBTValue(nbt, "burnTicksLeft", burnTicksLeft);
    NBTUtils.setGenericNBTValue(nbt, "airTicks", airTicks);
    NBTUtils.setGenericNBTValue(nbt, "burnTemperature", burnTemperature);
    NBTUtils.setGenericNBTValue(nbt, "temperature", temperature);
    NBTUtils.setGenericNBTValue(nbt, "alloy", alloy.serializeNBT());
    return super.writeToNBT(nbt);
  }

  @Override
  public void onBreakBlock(World worldIn, BlockPos pos, IBlockState state) {
    // Dump everything in world
    for (int i = 1; i < 6; i++) {
      if (world.getBlockState(pos.up(i)).getBlock() == BlocksDevice.MOLTEN.get()) {
        world.setBlockToAir(pos.up(i));
      }
    }
    for (ItemStack stack : oreStacks) {
      StackUtils.spawnItemStack(world, pos, stack);
    }
    for (ItemStack stack : fuelStacks) {
      StackUtils.spawnItemStack(world, pos, stack);
    }
    super.onBreakBlock(world, pos, state);
  }

  public ImmutableList<ItemStack> getFuelStacks() {
    return ImmutableList.copyOf(fuelStacks);
  }

  public ImmutableList<ItemStack> getOreStacks() {
    return ImmutableList.copyOf(oreStacks);
  }

  public boolean canIgnite() {
    if (!world.isRemote) {
      return !fuelStacks.isEmpty() && !oreStacks.isEmpty();
    }
    return false;
  }

  @Override
  public int getFieldCount() {
    return 6;
  }

  @Override
  public void setField(int index, int value) {
    switch (index) {
      case FIELD_TEMPERATURE:
        temperature = value;
        return;
      case FIELD_ORE:
        oreCount = value;
        return;
      case FIELD_FUEL:
        fuelCount = value;
        return;
      case FIELD_MELT:
        meltAmount = value;
        return;
      case FIELD_ORE_UNITS:
        oreUnits = value;
        return;
      case CHIMNEY_LEVELS:
        chimney = value;
        return;
    }
    ModuleDevice.LOGGER.warn("Illegal field id {} in TEBlastFurnace#setField", index);
  }

  @Override
  public int getField(int index) {
    switch (index) {
      case FIELD_TEMPERATURE:
        return (int) temperature;
      case FIELD_ORE:
        return oreCount;
      case FIELD_FUEL:
        return fuelCount;
      case FIELD_MELT:
        return meltAmount;
      case FIELD_ORE_UNITS:
        return oreUnits;
      case CHIMNEY_LEVELS:
        return chimney;
    }
    ModuleDevice.LOGGER.warn("Illegal field id {} in TEBlastFurnace#getField", index);
    return 0;
  }

  @Override
  public void update() {
    super.update();
    if (!world.isRemote) {
      IBlockState state = world.getBlockState(pos);
      if (state.getValue(LIT)) {
        // Update bellows air
        if (burnTicksLeft > 0) {
          // Double fuel consumption if using bellows
          burnTicksLeft -= airTicks > 0 ? 2 : 1;
        }

        if (burnTicksLeft <= 0) {
          if (!fuelStacks.isEmpty()) {
            ItemStack fuelStack = fuelStacks.get(0);
            fuelStacks.remove(0);
            Fuel fuel = FuelManager.getFuel(fuelStack);
            burnTicksLeft = (int) (Math.ceil(
              fuel.getAmount() / ConfigDevice.BLOCK.BLAST_FURNACE.consumption));
            burnTemperature = fuel.getTemperature();
          } else {
            burnTemperature = 0;
          }
          markForSync();
        }

        if (temperature > 0 || burnTemperature > 0) {
          temperature = CapabilityHeat.adjustToTargetTemperature(temperature, burnTemperature,
            airTicks, MAX_AIR_TICKS);
          // Provide heat to blocks that are one block bellow AKA crucible
          Block blockCrucible = world.getBlockState(pos.down()).getBlock();
          if (blockCrucible instanceof IHeatConsumerBlock heatConsumerBlock) {
            heatConsumerBlock.acceptHeat(world, pos.down(), temperature);
          }
          if (!world.isRemote) {
            oreStacks.removeIf(stack ->
            {
              var cap = CapabilityHeat.get(stack);
              if (cap != null) {
                // Update temperature of item
                float itemTemp = cap.getTemperature();
                if (temperature > itemTemp) {
                  CapabilityHeat.addTemp(cap);
                }
                if (cap.isMolten()) {
                  convertToMolten(stack);
                  ItemStack tuyereStack = inventory.getStackInSlot(0);
                  if (!tuyereStack.isEmpty()) {
                    StackUtils.damageItem(tuyereStack);
                  }
                  return true;
                }
              }
              return false;
            });
          }
          if (temperature <= 0 && burnTemperature <= 0) {
            temperature = 0;
            world.setBlockState(pos, state.withProperty(LIT, false));
          }
        }
      }

      // Update air ticks
      if (airTicks > 0) {
        airTicks--;
      } else {
        airTicks = 0;
      }

      meltAmount = alloy.getAmount(); //update for client GUI
      if (--delayTimer <= 0) {
        delayTimer = 20;
        // Update multiblock status

        // Detect client changes
        int oldChimney = chimney;
        int oldOre = oreCount;
        int oldFuel = fuelCount;

        chimney = BlockBlastFurnace.getChimneyLevels(world, pos);
        int newMaxItems = chimney * 4;
        maxFuel = newMaxItems;
        maxOre = newMaxItems;
        while (maxOre < oreStacks.size()) {
          //Structure lost one or more chimney levels
          StackUtils.spawnItemStack(world, pos, oreStacks.get(0));
          oreStacks.remove(0);
        }
        while (maxFuel < fuelStacks.size()) {
          StackUtils.spawnItemStack(world, pos, fuelStacks.get(0));
          fuelStacks.remove(0);
        }
        addItemsFromWorld();
        updateSlagBlock(state.getValue(LIT));

        oreCount = oreStacks.size();
        oreUnits = oreStacks.stream().mapToInt(stack -> {
          var metalObject = CapabilityMetal.get(stack);
          if (metalObject != null) {
            return metalObject.getSmeltAmount(stack);
          }
          return 1;
        }).sum();
        fuelCount = fuelStacks.size();

        if (oldChimney != chimney || oldOre != oreCount || oldFuel != fuelCount) {
          markForSync();
        }
      }
      if (alloy.removeAlloy(1, true) > 0) {
        // Move already molten liquid metal to the crucible.
        // This makes the effect of slowly filling up the crucible.
        // Take into account full or non-existent (removed) crucibles
        TileUtils.getTile(world, pos.down(), TileCrucible.class).ifPresent(tile -> {
          if (tile.addMetal(alloy.getResult(), 1) <= 0) {
            alloy.removeAlloy(1, false);
          }
        });
      }
    }
  }

  /**
   * Melts stacks
   */
  private void convertToMolten(ItemStack stack) {
    BlastFurnaceRecipe recipe = BlastFurnaceRecipe.get(stack);
    if (recipe != null) {
      FluidStack output = recipe.getOutput(stack);
      if (output != null) {
        alloy.add(output);
      }
    }
  }

  private void addItemsFromWorld() {
    EntityItem fluxEntity = null, oreEntity = null;
    List<EntityItem> items = world.getEntitiesWithinAABB(EntityItem.class,
      new AxisAlignedBB(pos.up(), pos.up().add(1, 5, 1)),
      EntitySelectors.IS_ALIVE);
    for (EntityItem entityItem : items) {
      ItemStack stack = entityItem.getItem();
      BlastFurnaceRecipe recipe = BlastFurnaceRecipe.get(stack);
      if (recipe != null) {
        oreEntity = entityItem;
        // Try searching for the additive (flux for pig iron)
        for (EntityItem item : items) {
          if (recipe.isValidAdditive(item.getItem())) {
            fluxEntity = item;
            break;
          }
        }
        if (fluxEntity != null) {
          // We have both additives + ores for the found recipe
          break;
        } else {
          // Didn't found the correct additive, abort adding the ore to the input
          oreEntity = null;
        }
      }
      if (FuelManager.isItemBloomeryFuel(stack)) {
        // Add fuel
        while (maxFuel > fuelStacks.size()) {
          markDirty();
          fuelStacks.add(stack.splitStack(1));
          if (stack.getCount() <= 0) {
            entityItem.setDead();
            break;
          }
        }
      }
    }

    // Add each ore consuming flux
    while (maxOre > oreStacks.size()) {
      if (fluxEntity == null || oreEntity == null) {
        break;
      }
      markDirty();

      ItemStack flux = fluxEntity.getItem();
      flux.shrink(1);

      ItemStack ore = oreEntity.getItem();
      oreStacks.add(ore.splitStack(1));

      if (flux.getCount() <= 0) {
        fluxEntity.setDead();
        fluxEntity = null;
      }

      if (ore.getCount() <= 0) {
        oreEntity.setDead();
        oreEntity = null;
      }
    }
  }

  private void updateSlagBlock(boolean cooking) {
    int slag = fuelStacks.size() + oreStacks.size();
    //If there's at least one item, show one layer so player knows that it is holding stacks
    int slagLayers = slag == 1 ? 1 : slag / 2;
    for (int i = 1; i < 6; i++) {
      if (slagLayers > 0) {
        if (slagLayers >= 4) {
          slagLayers -= 4;
          world.setBlockState(pos.up(i), BlocksDevice.MOLTEN.get().getDefaultState()
            .withProperty(LIT, cooking)
            .withProperty(LAYERS, 4));
        } else {
          world.setBlockState(pos.up(i), BlocksDevice.MOLTEN.get().getDefaultState()
            .withProperty(LIT, cooking)
            .withProperty(LAYERS, slagLayers));
          slagLayers = 0;
        }
      } else {
        //Remove any surplus slag(ie: after cooking/structure became compromised)
        if (world.getBlockState(pos.up(i)).getBlock() == BlocksDevice.MOLTEN.get()) {
          world.setBlockToAir(pos.up(i));
        }
      }
    }
  }

  public void debug() {
    ModuleDevice.LOGGER.debug("Debugging Blast Furnace:");
    ModuleDevice.LOGGER.debug("Temp {} | Burn Temp {} | Fuel Ticks {}", temperature, burnTemperature, burnTicksLeft);
    ModuleDevice.LOGGER.debug("Burning? {}", world.getBlockState(pos).getValue(LIT));
    int i = 0;
    for (ItemStack item : oreStacks) {
      ModuleDevice.LOGGER.debug("Slot: {} - NBT: {}", i, item.serializeNBT().toString());
    }
  }

  /**
   * Passed from BlockBlastFurnace's IBellowsConsumerBlock
   *
   * @param airAmount the air amount
   */
  public void onAirIntake(int airAmount) {
    ItemStack stack = inventory.getStackInSlot(SLOT_TUYERE);
    if (!stack.isEmpty() && burnTicksLeft > 0) {
      airTicks += airAmount;
      if (airTicks > MAX_AIR_TICKS) {
        airTicks = MAX_AIR_TICKS;
      }
    }
  }

  @Override
  public ContainerBlastFurnace getContainer(InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos) {
    return new ContainerBlastFurnace(inventoryPlayer, this);
  }

  @Override
  public GuiBlastFurnace getGuiContainer(InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos) {
    return new GuiBlastFurnace(getContainer(inventoryPlayer, world, state, pos), inventoryPlayer, this);
  }
}
