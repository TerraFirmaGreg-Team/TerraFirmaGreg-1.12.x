package su.terrafirmagreg.modules.core.objects.effects;

import net.dries007.tfc.api.capability.food.IFoodStatsTFC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import su.terrafirmagreg.api.spi.effects.PotionBase;

import javax.annotation.Nonnull;

public class PotionThirst extends PotionBase {
	public PotionThirst() {
		super(true, 0x2c86d4);
		setPotionName("effectsTFC.thirst");
		setIconIndex(1, 0);
	}

	@Override
	public void performEffect(@Nonnull EntityLivingBase entity, int amplifier) {
		EntityPlayerMP player = null;
		IFoodStatsTFC foodStatsTFC = null;
		if (entity instanceof EntityPlayerMP) {
			player = (EntityPlayerMP) entity;
			if (player.getFoodStats() instanceof IFoodStatsTFC) {
				foodStatsTFC = (IFoodStatsTFC) player.getFoodStats();
			}
		}

		if (player != null && foodStatsTFC != null) {
			float thirst = foodStatsTFC.getThirst();

			foodStatsTFC.setThirst(thirst - 0.02F * (float) (amplifier + 1));
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return true;
	}

	@Override
	public boolean isInstant() {
		return false;
	}
}
