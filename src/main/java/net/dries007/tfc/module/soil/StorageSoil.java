package net.dries007.tfc.module.soil;

import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.dries007.tfc.module.soil.api.types.type.SoilType;
import net.dries007.tfc.module.soil.api.types.variant.block.ISoilBlock;
import net.dries007.tfc.module.soil.api.types.variant.block.SoilBlockVariant;
import net.dries007.tfc.module.soil.api.types.variant.item.ISoilItem;
import net.dries007.tfc.module.soil.api.types.variant.item.SoilItemVariant;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import su.terrafirmagreg.util.util.Pair;

import javax.annotation.Nonnull;
import java.util.Map;

public class StorageSoil {
    public static final Map<Pair<SoilBlockVariant, SoilType>, ISoilBlock> SOIL_BLOCKS = new Object2ObjectLinkedOpenHashMap<>();
    public static final Map<Pair<SoilItemVariant, SoilType>, ISoilItem> SOIL_ITEMS = new Object2ObjectLinkedOpenHashMap<>();

    @Nonnull
    public static Block getSoilBlock(@Nonnull SoilBlockVariant variant, @Nonnull SoilType type) {
        var block = (Block) SOIL_BLOCKS.get(new Pair<>(variant, type));
        if (block != null) return block;
        throw new RuntimeException(String.format("Block soil is null: %s, %s", variant, type));
    }

    @Nonnull
    public static ISoilBlock getISoilBlock(@Nonnull SoilBlockVariant variant, @Nonnull SoilType type) {
        var block = SOIL_BLOCKS.get(new Pair<>(variant, type));
        if (block != null) return block;
        throw new RuntimeException(String.format("Block soil is null: %s, %s", variant, type));
    }

    @Nonnull
    public static Item getSoilItem(@Nonnull SoilItemVariant variant, @Nonnull SoilType type) {
        var item = (Item) SOIL_ITEMS.get(new Pair<>(variant, type));
        if (item != null) return item;
        throw new RuntimeException(String.format("Item soil is null: %s, %s", variant, type));
    }

    @Nonnull
    public static ISoilItem getISoilItem(@Nonnull SoilItemVariant variant, @Nonnull SoilType type) {
        var item = SOIL_ITEMS.get(new Pair<>(variant, type));
        if (item != null) return item;
        throw new RuntimeException(String.format("Item soil is null: %s, %s", variant, type));
    }
}
