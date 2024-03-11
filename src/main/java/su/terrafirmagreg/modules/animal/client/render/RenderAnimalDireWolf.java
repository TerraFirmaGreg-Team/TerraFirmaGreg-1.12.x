package su.terrafirmagreg.modules.animal.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import su.terrafirmagreg.api.util.ModUtils;
import su.terrafirmagreg.modules.animal.client.model.ModelAnimalDireWolf;
import su.terrafirmagreg.modules.animal.objects.entities.predator.EntityAnimalDireWolf;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public class RenderAnimalDireWolf extends RenderLiving<EntityAnimalDireWolf> {
	private static final ResourceLocation TEXTURE = ModUtils.getID("textures/entity/animal/predators/direwolf.png");

	public RenderAnimalDireWolf(RenderManager renderManager) {
		super(renderManager, new ModelAnimalDireWolf(), 0.7F);
	}

	@Override
	public void doRender(@Nonnull EntityAnimalDireWolf direwolf, double par2, double par4, double par6, float par8, float par9) {
		this.shadowSize = (float) (0.35f + (direwolf.getPercentToAdulthood() * 0.35f));
		super.doRender(direwolf, par2, par4, par6, par8, par9);
	}

	@Override
	protected float handleRotationFloat(EntityAnimalDireWolf par1EntityLiving, float par2) {
		return 1.0f;
	}

	@Override
	protected void preRenderCallback(EntityAnimalDireWolf direwolfTFC, float par2) {
		GlStateManager.scale(1.15f, 1.15f, 1.15f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAnimalDireWolf entity) {
		return TEXTURE;
	}
}
