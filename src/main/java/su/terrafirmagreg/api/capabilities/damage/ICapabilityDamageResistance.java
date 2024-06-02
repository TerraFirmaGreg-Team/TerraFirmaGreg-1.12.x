package su.terrafirmagreg.api.capabilities.damage;

public interface ICapabilityDamageResistance {

    default float getCrushingModifier() {
        return 0;
    }

    default float getPiercingModifier() {
        return 0;
    }

    default float getSlashingModifier() {
        return 0;
    }
}
