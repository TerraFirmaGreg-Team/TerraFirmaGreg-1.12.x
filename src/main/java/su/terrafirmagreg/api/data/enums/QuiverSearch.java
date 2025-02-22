package su.terrafirmagreg.api.data.enums;

public enum QuiverSearch {
  DISABLED("Disabled"),
  ARMOR("Armor"),
  HOTBAR("Hotbar"),
  INVENTORY("Inventory");

  private final String name;

  QuiverSearch(String name) {
    this.name = name;
  }

  /**
   * Shows this text in config instead of the enum name
   */
  @Override
  public String toString() {
    return name;
  }
}
