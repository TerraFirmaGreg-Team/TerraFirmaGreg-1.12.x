package tfcflorae.objects.items.ceramics;

import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.items.ceramics.ItemPottery;

import java.util.EnumMap;

public class ItemUnfiredEarthenwareMold extends ItemPottery {
    private static final EnumMap<Metal.ItemType, ItemUnfiredEarthenwareMold> MAP = new EnumMap<>(Metal.ItemType.class);
    public final Metal.ItemType type;

    public ItemUnfiredEarthenwareMold(Metal.ItemType type) {
        this.type = type;
        if (MAP.put(type, this) != null) {
            throw new IllegalStateException("There can only be one.");
        }
    }

    public static ItemUnfiredEarthenwareMold get(Metal.ItemType category) {
        return MAP.get(category);
    }
}
