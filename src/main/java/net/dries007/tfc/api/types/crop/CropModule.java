package net.dries007.tfc.api.types.crop;

import net.dries007.tfc.api.types.crop.category.CropCategoryHandler;
import net.dries007.tfc.api.types.crop.type.CropTypeHandler;
import net.dries007.tfc.api.types.crop.variant.block.CropBlockVariantHandler;

public class CropModule {

    public static void preInit() {

        CropCategoryHandler.init();
        CropTypeHandler.init();
        CropBlockVariantHandler.init();
    }
}
