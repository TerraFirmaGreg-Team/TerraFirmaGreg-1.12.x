package su.terrafirmagreg.modules.device.object.tile;

import su.terrafirmagreg.api.base.object.tile.api.ITileFields;
import su.terrafirmagreg.api.base.object.tile.spi.BaseTileTickableInventory;
import su.terrafirmagreg.api.util.MathUtils;
import su.terrafirmagreg.api.util.NBTUtils;
import su.terrafirmagreg.framework.registry.api.provider.IProviderContainer;
import su.terrafirmagreg.modules.core.capabilities.food.CapabilityFood;
import su.terrafirmagreg.modules.core.capabilities.food.spi.FoodTrait;
import su.terrafirmagreg.modules.core.capabilities.heat.CapabilityHeat;
import su.terrafirmagreg.modules.core.feature.ambiental.modifier.ModifierTile;
import su.terrafirmagreg.modules.core.feature.ambiental.provider.IAmbientalProviderTile;
import su.terrafirmagreg.modules.core.feature.calendar.Calendar;
import su.terrafirmagreg.modules.core.feature.calendar.ICalendarTickable;
import su.terrafirmagreg.modules.device.ConfigDevice;
import su.terrafirmagreg.modules.device.ModuleDevice;
import su.terrafirmagreg.modules.device.client.gui.GuiCharcoalForge;
import su.terrafirmagreg.modules.device.object.container.ContainerCharcoalForge;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import net.dries007.tfc.api.recipes.heat.HeatRecipe;
import net.dries007.tfc.api.util.IHeatConsumerBlock;
import net.dries007.tfc.util.fuel.Fuel;
import net.dries007.tfc.util.fuel.FuelManager;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

import static su.terrafirmagreg.api.data.Properties.BoolProp.LIT;

public class TileCharcoalForge extends BaseTileTickableInventory
  implements ICalendarTickable, ITileFields, IAmbientalProviderTile,
             IProviderContainer<ContainerCharcoalForge, GuiCharcoalForge> {

  public static final int SLOT_FUEL_MIN = 0;
  public static final int SLOT_FUEL_MAX = 4;
  public static final int SLOT_INPUT_MIN = 5;
  public static final int SLOT_INPUT_MAX = 9;
  public static final int SLOT_EXTRA_MIN = 10;
  public static final int SLOT_EXTRA_MAX = 13;

  public static final int FIELD_TEMPERATURE = 0;

  private static final int MAX_AIR_TICKS = ConfigDevice.BLOCK.BELLOWS.maxTicks;

  private final HeatRecipe[] cachedRecipes = new HeatRecipe[5];
  private boolean requiresSlotUpdate = false;
  private float temperature; // Current Temperature
  private int burnTicks; // Ticks remaining on the current item of fuel
  private float burnTemperature; // Temperature provided from the current item of fuel
  private int airTicks; // Ticks of air provided by bellows
  private long lastPlayerTick; // Last player tick this forge was ticked (for purposes of catching up)

  public TileCharcoalForge() {
    // 0 - 4 are fuel slots: 0 being the lowest, 4 highest (in order of consumption)
    // 5 - 9 are the input slots. Same arrangement (0 lowest, then alternating L, R, L high, R high)
    // 10 - 13 are the extra slots for molds and stuff. 10 at the top, 14 at the bottom. Iterate in that order
    super(14);

    temperature = 0;
    burnTemperature = 0;
    burnTicks = 0;
    airTicks = 0;
    lastPlayerTick = Calendar.PLAYER_TIME.getTicks();

    Arrays.fill(cachedRecipes, null);
  }

  public void onAirIntake(int amount) {
    airTicks += amount;
    if (airTicks > MAX_AIR_TICKS) {
      airTicks = MAX_AIR_TICKS;
    }
  }

  /**
   * Consume more fuel on rain
   */
  public void onRainDrop() {
    burnTicks -= ConfigDevice.BLOCK.CHARCOAL_FORGE.rainTicks;
    // Play the "tsssss" sound
    world.playSound(null, pos, SoundEvents.BLOCK_LAVA_EXTINGUISH, SoundCategory.BLOCKS, 0.8f, 0.8f + MathUtils.RNG.nextFloat() * 0.4f);
  }

  @Override
  public void update() {
    super.update();
    checkForCalendarUpdate();
    if (!world.isRemote) {
      IBlockState state = world.getBlockState(pos);
      if (state.getValue(LIT)) {
        // Update fuel
        if (burnTicks > 0) {
          // Double fuel consumption if using bellows
          burnTicks -= airTicks > 0 ? 2 : 1;
        }
        if (burnTicks <= 0) {
          // Consume fuel
          ItemStack stack = inventory.getStackInSlot(SLOT_FUEL_MIN);
          if (stack.isEmpty()) {
            world.setBlockState(pos, state.withProperty(LIT, false));
            burnTicks = 0;
            burnTemperature = 0;
          } else {
            inventory.setStackInSlot(SLOT_FUEL_MIN, ItemStack.EMPTY);
            requiresSlotUpdate = true;
            Fuel fuel = FuelManager.getFuel(stack);
            burnTicks = fuel.getAmount();
            burnTemperature = fuel.getTemperature();
          }
        }
      } else if (burnTemperature > 0) {
        // If not lit, stop burning
        burnTemperature = 0;
        burnTicks = 0;
      }

      // Update air ticks
      if (airTicks > 0) {
        airTicks--;
      } else {
        airTicks = 0;
      }

      // Always update temperature / cooking, until the fire pit is not hot anymore
      if (temperature > 0 || burnTemperature > 0) {
        // Update temperature
        temperature = CapabilityHeat.adjustToTargetTemperature(temperature, burnTemperature, airTicks, MAX_AIR_TICKS);

        // Provide heat to blocks that are one block above
        Block blockUp = world.getBlockState(pos.up()).getBlock();
        if (blockUp instanceof IHeatConsumerBlock heatConsumerBlock) {
          heatConsumerBlock.acceptHeat(world, pos.up(), temperature);
        }

        // Update items in slots
        // Loop through input + 2 output slots
        for (int i = SLOT_INPUT_MIN; i <= SLOT_INPUT_MAX; i++) {
          ItemStack stack = inventory.getStackInSlot(i);
          var cap = CapabilityHeat.get(stack);
          if (cap != null) {
            // Update temperature of item
            float itemTemp = cap.getTemperature();
            if (temperature > itemTemp) {
              CapabilityHeat.addTemp(cap);
            }

            // Handle possible melting, or conversion (if reach 1599 = pit kiln temperature)
            handleInputMelting(stack, i);
          }
        }
      }

      // This is here to avoid duplication glitches
      if (requiresSlotUpdate) {
        cascadeFuelSlots();
      }
      markDirty();
    }
  }

  private void handleInputMelting(ItemStack stack, int startIndex) {
    HeatRecipe recipe = cachedRecipes[startIndex - SLOT_INPUT_MIN];
    var cap = CapabilityHeat.get(stack);

    if (recipe != null && cap != null && recipe.isValidTemperature(cap.getTemperature())) {
      // Handle possible metal output
      FluidStack fluidStack = recipe.getOutputFluid(stack);
      ItemStack outputStack = recipe.getOutputStack(stack);
      float itemTemperature = cap.getTemperature();
      if (fluidStack != null) {
        // Loop through all input slots
        for (int i = SLOT_EXTRA_MIN; i <= SLOT_EXTRA_MAX; i++) {
          // While the fluid is still waiting
          if (fluidStack.amount <= 0) {
            break;
          }
          // Try an output slot
          ItemStack output = inventory.getStackInSlot(i);
          // Fill the fluid
          IFluidHandler fluidHandler = output.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
          if (fluidHandler != null) {
            int amountFilled = fluidHandler.fill(fluidStack.copy(), true);
            if (amountFilled > 0) {
              fluidStack.amount -= amountFilled;

              // If the fluid was filled, make sure to make it the same temperature
              var heatHandler = CapabilityHeat.get(output);
              if (heatHandler != null) {
                heatHandler.setTemperature(itemTemperature);
              }
            }
          }
        }
      }

      // Charcoal grilled!
      CapabilityFood.applyTrait(outputStack, FoodTrait.CHARCOAL_GRILLED);

      // Handle possible item output
      inventory.setStackInSlot(startIndex, outputStack);
    }
  }

  @Override
  public ContainerCharcoalForge getContainer(InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos) {
    return new ContainerCharcoalForge(inventoryPlayer, this);
  }

  @Override
  public GuiCharcoalForge getGuiContainer(InventoryPlayer inventoryPlayer, World world, IBlockState state, BlockPos pos) {
    return new GuiCharcoalForge(getContainer(inventoryPlayer, world, state, pos), inventoryPlayer, this);
  }

  private void cascadeFuelSlots() {
    // This will cascade all fuel down to the lowest available slot
    int lowestAvailSlot = 0;
    for (int i = 0; i <= SLOT_FUEL_MAX; i++) {
      ItemStack stack = inventory.getStackInSlot(i);
      if (!stack.isEmpty()) {
        // Move to lowest avail slot
        if (i > lowestAvailSlot) {
          inventory.setStackInSlot(lowestAvailSlot, stack.copy());
          inventory.setStackInSlot(i, ItemStack.EMPTY);
        }
        lowestAvailSlot++;
      }
    }
    requiresSlotUpdate = false;
  }

  @Override
  public long getLastUpdateTick() {
    return lastPlayerTick;
  }

  @Override
  public void setLastUpdateTick(long tick) {
    this.lastPlayerTick = tick;
  }

  @Override
  public void onCalendarUpdate(long deltaPlayerTicks) {
    IBlockState state = world.getBlockState(pos);
    if (!state.getValue(LIT)) {
      return;
    }
    // Consume fuel as dictated by the delta player ticks (don't simulate any input changes), and then extinguish
    if (burnTicks > deltaPlayerTicks) {
      burnTicks -= deltaPlayerTicks;
      return;
    } else {
      deltaPlayerTicks -= burnTicks;
      burnTicks = 0;
    }
    // Need to consume fuel
    requiresSlotUpdate = true;
    for (int i = SLOT_FUEL_MIN; i <= SLOT_FUEL_MAX; i++) {
      ItemStack fuelStack = inventory.getStackInSlot(i);
      Fuel fuel = FuelManager.getFuel(fuelStack);
      inventory.setStackInSlot(i, ItemStack.EMPTY);
      if (fuel.getAmount() > deltaPlayerTicks) {
        burnTicks = (int) (fuel.getAmount() - deltaPlayerTicks);
        burnTemperature = fuel.getTemperature();
        return;
      } else {
        deltaPlayerTicks -= fuel.getAmount();
        burnTicks = 0;
      }
    }
    if (deltaPlayerTicks > 0) {
      // Consumed all fuel, so extinguish and cool instantly
      burnTemperature = 0;
      temperature = 0;
      for (int i = SLOT_INPUT_MIN; i <= SLOT_INPUT_MAX; i++) {
        ItemStack stack = inventory.getStackInSlot(i);
        var cap = CapabilityHeat.get(stack);
        if (cap != null) {
          cap.setTemperature(0f);
        }
      }
      world.setBlockState(pos, state.withProperty(LIT, false));
    }
  }


  public void onCreate() {
    burnTicks = 200;
    burnTemperature = 500;
  }

  @Override
  public void setAndUpdateSlots(int slot) {
    super.setAndUpdateSlots(slot);
    requiresSlotUpdate = true;
    updateCachedRecipes();
  }

  @Override
  public void readFromNBT(NBTTagCompound nbt) {
    temperature = nbt.getFloat("temperature");
    burnTicks = nbt.getInteger("burnTicks");
    airTicks = nbt.getInteger("airTicks");
    burnTemperature = nbt.getFloat("burnTemperature");
    lastPlayerTick = nbt.getLong("lastPlayerTick");
    super.readFromNBT(nbt);

    updateCachedRecipes();
  }

  @Override
  @NotNull
  public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
    NBTUtils.setGenericNBTValue(nbt, "temperature", temperature);
    NBTUtils.setGenericNBTValue(nbt, "burnTicks", burnTicks);
    NBTUtils.setGenericNBTValue(nbt, "airTicks", airTicks);
    NBTUtils.setGenericNBTValue(nbt, "burnTemperature", burnTemperature);
    NBTUtils.setGenericNBTValue(nbt, "lastPlayerTick", lastPlayerTick);
    return super.writeToNBT(nbt);
  }

  private void updateCachedRecipes() {
    // cache heat recipes for each input
    for (int i = SLOT_INPUT_MIN; i <= SLOT_INPUT_MAX; i++) {
      cachedRecipes[i - SLOT_INPUT_MIN] = null;
      ItemStack inputStack = inventory.getStackInSlot(i);
      if (!inputStack.isEmpty()) {
        cachedRecipes[i - SLOT_INPUT_MIN] = HeatRecipe.get(inputStack);
      }
    }
  }

  @Override
  public int getSlotLimit(int slot) {
    // All slots have limit 1
    return 1;
  }

  @Override
  public boolean isItemValid(int slot, @NotNull ItemStack stack) {
    if (slot <= SLOT_FUEL_MAX) {
      // Fuel slots - anything that is a valid TFC fuel
      return FuelManager.isItemForgeFuel(stack);
    } else if (slot <= SLOT_INPUT_MAX) {
      // Input slots - anything that can heat up
      return CapabilityHeat.has(stack);
    } else {
      // Extra slots - anything that can heat up and hold fluids
      return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null) && CapabilityHeat.has(stack);
    }
  }

  @Override
  public int getFieldCount() {
    return 1;
  }

  @Override
  public void setField(int index, int value) {
    if (index == FIELD_TEMPERATURE) {
      this.temperature = (float) value;
    } else {
      ModuleDevice.LOGGER.warn("Invalid field ID {} in TECharcoalForge#setField", index);
    }
  }

  @Override
  public int getField(int index) {
    if (index == FIELD_TEMPERATURE) {
      return (int) temperature;
    }
    ModuleDevice.LOGGER.warn("Invalid field ID {} in TECharcoalForge#getField", index);
    return 0;
  }

  @Override
  public Optional<ModifierTile> getModifier(EntityPlayer player, TileEntity tile) {
    float change = temperature / 140f;
    float potency = temperature / 350f;
    if (ModifierTile.hasProtection(player)) {
      change = 1.0F;
    }
    return ModifierTile.defined(this.getBlockType().getRegistryName().getPath(), change, potency);
  }


}
