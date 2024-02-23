package su.terrafirmagreg.modules.rock.api.types.variant.block;

import gregtech.api.unification.ore.StoneType;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import lombok.Getter;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import org.jetbrains.annotations.NotNull;
import su.terrafirmagreg.api.util.Pair;
import su.terrafirmagreg.modules.rock.api.types.type.RockType;
import su.terrafirmagreg.modules.rock.init.BlocksRock;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;

import static net.dries007.tfc.api.util.FallingBlockManager.Specification;

/**
 * Класс, представляющий тип блока породы.
 */
public class RockBlockVariant {

	private static final Set<RockBlockVariant> ROCK_BLOCK_VARIANTS = new ObjectOpenHashSet<>();
	private static final AtomicInteger idCounter = new AtomicInteger(16);

	private final String name;
	@Getter
	private final float baseHardness;
	@Getter
	private final Specification specification;

	private RockBlockVariant(Builder builder) {
		this.name = builder.name;
		this.baseHardness = builder.baseHardness;
		this.specification = builder.specification;

		if (name.isEmpty())
			throw new RuntimeException(String.format("RockBlockVariant name must contain any character: [%s]", name));


		if (!ROCK_BLOCK_VARIANTS.add(this))
			throw new RuntimeException(String.format("RockBlockVariant: [%s] already exists!", name));


		for (var type : RockType.getTypes()) {
			if (BlocksRock.ROCK_BLOCKS.put(new Pair<>(this, type), builder.factory.apply(this, type)) != null)
				throw new RuntimeException(String.format("Duplicate registry detected: %s, %s", this, type));

			if (builder.hasStoneType)
				createStoneType(idCounter.getAndIncrement(), type, builder.name);
		}
	}

	/**
	 * Возвращает набор всех типов блоков породы.
	 *
	 * @return Набор всех типов блоков породы.
	 */
	public static Set<RockBlockVariant> getBlockVariants() {
		return ROCK_BLOCK_VARIANTS;
	}

	@Override
	public String toString() {
		return name;
	}

	public void createStoneType(int id, RockType type, String name) {
		new StoneType(id, "tfg_" + type + "_" + name,
				SoundType.STONE, type.getOrePrefix(), type.getMaterial(),
				() -> BlocksRock.getBlock(this, type).getDefaultState(),
				state -> state.getBlock() == BlocksRock.getBlock(this, type), false
		);
	}

	public static class Builder {

		private final String name;
		private float baseHardness;
		private BiFunction<RockBlockVariant, RockType, ? extends Block> factory;
		private Specification specification = null;
		private boolean hasStoneType = false;

		/**
		 * Создает экземпляр Builder с указанным именем.
		 *
		 * @param name Имя породы.
		 */
		public Builder(@NotNull String name) {
			this.name = name;
		}

		public Builder setBaseHardness(float baseHardness) {
			this.baseHardness = baseHardness;
			return this;
		}

		public Builder setFactory(BiFunction<RockBlockVariant, RockType, ? extends Block> factory) {
			this.factory = factory;
			return this;
		}

		public Builder setFallingSpecification(Specification specification) {
			this.specification = specification;
			return this;
		}

		public Builder setStoneType() {
			this.hasStoneType = true;
			return this;
		}

		public RockBlockVariant build() {
			return new RockBlockVariant(this);
		}
	}
}
