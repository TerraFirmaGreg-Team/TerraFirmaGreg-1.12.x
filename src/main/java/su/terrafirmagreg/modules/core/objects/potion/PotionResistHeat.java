package su.terrafirmagreg.modules.core.objects.potion;

import su.terrafirmagreg.api.spi.effects.PotionBase;
import su.terrafirmagreg.modules.core.init.PotionsCore;

import net.minecraft.entity.EntityLivingBase;


import org.jetbrains.annotations.NotNull;

public class PotionResistHeat extends PotionBase {

    public PotionResistHeat() {
        super(false, 0xFFCD72);
        formatTexture("resist_heat");
        setBeneficial();
    }

    @Override
    public void performEffect(@NotNull EntityLivingBase entity, int amplifier) {
        removePotionCoreEffect(entity, PotionsCore.HYPERTHERMIA);
    }
}
