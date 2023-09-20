package net.dries007.tfc.module.rock;

import net.dries007.tfc.module.rock.api.category.RockCategoryHandler;
import net.dries007.tfc.module.rock.api.type.RockTypeHandler;
import net.dries007.tfc.module.rock.api.variant.block.RockBlockVariantHandler;
import net.dries007.tfc.module.rock.api.variant.item.RockItemVariantHandler;

public class ModuleRock {

    public static void preInit() {
        RockCategoryHandler.init();
        RockTypeHandler.init();
        RockBlockVariantHandler.init();
        RockItemVariantHandler.init();
    }

    public static void init() {
    }

    public static void postInit() {
    }
}
