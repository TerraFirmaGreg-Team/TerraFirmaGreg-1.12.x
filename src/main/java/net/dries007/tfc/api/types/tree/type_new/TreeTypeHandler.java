package net.dries007.tfc.api.types.tree.type_new;

import static net.dries007.tfc.api.types.tree.type_new.TreeTypes.ACACIA_TREE;
import static net.dries007.tfc.api.types.wood.type.WoodTypes.ACACIA;

public class TreeTypeHandler {

    public static void init() {
        ACACIA_TREE = new TreeType.Builder("acacia")
                .setWoodType(ACACIA)
                .setTemp(19f, 31f).setRain(30f, 210f)
                .setParamMap(0.10f, 14f, 6, 6, 0.90f)
                .setCellKit("acacia")
                .build();
    }
}
