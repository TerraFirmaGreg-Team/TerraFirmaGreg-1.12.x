package su.terrafirmagreg.modules.core.capabilities.size;

import su.terrafirmagreg.modules.core.capabilities.size.spi.Size;
import su.terrafirmagreg.modules.core.capabilities.size.spi.Weight;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import net.dries007.tfc.objects.inventory.ingredient.IIngredient;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CapabilityHandlerSize {

  //Used inside CT, set custom IItemSize for items outside TFC
  public static final Map<IIngredient<ItemStack>, Supplier<ICapabilityProvider>> CUSTOM_ITEMS = new LinkedHashMap<>();

  public static void init() {
    // Add hardcoded size values for vanilla items
    CUSTOM_ITEMS.put(IIngredient.of(Items.COAL),
      () -> CapabilityProviderSize.of(Size.SMALL, Weight.LIGHT, true)); // Store anywhere stacksize = 32

    CUSTOM_ITEMS.put(IIngredient.of(Items.STICK),
      () -> CapabilityProviderSize.of(Size.SMALL, Weight.VERY_LIGHT, true)); // Store anywhere stacksize = 64

    CUSTOM_ITEMS.put(IIngredient.of(Items.CLAY_BALL),
      () -> CapabilityProviderSize.of(Size.SMALL, Weight.VERY_LIGHT, true)); // Store anywhere stacksize = 64

    CUSTOM_ITEMS.put(IIngredient.of(Items.BED),
      () -> CapabilityProviderSize.of(Size.LARGE, Weight.VERY_HEAVY, false)); // Store only in chests stacksize = 1

    CUSTOM_ITEMS.put(IIngredient.of(Items.MINECART),
      () -> CapabilityProviderSize.of(Size.LARGE, Weight.VERY_HEAVY, false)); // Store only in chests stacksize = 1

    CUSTOM_ITEMS.put(IIngredient.of(Items.ARMOR_STAND),
      () -> CapabilityProviderSize.of(Size.LARGE, Weight.HEAVY, true)); // Store only in chests stacksize = 4

    CUSTOM_ITEMS.put(IIngredient.of(Items.CAULDRON),
      () -> CapabilityProviderSize.of(Size.LARGE, Weight.LIGHT, true)); // Store only in chests stacksize = 32

    CUSTOM_ITEMS.put(IIngredient.of(Blocks.TRIPWIRE_HOOK),
      () -> CapabilityProviderSize.of(Size.SMALL, Weight.VERY_LIGHT, true)); // Store anywhere stacksize = 64

    CUSTOM_ITEMS.put(IIngredient.of(Blocks.TRIPWIRE_HOOK),
      () -> CapabilityProviderSize.of(Size.SMALL, Weight.VERY_LIGHT, true)); // Store anywhere stacksize = 64
  }

  @NotNull
  public static ICapabilityProvider getCustom(ItemStack stack) {

    for (var entry : CUSTOM_ITEMS.entrySet()) {
      if (entry.getKey().testIgnoreCount(stack)) {
        return entry.getValue().get();
      }
    }
    Item item = stack.getItem();

    if (item instanceof ICapabilitySize capability) {
      return CapabilityProviderSize.of(capability.getSize(stack), capability.getWeight(stack), capability.canStack(stack));
    }

    if (item instanceof ItemBlock itemBlock && itemBlock.getBlock() instanceof ICapabilitySize capability) {
      return CapabilityProviderSize.of(capability.getSize(stack), capability.getWeight(stack), capability.canStack(stack));
    }

    if (item instanceof ItemTool || item instanceof ItemSword) {
      return CapabilityProviderSize.of(Size.LARGE, Weight.MEDIUM, true); // Stored only in chests, stacksize should be limited to 1 since it is a tool

    }
    if (item instanceof ItemArmor) {
      return CapabilityProviderSize.of(Size.LARGE, Weight.VERY_HEAVY, true); // Stored only in chests and stacksize = 1

    }
    if (item instanceof ItemBlock itemBlock) {
      Block block = itemBlock.getBlock();

//      if (block instanceof ICapabilitySize capabilitySize) {
//        return capabilitySize;
//      }

      if (block instanceof BlockLadder) {
        return CapabilityProviderSize.of(Size.SMALL, Weight.VERY_LIGHT, true); // Fits small vessels and stacksize = 64
      }
      return CapabilityProviderSize.of(Size.SMALL, Weight.LIGHT, true); // Fits small vessels and stacksize = 32

    }
    return CapabilityProviderSize.of(Size.VERY_SMALL, Weight.VERY_LIGHT, true); // Stored anywhere and stacksize = 64
  }
}
