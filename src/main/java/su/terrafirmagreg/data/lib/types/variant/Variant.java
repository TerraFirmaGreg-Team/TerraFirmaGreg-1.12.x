package su.terrafirmagreg.data.lib.types.variant;

import su.terrafirmagreg.data.lib.types.type.Type;

import org.jetbrains.annotations.NotNull;

import lombok.Getter;

@Getter
public abstract class Variant<V, T extends Type<T>> implements Comparable<Variant<V, T>> {

  protected final String name;

  protected Variant(String name) {
    this.name = name;

    if (name.isEmpty()) {
      throw new RuntimeException(String.format("Variant name must contain any character: [%s]", name));
    }
  }

  @Override
  public String toString() {
    return name;
  }


  @Override
  public int compareTo(@NotNull Variant<V, T> type) {
    return this.name.compareTo(type.getName());
  }

  public abstract String getRegistryKey(T type);

  public abstract String getLocalizedName();
}
