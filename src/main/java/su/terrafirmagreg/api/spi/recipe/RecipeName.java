package su.terrafirmagreg.api.spi.recipe;

import su.terrafirmagreg.api.data.Constants;
import su.terrafirmagreg.api.util.ModUtils;

import net.minecraft.util.ResourceLocation;

public class RecipeName {

    private static int nextId = -1;
    private static String prefix = "";

    public static String generate() {
        if (prefix.isEmpty())
            prefix = Constants.MOD_ID + "_";

        return generate(prefix);
    }

    public static String generate(String prefix) {
        return prefix + Integer.toHexString(nextId--);
    }

    public static ResourceLocation generateRl(String prefix) {
        return ModUtils.resource(generate(prefix));
    }
}
