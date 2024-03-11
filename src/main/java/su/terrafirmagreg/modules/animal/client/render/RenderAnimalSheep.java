package su.terrafirmagreg.modules.animal.client.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import su.terrafirmagreg.api.util.ModUtils;
import su.terrafirmagreg.modules.animal.client.model.ModelAnimalSheepBody;
import su.terrafirmagreg.modules.animal.objects.entities.livestock.EntityAnimalSheep;

import javax.annotation.ParametersAreNonnullByDefault;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public class RenderAnimalSheep extends RenderAnimal<EntityAnimalSheep> {
	private static final ResourceLocation SHEEP_YOUNG = ModUtils.getID("textures/entity/animal/livestock/sheep_young.png");
	private static final ResourceLocation SHEEP_OLD = ModUtils.getID("textures/entity/animal/livestock/sheep_old.png");

	public RenderAnimalSheep(RenderManager renderManager) {
		super(renderManager, new ModelAnimalSheepBody(), 0.7F, SHEEP_YOUNG, SHEEP_OLD);
		this.addLayer(new LayerSheepWool(this));
	}
}
