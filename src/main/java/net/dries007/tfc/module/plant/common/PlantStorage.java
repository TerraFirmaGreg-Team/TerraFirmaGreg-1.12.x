package net.dries007.tfc.module.plant.common;

import net.dries007.tfc.api.util.Pair;
import net.dries007.tfc.module.plant.api.type.PlantType;
import net.dries007.tfc.module.plant.api.variant.block.IPlantBlock;
import net.dries007.tfc.module.plant.api.variant.block.PlantEnumVariant;
import net.dries007.tfc.module.plant.api.variant.item.IPlantItem;
import net.dries007.tfc.module.plant.api.variant.item.PlantItemVariant;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

public class PlantStorage {

    public static final Map<Pair<PlantEnumVariant, PlantType>, IPlantBlock> PLANT_BLOCKS = new LinkedHashMap<>();
    public static final Map<Pair<PlantItemVariant, PlantType>, IPlantItem> PLANT_ITEMS = new LinkedHashMap<>();


    @Nonnull
    public static Block getPlantBlock(@Nonnull PlantEnumVariant variant, @Nonnull PlantType type) {
        var block = (Block) PLANT_BLOCKS.get(new Pair<>(variant, type));
        if (block != null) return block;
        throw new RuntimeException(String.format("Block is null: %s, %s", variant, type));
    }

    @Nonnull
    public static IPlantBlock getIPlantBlock(@Nonnull PlantEnumVariant variant, @Nonnull PlantType type) {
        var block = PLANT_BLOCKS.get(new Pair<>(variant, type));
        if (block != null) return block;
        throw new RuntimeException(String.format("Block is null: %s, %s", variant, type));
    }

    @Nonnull
    public static Item getRockItem(@Nonnull PlantItemVariant variant, @Nonnull PlantType type) {
        var item = (Item) PLANT_ITEMS.get(new Pair<>(variant, type));
        if (item != null) return item;
        throw new RuntimeException(String.format("Item is null: %s, %s", variant, type));
    }

    @Nonnull
    public static IPlantItem getIRockItem(@Nonnull PlantItemVariant variant, @Nonnull PlantType type) {
        var item = PLANT_ITEMS.get(new Pair<>(variant, type));
        if (item != null) return item;
        throw new RuntimeException(String.format("Item is null: %s, %s", variant, type));
    }
}
