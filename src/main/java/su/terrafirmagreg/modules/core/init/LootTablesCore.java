package su.terrafirmagreg.modules.core.init;

import su.terrafirmagreg.api.registry.RegistryManager;
import su.terrafirmagreg.modules.core.object.loot.ApplyRequiredSkill;
import su.terrafirmagreg.modules.core.object.loot.ApplySimpleSkill;

public final class LootTablesCore {

  public static void onRegister(RegistryManager registry) {

    registry.lootFunction(new ApplySimpleSkill.Serializer());
    registry.lootFunction(new ApplyRequiredSkill.Serializer());
  }
}
