package su.terrafirmagreg.modules.core.api.capabilities.damage;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DamageResistanceProvider implements IDamageResistanceCapability, ICapabilityProvider {
	private final float crushingModifier;
	private final float piercingModifier;
	private final float slashingModifier;

	public DamageResistanceProvider() {
		this(0, 0, 0);
	}

	public DamageResistanceProvider(float crushingModifier, float piercingModifier, float slashingModifier) {
		this.crushingModifier = crushingModifier;
		this.piercingModifier = piercingModifier;
		this.slashingModifier = slashingModifier;
	}

	@Override
	public float getCrushingModifier() {
		return crushingModifier;
	}

	@Override
	public float getPiercingModifier() {
		return piercingModifier;
	}

	@Override
	public float getSlashingModifier() {
		return slashingModifier;
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == DamageResistanceCapability.DAMAGE_RESISTANCE_CAPABILITY;
	}

	@SuppressWarnings("unchecked")
	@Nullable
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return hasCapability(capability, facing) ? (T) this : null;
	}
}
