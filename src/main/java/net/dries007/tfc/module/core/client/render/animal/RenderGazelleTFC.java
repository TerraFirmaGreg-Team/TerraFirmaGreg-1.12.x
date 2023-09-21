package net.dries007.tfc.module.core.client.render.animal;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.module.core.client.model.animal.ModelGazelleTFC;
import net.dries007.tfc.module.core.common.objects.entity.animal.EntityGazelleTFC;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public class RenderGazelleTFC extends RenderLiving<EntityGazelleTFC> {
    private static final ResourceLocation TEXTURE = TerraFirmaCraft.getID("textures/entity/animal/huntable/gazelle.png");

    public RenderGazelleTFC(RenderManager manager) {
        super(manager, new ModelGazelleTFC(), 0.7F);
    }

    @Override
    protected float handleRotationFloat(EntityGazelleTFC gazelle, float par2) {
        return 1.0f;
    }

    @Override
    protected void preRenderCallback(EntityGazelleTFC gazelleTFC, float par2) {
        GlStateManager.scale(0.9f, 0.9f, 0.9f);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityGazelleTFC entity) {
        return TEXTURE;
    }
}
