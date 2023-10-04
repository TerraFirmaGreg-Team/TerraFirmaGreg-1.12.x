package net.dries007.tfc.module.wood.api.types.variant.item;

import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import net.dries007.tfc.module.wood.StorageWood;
import net.dries007.tfc.module.wood.api.types.type.WoodType;
import su.terrafirmagreg.util.util.Pair;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * Класс CropItemVariant представляет вариант деревянного блока.
 */
public class WoodItemVariant {

    private static final Set<WoodItemVariant> WOOD_ITEM_VARIANTS = new ObjectLinkedOpenHashSet<>();

    @Nonnull
    private final String name;
    @Nonnull
    private final BiFunction<WoodItemVariant, WoodType, IWoodItem> factory;

    /**
     * Создает новый вариант деревянного блока с заданным именем и фабрикой.
     *
     * @param name    имя варианта деревянного блока
     * @param factory фабрика, создающая деревянный блок
     * @throws RuntimeException если имя варианта пустое или вариант с таким именем уже существует
     */
    public WoodItemVariant(@Nonnull String name, @Nonnull BiFunction<WoodItemVariant, WoodType, IWoodItem> factory) {
        this.name = name;
        this.factory = factory;

        if (name.isEmpty()) {
            throw new RuntimeException(String.format("CropItemVariant name must contain any character: [%s]", name));
        }

        if (!WOOD_ITEM_VARIANTS.add(this)) {
            throw new RuntimeException(String.format("CropItemVariant: [%s] already exists!", name));
        }

        for (var type : WoodType.getWoodTypes()) {
            if (StorageWood.WOOD_ITEMS.put(new Pair<>(this, type), this.create(type)) != null)
                throw new RuntimeException(String.format("Duplicate registry detected: %s, %s", this, type));
        }
    }

    /**
     * Возвращает множество всех созданных вариантов деревянных блоков.
     *
     * @return множество вариантов деревянных блоков
     */
    public static Set<WoodItemVariant> getWoodItemVariants() {
        return WOOD_ITEM_VARIANTS;
    }

    /**
     * Возвращает строковое представление варианта деревянного блока (его имя).
     *
     * @return имя варианта деревянного блока
     */
    @Nonnull
    @Override
    public String toString() {
        return name;
    }

    /**
     * Применяет вариант деревянного блока к фабрике, чтобы получить соответствующий деревянный блок.
     *
     * @param type тип дерева
     * @return объект IWoodBlock, созданный фабрикой
     */
    @Nonnull
    public IWoodItem create(@Nonnull WoodType type) {
        return factory.apply(this, type);
    }
}
