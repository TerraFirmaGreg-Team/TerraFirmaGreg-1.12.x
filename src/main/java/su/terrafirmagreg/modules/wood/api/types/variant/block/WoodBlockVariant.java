package su.terrafirmagreg.modules.wood.api.types.variant.block;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import net.minecraft.block.Block;
import org.jetbrains.annotations.NotNull;
import su.terrafirmagreg.api.lib.Pair;
import su.terrafirmagreg.modules.wood.api.types.type.WoodType;
import su.terrafirmagreg.modules.wood.data.BlocksWood;

import java.util.Set;
import java.util.function.BiFunction;

import static net.dries007.tfc.api.util.FallingBlockManager.Specification;

/**
 * Класс CropItemVariant представляет вариант деревянного блока.
 */
public class WoodBlockVariant implements Comparable<WoodBlockVariant> {

	private static final Set<WoodBlockVariant> WOOD_BLOCK_VARIANTS = new ObjectOpenHashSet<>();

	@NotNull
	private final String name;
	@Getter
	private final Specification specification;
	@Getter
	private final int encouragement;
	@Getter
	private final int flammability;

	private WoodBlockVariant(Builder builder) {
		this.name = builder.name;
		this.specification = builder.specification;
		this.encouragement = builder.encouragement;
		this.flammability = builder.flammability;

		if (name.isEmpty())
			throw new RuntimeException(String.format("WoodBlockVariant name must contain any character: [%s]", name));

		if (!WOOD_BLOCK_VARIANTS.add(this))
			throw new RuntimeException(String.format("WoodBlockVariant: [%s] already exists!", name));

		for (var type : WoodType.getTypes()) {
			if (BlocksWood.WOOD_BLOCKS.put(new Pair<>(this, type), builder.factory.apply(this, type)) != null)
				throw new RuntimeException(String.format("Duplicate registry detected: %s, %s", this, type));
		}
	}

	/**
	 * Возвращает множество всех созданных вариантов деревянных блоков.
	 *
	 * @return множество вариантов деревянных блоков
	 */
	public static Set<WoodBlockVariant> getBlockVariants() {
		return WOOD_BLOCK_VARIANTS;
	}

	/**
	 * Возвращает строковое представление варианта деревянного блока (его имя).
	 *
	 * @return имя варианта деревянного блока
	 */
	@NotNull
	@Override
	public String toString() {
		return name;
	}

	@Override
	public int compareTo(@NotNull WoodBlockVariant blockVariant) {
		return this.name.compareTo(blockVariant.toString());
	}

	public static class Builder {

		private final String name;
		private BiFunction<WoodBlockVariant, WoodType, ? extends Block> factory;
		private Specification specification = null;
		private int encouragement;
		private int flammability;

		/**
		 * Создает экземпляр Builder с указанным именем.
		 *
		 * @param name Имя породы.
		 */
		public Builder(@NotNull String name) {
			this.name = name;
		}

		public Builder setFactory(BiFunction<WoodBlockVariant, WoodType, ? extends Block> factory) {
			this.factory = factory;
			return this;
		}

		public Builder setFireInfo(int encouragement, int flammability) {
			this.encouragement = encouragement;
			this.flammability = flammability;
			return this;
		}

		public Builder setFallingSpecification(Specification specification) {
			this.specification = specification;
			return this;
		}

		public WoodBlockVariant build() {
			return new WoodBlockVariant(this);
		}
	}
}
