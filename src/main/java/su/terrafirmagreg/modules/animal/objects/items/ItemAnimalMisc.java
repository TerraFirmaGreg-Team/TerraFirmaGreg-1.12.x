package su.terrafirmagreg.modules.animal.objects.items;

import lombok.Getter;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.terrafirmagreg.api.spi.item.ItemBase;
import su.terrafirmagreg.api.util.OreDictUtils;

import javax.annotation.Nonnull;

public class ItemAnimalMisc extends ItemBase {
	private final Size size;
	private final Weight weight;

	@Getter
	private final String name;

	public ItemAnimalMisc(String name, Size size, Weight weight, Object... oreNameParts) {
		this(name, size, weight);
		if (oreNameParts != null) OreDictUtils.register(this, oreNameParts);
	}

	public ItemAnimalMisc(String name, Size size, Weight weight) {
		this.name = "animal/" + name;
		this.size = size;
		this.weight = weight;
	}

	@Nonnull
	@Override
	public @NotNull Size getSize(@Nonnull ItemStack stack) {
		return size != null ? size : super.getSize(stack);
	}

	@Nonnull
	@Override
	public @NotNull Weight getWeight(@Nonnull ItemStack stack) {
		return weight != null ? weight : super.getWeight(stack);
	}

	public static class Builder {
		private Size size;
		private Weight weight;
		private @NotNull Object[] oreNameParts;
		private final String name;

		public Builder(String name) {
			this.name = name;
		}

		public Builder setSize(Size size) {
			this.size = size;
			return this;
		}

		public Builder setWeight(Weight weight) {
			this.weight = weight;
			return this;
		}

		public Builder ore(@NotNull Object... oreNameParts) {
			this.oreNameParts = oreNameParts;
			return this;
		}

		public ItemAnimalMisc build() {
			return new ItemAnimalMisc(name, size, weight, oreNameParts);
		}
	}


}
