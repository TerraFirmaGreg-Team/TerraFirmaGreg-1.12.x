package su.terrafirmagreg.modules.core.object.potion;

import su.terrafirmagreg.api.base.potion.BasePotion;

public class PotionHyperthermia extends BasePotion {

  public PotionHyperthermia() {
    super(false, 0xFFC85C);
    formatTexture("hyperthermia");
  }
}
