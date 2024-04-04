package su.terrafirmagreg.modules.device.objects.items;

import lombok.Getter;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import su.terrafirmagreg.api.spi.item.ItemBase;

public class ItemSlingAmmo extends ItemBase {

	@Getter
	private final int type;
	private final String name;

	public ItemSlingAmmo(int type, String name) {
		this.type = type;
		this.name = name;
	}


	@Override
	public @NotNull Size getSize(@NotNull ItemStack itemStack) {
		return Size.SMALL;
	}

	@Override
	public @NotNull Weight getWeight(@NotNull ItemStack itemStack) {
		return Weight.LIGHT;
	}


	@Override
	public @NotNull String getName() {
		return "device/sling/ammo/" + name;
	}
}
