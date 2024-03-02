package su.terrafirmagreg.modules.rock.api.types.variant.item;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.item.Item;
import org.jetbrains.annotations.NotNull;
import su.terrafirmagreg.api.lib.Pair;
import su.terrafirmagreg.modules.rock.api.types.type.RockType;
import su.terrafirmagreg.modules.rock.data.ItemsRock;

import java.util.Set;
import java.util.function.BiFunction;

/**
 * Класс, представляющий тип блока породы.
 */
public class RockItemVariant implements Comparable<RockItemVariant> {

	private static final Set<RockItemVariant> ROCK_ITEM_VARIANTS = new ObjectOpenHashSet<>();

	@NotNull
	private final String name;

	private RockItemVariant(Builder builder) {
		this.name = builder.name;

		if (name.isEmpty())
			throw new RuntimeException(String.format("MetalItemVariant name must contain any character: [%s]", name));

		if (!ROCK_ITEM_VARIANTS.add(this))
			throw new RuntimeException(String.format("MetalItemVariant: [%s] already exists!", name));

		for (var type : RockType.getTypes()) {
			if (ItemsRock.ROCK_ITEMS.put(new Pair<>(this, type), builder.factory.apply(this, type)) != null)
				throw new RuntimeException(String.format("Duplicate registry detected: %s, %s", this, type));
		}
	}

	/**
	 * Возвращает набор всех типов блоков породы.
	 *
	 * @return Набор всех типов блоков породы.
	 */
	public static Set<RockItemVariant> getItemVariants() {
		return ROCK_ITEM_VARIANTS;
	}

	public Item getItem(RockType type) {
		return ItemsRock.getItem(this, type);
	}

	/**
	 * Возвращает строковое представление типа блока породы.
	 *
	 * @return Строковое представление типа блока породы.
	 */
	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(@NotNull RockItemVariant itemVariant) {
		return this.name.compareTo(itemVariant.toString());
	}


	public static class Builder {

		private final String name;
		private BiFunction<RockItemVariant, RockType, ? extends Item> factory;


		/**
		 * Создает экземпляр Builder с указанным именем.
		 *
		 * @param name Имя породы.
		 */
		public Builder(@NotNull String name) {
			this.name = name;
		}


		public Builder setFactory(BiFunction<RockItemVariant, RockType, ? extends Item> factory) {
			this.factory = factory;
			return this;
		}


		public RockItemVariant build() {
			return new RockItemVariant(this);
		}
	}
}
