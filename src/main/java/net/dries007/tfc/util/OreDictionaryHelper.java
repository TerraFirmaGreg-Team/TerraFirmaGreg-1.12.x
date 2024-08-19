package net.dries007.tfc.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;


import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.google.common.base.Joiner;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;


import su.terrafirmagreg.modules.core.capabilities.damage.spi.DamageType;


import net.dries007.tfc.api.types.Rock;

import org.jetbrains.annotations.NotNull;

/**
 * This is not the best example of good coding practice, but I do think it works rather well. The reason for the delayed registration it because now the helper's functions can be
 * called in the constructor of the blocks/items (BEFORE they are actually in registries). At this point you cannot yet make an itemstack. Storing based on RegistryName is also not
 * possible, as they don't have one yet.
 */
public class OreDictionaryHelper {

    public static final Multimap<Thing, String> MAP = HashMultimap.create();
    private static final Converter<String, String> UPPER_UNDERSCORE_TO_LOWER_CAMEL = CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL);
    private static final Joiner JOINER_UNDERSCORE = Joiner.on('_').skipNulls();
    private static boolean done = false;

    public static String toString(Object... parts) {
        return UPPER_UNDERSCORE_TO_LOWER_CAMEL.convert(JOINER_UNDERSCORE.join(parts));
    }

    public static String toString(Iterable<Object> parts) {
        return UPPER_UNDERSCORE_TO_LOWER_CAMEL.convert(JOINER_UNDERSCORE.join(parts));
    }

    public static String toString(Object[] prefix, Object... parts) {
        return toString(ImmutableList.builder().add(prefix).add(parts).build());
    }

    public static void register(Block thing, Object... parts) {
        register(new Thing(thing), parts);
    }

    public static void register(Item thing, Object... parts) {
        register(new Thing(thing), parts);
    }

    public static void registerMeta(Item thing, int meta, Object... parts) {
        register(new Thing(thing, meta), parts);
    }

    public static void registerRockType(Block thing, Rock.Type type, Object... prefixParts) {
        registerRockType(new Thing(thing), type, prefixParts);
    }

    public static void registerDamageType(Item thing, DamageType type) {
        register(thing, "damage", "type", type.name().toLowerCase());
    }

    /**
     * Checks if an ItemStack has an OreDictionary entry that matches 'name'.
     */
    public static boolean doesStackMatchOre(@NotNull ItemStack stack, String name) {
        if (!OreDictionary.doesOreNameExist(name)) {
            //TerraFirmaCraft.getLog().warn("doesStackMatchOre called with non-existing name. stack: {} name: {}", stack, name);
            return false;
        }
        if (stack.isEmpty()) return false;
        int needle = OreDictionary.getOreID(name);
        for (int id : OreDictionary.getOreIDs(stack)) {
            if (id == needle) return true;
        }
        return false;
    }

    private static void register(Thing thing, Object... parts) {
        if (done) throw new IllegalStateException("Cannot use the helper to register after postInit has past.");
        MAP.put(thing, toString(parts));
    }

    private static void registerRockType(Thing thing, Rock.Type type, Object... prefixParts) {
        switch (type) {
            case RAW:
                MAP.put(thing, toString(prefixParts, "stone"));
                break;
            case SMOOTH:
                MAP.put(thing, toString(prefixParts, "stone", "polished"));
                break;
            case COBBLE:
                MAP.put(thing, toString(prefixParts, "cobblestone"));
                break;
            case BRICKS:
                MAP.put(thing, toString(prefixParts, "stone", "brick"));
                MAP.put(thing, toString(prefixParts, "brick", "stone"));
                break;
            case DRY_GRASS:
                MAP.put(thing, toString(prefixParts, type, "dry"));
                break;
            case CLAY:
                MAP.put(thing, toString(prefixParts, "block", type, "dirt"));
                break;
            case CLAY_GRASS:
                MAP.put(thing, toString(prefixParts, "block", type));
                break;
            case SAND:
            case GRAVEL:
            case DIRT:
            case GRASS:
            default:
                MAP.put(thing, toString(prefixParts, type));
        }
    }

    public static class Thing {

        private final Block block;
        private final Item item;
        private final int meta;

        public Thing(Block thing) {
            block = thing;
            item = null;
            meta = 0;
        }

        public Thing(Item thing) {
            this(thing, -1);
        }

        public Thing(Item thing, int meta) {
            block = null;
            item = thing;
            this.meta = meta;
        }

        public ItemStack toItemStack() {
            if (block != null) {
                return new ItemStack(block, 1, meta);
            } else if (item != null) {
                int meta = this.meta;
                if (meta == -1 && item.isDamageable()) {
                    meta = OreDictionary.WILDCARD_VALUE;
                }
                return new ItemStack(item, 1, meta);
            }
            return ItemStack.EMPTY;
        }
    }
}
