package su.terrafirmagreg.modules;

import su.terrafirmagreg.api.module.Container;
import su.terrafirmagreg.api.module.IModuleContainer;

import net.minecraft.util.IStringSerializable;


import org.jetbrains.annotations.NotNull;

import lombok.Getter;

import static su.terrafirmagreg.api.data.Constants.MOD_ID;

@Getter
@Container
public enum Modules implements IStringSerializable, IModuleContainer {

    CORE(true),
    ROCK(true),
    SOIL(true),
    WOOD(true),
    METAL(false),
    ANIMAL(true),
    DEVICE(true),
    WORLD_GEN(true);

    // TODO переделать с enum на статические финальные поля

    private final boolean enabled;

    Modules(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getID() {
        return MOD_ID;
    }

    @Override
    public @NotNull String getName() {
        return this.name().toLowerCase();
    }
}
