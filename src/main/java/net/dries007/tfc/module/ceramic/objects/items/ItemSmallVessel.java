package net.dries007.tfc.module.ceramic.objects.items;

import gregtech.api.fluids.MetaFluids;
import gregtech.api.unification.material.Material;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.dries007.tfc.Tags;
import net.dries007.tfc.api.capability.ISmallVesselHandler;
import net.dries007.tfc.api.capability.food.CapabilityFood;
import net.dries007.tfc.api.capability.food.FoodTrait;
import net.dries007.tfc.api.capability.food.IFood;
import net.dries007.tfc.module.core.api.capability.heat.CapabilityItemHeat;
import net.dries007.tfc.api.capability.metal.CapabilityMetalItem;
import net.dries007.tfc.api.capability.metal.IMaterialItem;
import net.dries007.tfc.module.core.api.capability.size.CapabilityItemSize;
import net.dries007.tfc.client.util.TFCGuiHandler;
import net.dries007.tfc.common.objects.inventory.capability.ISlotCallback;
import net.dries007.tfc.common.objects.inventory.slot.SlotCallback;
import net.dries007.tfc.compat.gregtech.material.TFGPropertyKey;
import net.dries007.tfc.config.ConfigTFC;
import net.dries007.tfc.module.core.ModuleCore;
import net.dries007.tfc.module.core.api.capability.size.IItemSizeAndWeight;
import net.dries007.tfc.module.core.api.capability.size.Size;
import net.dries007.tfc.module.core.api.capability.size.Weight;
import net.dries007.tfc.module.core.network.SCPacketSimpleMessage;
import net.dries007.tfc.module.core.network.SCPacketSimpleMessage.MessageCategory;
import net.dries007.tfc.module.core.objects.container.CapabilityContainerListener;
import net.dries007.tfc.util.Alloy;
import net.dries007.tfc.util.calendar.CalendarTFC;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankPropertiesWrapper;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map.Entry;


@ParametersAreNonnullByDefault
public class ItemSmallVessel extends ItemPottery {
    private final boolean glazed;

    public ItemSmallVessel(boolean glazed) {
        this.glazed = glazed;
        setHasSubtypes(glazed);
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote && !playerIn.isSneaking()) {
            IFluidHandler cap = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null);
            if (cap instanceof ISmallVesselHandler) {
                ISmallVesselHandler.Mode mode = ((ISmallVesselHandler) cap).getFluidMode();
                switch (mode) {
                    case INVENTORY:
                        TFCGuiHandler.openGui(worldIn, playerIn, TFCGuiHandler.Type.SMALL_VESSEL);
                        break;
                    case LIQUID_MOLTEN:
                        TFCGuiHandler.openGui(worldIn, playerIn, TFCGuiHandler.Type.SMALL_VESSEL_LIQUID);
                        break;
                    case LIQUID_SOLID:
                        ModuleCore.PACKET_SERVICE.sendTo(SCPacketSimpleMessage.translateMessage(MessageCategory.VESSEL, Tags.MOD_ID + ".vessel.liquid_solid"), (EntityPlayerMP) playerIn);
                        break;
                }
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        if (!glazed) {
            return new TextComponentTranslation("item.tfc.ceramics.fired.vessel.name").getFormattedText();
        } else {
            return new TextComponentTranslation(
                    "item.tfc.ceramics.fired.vessel_glazed.name",
                    new TextComponentTranslation("color." + EnumDyeColor.byDyeDamage(stack.getItemDamage()).getName())
            ).getFormattedText();
        }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (isInCreativeTab(tab)) {
            if (!glazed) {
                items.add(new ItemStack(this));
            } else {
                for (EnumDyeColor color : EnumDyeColor.values()) {
                    items.add(new ItemStack(this, 1, color.getDyeDamage()));
                }
            }
        }
    }

    @Nullable
    @Override
    public NBTTagCompound getNBTShareTag(ItemStack stack) {
        return CapabilityContainerListener.readShareTag(stack);
    }

    @Override
    public void readNBTShareTag(ItemStack stack, @Nullable NBTTagCompound nbt) {
        CapabilityContainerListener.applyShareTag(stack, nbt);
    }

    @Override
    public boolean canStack(ItemStack stack) {
        return false;
    }

    /**
     * Get the firing result of a vessel in a heating device
     *
     * @param input the small vessel
     * @return the vessel with molten contents, if possible
     */
    @Nonnull
    public ItemStack getFiringResult(ItemStack input) {
        IItemHandler capItemHandler = input.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (capItemHandler instanceof ISmallVesselHandler cap) {
            var alloy = new Alloy();

            for (int i = 0; i < cap.getSlots(); i++) {
                alloy.add(cap.getStackInSlot(i), 1600f);
                cap.setStackInSlot(i, ItemStack.EMPTY);
            }

            cap.setFluidMode(true);
            cap.fill(new FluidStack(alloy.getResult().getFluid(), alloy.getAmount()), true);
            cap.setTemperature(1600f);
        }
        return input;
    }

    @Nonnull
    @Override
    public Size getSize(ItemStack stack) {
        return Size.NORMAL; // Can't be stored in itself
    }

    @Nonnull
    @Override
    public Weight getWeight(ItemStack stack) {
        return Weight.VERY_HEAVY; // Stacksize = 1
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new SmallVesselCapability(nbt);
    }

    // Extends ItemStackHandler for ease of use. Duplicates most of ItemHeatHandler functionality
    private class SmallVesselCapability extends ItemStackHandler implements ICapabilityProvider, ISmallVesselHandler, ISlotCallback {
        private final FluidTank tank;

        private float heatCapacity;
        private float meltTemp;
        private float temperature;
        private long lastUpdateTick;

        private boolean fluidMode; // Does the stack contain molten metal?
        private IFluidTankProperties[] fluidTankProperties;

        SmallVesselCapability(@Nullable NBTTagCompound nbt) {
            super(4);

            tank = new FluidTank(ConfigTFC.Devices.SMALL_VESSEL.tank);
            fluidMode = false;
            deserializeNBT(nbt);
        }

        @Override
        public Mode getFluidMode() {
            if (fluidMode) {
                return getTemperature() < meltTemp ? Mode.LIQUID_SOLID : Mode.LIQUID_MOLTEN;
            }
            return Mode.INVENTORY;
        }

        @Override
        public void setFluidMode(boolean fluidMode) {
            this.fluidMode = fluidMode;
        }

        @Override
        public float getTemperature() {
            return CapabilityItemHeat.adjustTemp(temperature, heatCapacity, CalendarTFC.PLAYER_TIME.getTicks() - lastUpdateTick);
        }

        @Override
        public void setTemperature(float temperature) {
            this.temperature = temperature;
            this.lastUpdateTick = CalendarTFC.PLAYER_TIME.getTicks();
        }

        @Override
        public float getHeatCapacity() {
            return heatCapacity;
        }

        @Override
        public float getMeltTemp() {
            return meltTemp;
        }

        @SideOnly(Side.CLIENT)
        @Override
        public void addHeatInfo(@Nonnull ItemStack stack, @Nonnull List<String> text) {
            var material = getMaterial();

            ISmallVesselHandler.super.addHeatInfo(stack, text);

            if (material != null) {
                text.add("");
                text.add(I18n.format("tfc.tooltip.containsmetal"));

                if (isMolten()) {
                    text.add(I18n.format("tfc.tooltip.state", I18n.format("tfc.tooltip.liquid")));
                } else {
                    text.add(I18n.format("tfc.tooltip.state", I18n.format("tfc.tooltip.solid")));
                }

                text.add(I18n.format("tfc.tooltip.metalname", material.getLocalizedName()));
                text.add(I18n.format("tfc.tooltip.units", getAmount()));
            } else {
                boolean hasContent = false;
                Object2IntMap<Material> materials = new Object2IntOpenHashMap<>();
                boolean onlySmeltables = true;
                for (ItemStack slot : super.stacks) {
                    if (!slot.isEmpty()) {
                        IMaterialItem itemMetal = CapabilityMetalItem.getMaterialItem(slot);
                        if (itemMetal != null) {
                            materials.merge(itemMetal.getMaterial(slot), itemMetal.getSmeltAmount(slot) * slot.getCount(), Integer::sum);
                        } else {
                            onlySmeltables = false;
                        }
                        text.add(I18n.format(Tags.MOD_ID + ".tooltip.small_vessel_item", slot.getCount(), slot.getItem().getItemStackDisplayName(slot)));
                        hasContent = true;
                    }
                }

                if (hasContent) {
                    if (onlySmeltables) {
                        int textPosition = (int) super.stacks.stream().filter(itemstack -> !ItemStack.EMPTY.equals(itemstack)).count() + 1;
                        int totalAmount = materials.values().stream().reduce(0, Integer::sum);
                        for (Entry<Material, Integer> entry : materials.entrySet()) {
                            var key = entry.getKey();
                            if (key != null) {
                                int metalAmount = entry.getValue();
                                text.add(textPosition, I18n.format(Tags.MOD_ID + ".tooltip.small_vessel_unit_total", key.getLocalizedName(), metalAmount, Math.round((float) metalAmount / totalAmount * 1000) / 10f));
                            }
                        }
                        text.add(textPosition, ""); // Separator between the contents of the vessel and the above units text, not needed but I feel that it helps visually
                    }
                } else {
                    text.add(1, I18n.format(Tags.MOD_ID + ".tooltip.small_vessel_empty"));
                }
            }

        }

        @Nullable
        @Override
        public Material getMaterial() {
            return fluidMode && tank.getFluid() != null ? MetaFluids.getMaterialFromFluid(tank.getFluid().getFluid()) : null;
        }

        @Override
        public int getAmount() {
            return fluidMode ? tank.getFluidAmount() : 0;
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityItemHeat.ITEM_HEAT_CAPABILITY;
        }

        @Nullable
        @Override
        @SuppressWarnings("unchecked")
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return hasCapability(capability, facing) ? (T) this : null;
        }

        @Override
        public IFluidTankProperties[] getTankProperties() {
            if (fluidTankProperties == null) {
                fluidTankProperties = new IFluidTankProperties[]{new FluidTankPropertiesWrapper(tank)};
            }
            return fluidTankProperties;
        }

        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if ((fluidMode || isInventoryEmpty()) && resource != null) {
                updateFluidData(resource);
                fluidMode = true;
                return tank.fill(resource, doFill);
            }
            return 0;
        }

        @Nullable
        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            if (getFluidMode() == Mode.LIQUID_MOLTEN) {
                return tank.drain(resource, doDrain);
            }
            return null;
        }

        @Nullable
        @Override
        public FluidStack drain(int maxDrain, boolean doDrain) {
            if (getFluidMode() == Mode.LIQUID_MOLTEN) {
                return tank.drain(maxDrain, doDrain);
            }
            return null;
        }

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            IFood cap = stack.getCapability(CapabilityFood.CAPABILITY, null);
            if (cap != null) {
                CapabilityFood.applyTrait(cap, FoodTrait.PRESERVED);
            }
            super.setStackInSlot(slot, stack);
        }

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (!simulate) {
                IFood cap = stack.getCapability(CapabilityFood.CAPABILITY, null);
                if (cap != null) {
                    CapabilityFood.applyTrait(cap, FoodTrait.PRESERVED);
                }
            }
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        @Nonnull
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack stack = super.extractItem(slot, amount, simulate).copy();
            IFood cap = stack.getCapability(CapabilityFood.CAPABILITY, null);
            if (cap != null) {
                CapabilityFood.removeTrait(cap, FoodTrait.PRESERVED);
            }
            return stack;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            IItemSizeAndWeight size = CapabilityItemSize.getIItemSize(stack);
            if (size != null) {
                return size.getSize(stack).isSmallerThan(Size.NORMAL);
            }
            return false;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound nbt = new NBTTagCompound();
            fluidMode = tank.getFluidAmount() > 0;
            nbt.setBoolean("fluidMode", fluidMode);

            // Duplicated from ItemHeatHandler
            if (getTemperature() <= 0) {
                nbt.setLong("ticks", -1);
                nbt.setFloat("heat", 0);
            } else {
                nbt.setLong("ticks", lastUpdateTick);
                nbt.setFloat("heat", temperature);
            }

            if (fluidMode) {
                // Save fluid data
                NBTTagCompound fluidData = new NBTTagCompound();
                tank.writeToNBT(fluidData);
                nbt.setTag("fluids", fluidData);
            } else {
                // Save item data
                nbt.setTag("items", super.serializeNBT());
            }
            return nbt;
        }

        @Override
        public void deserializeNBT(@Nullable NBTTagCompound nbt) {
            if (nbt != null) {
                temperature = nbt.getFloat("heat");
                lastUpdateTick = nbt.getLong("ticks");
                fluidMode = nbt.getBoolean("fluidMode");

                if (fluidMode && nbt.hasKey("fluids", Constants.NBT.TAG_COMPOUND)) {
                    // Read fluid contents
                    tank.readFromNBT(nbt.getCompoundTag("fluids"));
                } else if (!fluidMode && nbt.hasKey("items", Constants.NBT.TAG_COMPOUND)) {
                    // Read item contents
                    super.deserializeNBT(nbt.getCompoundTag("items"));
                }
            }
            updateFluidData(tank.getFluid());
        }

        /**
         * This is used for a very unique situation, see #1083
         * By tracing the call path through {@link net.minecraft.inventory.Container#slotClick(int, int, ClickType, EntityPlayer)}, the *only* method that can possibly intercept in that massive chain, for clicking on a slot with a stack is either this one (in which case we handle the previous item stack in the slot which a reference has been obtained to)
         * Thus, we don't actually care about the stack being put in the slot. We do assume that since this stack is being put in the slot, a different stack is being taken out.
         */
        @Override
        public void beforePutStack(SlotCallback slot, @Nonnull ItemStack stack) {
            IFood cap = slot.getStack().getCapability(CapabilityFood.CAPABILITY, null);
            if (cap != null) {
                CapabilityFood.removeTrait(cap, FoodTrait.PRESERVED);
            }
        }

        private void updateFluidData(@Nullable FluidStack fluid) {
            meltTemp = 1000;
            heatCapacity = 1;
            if (fluid != null) {
                var material = MetaFluids.getMaterialFromFluid(fluid.getFluid());
                if (material != null) {
                    var property = material.getProperty(TFGPropertyKey.HEAT);

                    meltTemp = property.getMeltTemp();
                    heatCapacity = property.getSpecificHeat();
                }
            }
        }

        private boolean isInventoryEmpty() {
            for (int i = 0; i < getSlots(); i++) {
                if (!getStackInSlot(i).isEmpty()) {
                    return false;
                }
            }
            return true;
        }
    }

}
