package su.terrafirmagreg.modules.animal.client.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import su.terrafirmagreg.api.util.ModUtils;
import su.terrafirmagreg.modules.animal.client.model.ModelAnimalHyena;
import su.terrafirmagreg.modules.animal.objects.entities.predator.EntityAnimalHyena;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public class RenderAnimalHyena extends RenderLiving<EntityAnimalHyena> {
	private static final ResourceLocation TEXTURE = ModUtils.getID("textures/entity/animal/predators/hyena.png");

	public RenderAnimalHyena(RenderManager renderManager) {
		super(renderManager, new ModelAnimalHyena(), 0.7F);
	}

	@Override
	public void doRender(@Nonnull EntityAnimalHyena hyena, double par2, double par4, double par6, float par8, float par9) {
		this.shadowSize = (float) (0.35f + (hyena.getPercentToAdulthood() * 0.35f));
		super.doRender(hyena, par2, par4, par6, par8, par9);
	}

	@Override
	protected float handleRotationFloat(EntityAnimalHyena par1EntityLiving, float par2) {
		return 1.0f;
	}

	@Override
	protected void preRenderCallback(EntityAnimalHyena hyenaTFC, float par2) {
		GlStateManager.scale(1.1f, 1.1f, 1.1f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityAnimalHyena entity) {
		return TEXTURE;
	}
}
