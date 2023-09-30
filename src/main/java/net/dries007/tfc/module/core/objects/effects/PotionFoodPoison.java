package net.dries007.tfc.module.core.objects.effects;

import net.dries007.tfc.module.core.api.objects.effects.PotionBase;
import net.dries007.tfc.util.DamageSourcesTFC;
import net.minecraft.entity.EntityLivingBase;

import javax.annotation.Nonnull;

public class PotionFoodPoison extends PotionBase {
    public PotionFoodPoison() {
        super(true, 0x90EE90);
        setPotionName("effectsTFC.food_poison");
        setIconIndex(2, 0);
    }

    @Override
    public void performEffect(@Nonnull EntityLivingBase entity, int amplifier) {
        entity.attackEntityFrom(DamageSourcesTFC.FOOD_POISON, 1.0F * (amplifier + 1));
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 40 == 0; // 2 secs = damage
    }

    @Override
    public boolean isInstant() {
        return false;
    }
}
