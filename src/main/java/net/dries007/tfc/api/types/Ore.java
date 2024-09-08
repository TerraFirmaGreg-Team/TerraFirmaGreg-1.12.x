package net.dries007.tfc.api.types;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;


import net.dries007.tfc.ConfigTFC;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.util.Helpers;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;

import static su.terrafirmagreg.data.Constants.MODID_TFC;

@GameRegistry.ObjectHolder(MODID_TFC)
public class Ore extends IForgeRegistryEntry.Impl<Ore> {

  public static final Ore LIMONITE = Helpers.getNull();
  public static final Ore MALACHITE = Helpers.getNull();
  public static final Ore HEMATITE = Helpers.getNull();

  @Getter
  private final boolean graded;
  private final Metal metal;
  private final boolean canMelt;
  @Getter

  private final double chunkChance;
  @Getter
  private final double panChance;

  /**
   * Creates a registry object for an ore type
   *
   * @param name        The registry name of the ore
   * @param metal       The metal, or null if it's a non-metal ore
   * @param canMelt     If the metal can be melted directly from the ore
   * @param chunkChance the chance a chunk contains this ore when gold panning.
   * @param panChance   the chance to drop this ore when gold panning
   */
  public Ore(ResourceLocation name, @Nullable Metal metal, boolean canMelt, double chunkChance, double panChance) {
    this.graded = (metal != null);
    this.metal = metal;
    this.canMelt = canMelt;
    this.chunkChance = chunkChance;
    this.panChance = panChance;

    setRegistryName(name);
  }

  public Ore(ResourceLocation name, @NotNull ResourceLocation metal, boolean canMelt, double chunkChance, double panChance) {
    this(name, TFCRegistries.METALS.getValue(metal), canMelt, chunkChance, panChance);
  }

  public Ore(ResourceLocation name, @NotNull ResourceLocation metal) {
    this(name, TFCRegistries.METALS.getValue(metal), true, 0, 0);
  }

  public Ore(ResourceLocation name, @NotNull ResourceLocation metal, boolean canMelt) {
    this(name, TFCRegistries.METALS.getValue(metal), canMelt, 0, 0);
  }

  public Ore(ResourceLocation name, @NotNull ResourceLocation metal, double chunkChance, double panChance) {
    this(name, TFCRegistries.METALS.getValue(metal), true, chunkChance, panChance);
  }

  public Ore(ResourceLocation name) {
    this(name, (Metal) null, false, 0, 0);
  }

  public boolean canPan() {
    return chunkChance > 0;
  }

  @Nullable
  public Metal getMetal() {
    return metal;
  }

  public boolean canMelt() {
    return canMelt;
  }

  @Override
  public String toString() {
    //noinspection ConstantConditions
    return getRegistryName().getPath();
  }

  public enum Grade implements IStringSerializable {
    NORMAL,
    POOR,
    RICH;

    private static final Grade[] VALUES = values();

    @NotNull
    public static Grade valueOf(int value) {
      return value < 0 || value >= VALUES.length ? NORMAL : VALUES[value];
    }

    public int getSmeltAmount() {
      return switch (this) {
        case POOR -> ConfigTFC.General.MISC.poorOreMetalAmount;
        case RICH -> ConfigTFC.General.MISC.richOreMetalAmount;
        default -> ConfigTFC.General.MISC.normalOreMetalAmount;
      };
    }

    @Override
    public String getName() {
      return this.name().toLowerCase();
    }

    public int getMeta() {
      return this.ordinal();
    }
  }
}
