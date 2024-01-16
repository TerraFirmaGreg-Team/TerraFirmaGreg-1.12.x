package tfcflorae.objects.items.tools;

import com.google.common.collect.ImmutableSet;
import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.api.capability.damage.DamageType;
import net.dries007.tfc.api.capability.size.IItemSize;
import net.dries007.tfc.api.capability.size.Size;
import net.dries007.tfc.api.capability.size.Weight;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import org.jetbrains.annotations.NotNull;
import tfcflorae.util.OreDictionaryHelper;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemHammerTFCF extends ItemTool implements IItemSize {
	public final ToolMaterial material;

	public ItemHammerTFCF(ToolMaterial material, float AttackDamage, float AttackSpeed, int Durability, Object... oreNameParts) {
		super(AttackDamage, AttackSpeed, material, ImmutableSet.of());
		this.material = material;
		this.attackDamage = (AttackDamage);
		this.attackSpeed = (AttackSpeed);
		this.setMaxDamage(Durability);
		this.setHarvestLevel("hammer", material.getHarvestLevel());

		for (Object obj : oreNameParts) {
			if (obj instanceof Object[])
				OreDictionaryHelper.register(this, (Object[]) obj);
			else
				OreDictionaryHelper.register(this, obj);
		}
		OreDictionaryHelper.registerDamageType(this, DamageType.CRUSHING);
	}

	@Nonnull
	@Override
	public @NotNull Size getSize(ItemStack stack) {
		return Size.LARGE;  // Stored only in chests
	}

	@Nonnull
	@Override
	public @NotNull Weight getWeight(ItemStack stack) {
		return Weight.MEDIUM;
	}

	@Override
	public boolean canStack(ItemStack stack) {
		return false;
	}
}
