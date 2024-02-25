package su.terrafirmagreg.api.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class OreDictHelper {

	private OreDictHelper() {
		//
	}

	public static void register(String oreDict, ItemStack... itemStacks) {

		for (ItemStack itemStack : itemStacks) {
			OreDictionary.registerOre(oreDict, itemStack);
		}
	}

	public static boolean contains(String oreDict, ItemStack itemStack) {

		if (itemStack.isEmpty()) {
			return false;
		}

		int logWood = OreDictionary.getOreID(oreDict);
		int[] oreIDs = OreDictionary.getOreIDs(itemStack);

		//noinspection ForLoopReplaceableByForEach
		for (int i = 0; i < oreIDs.length; i++) {

			if (oreIDs[i] == logWood) {
				return true;
			}
		}

		return false;
	}

}
