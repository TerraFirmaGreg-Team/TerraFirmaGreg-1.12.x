package net.dries007.tfc.module.agriculture.api.crop.category;

import net.minecraft.util.text.TextFormatting;

public class CropCategoryHandler {

    public static void init() {
        CropCategories.SIMPLE = new CropCategory("simple", TextFormatting.GREEN);
        CropCategories.PICKABLE = new CropCategory("pickable", TextFormatting.YELLOW);
    }
}
