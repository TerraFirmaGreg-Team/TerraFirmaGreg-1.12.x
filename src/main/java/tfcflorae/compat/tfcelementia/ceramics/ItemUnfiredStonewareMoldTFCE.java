package tfcflorae.compat.tfcelementia.ceramics;

import net.dries007.tfc.objects.items.ceramics.ItemPottery;
import tfcelementia.objects.items.metal.ItemMetalTFCE;

import java.util.EnumMap;

public class ItemUnfiredStonewareMoldTFCE extends ItemPottery {
	private static final EnumMap<ItemMetalTFCE.ItemType, ItemUnfiredStonewareMoldTFCE> MAP = new EnumMap<>(ItemMetalTFCE.ItemType.class);
	public final ItemMetalTFCE.ItemType type;

	public ItemUnfiredStonewareMoldTFCE(ItemMetalTFCE.ItemType type) {
		this.type = type;
		if (MAP.put(type, this) != null) {
			throw new IllegalStateException("There can only be one.");
		}
	}

	public static ItemUnfiredStonewareMoldTFCE get(ItemMetalTFCE.ItemType category) {
		return MAP.get(category);
	}
}
