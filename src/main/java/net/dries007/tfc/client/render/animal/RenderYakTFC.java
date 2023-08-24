package net.dries007.tfc.client.render.animal;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.client.model.animal.ModelYakTFC;
import net.dries007.tfc.common.objects.entity.animal.EntityAnimalTFC;
import net.dries007.tfc.common.objects.entity.animal.EntityYakTFC;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.ParametersAreNonnullByDefault;

@SideOnly(Side.CLIENT)
@ParametersAreNonnullByDefault
public class RenderYakTFC extends RenderAnimalTFC<EntityYakTFC> {
    private static final ResourceLocation TEXTURE_YOUNG = TerraFirmaCraft.identifier("textures/entity/animal/livestock/yak_young.png");
    private static final ResourceLocation TEXTURE_OLD = TerraFirmaCraft.identifier("textures/entity/animal/livestock/yak_young.png");

    public RenderYakTFC(RenderManager renderManager) {
        super(renderManager, new ModelYakTFC(), 0.7F, TEXTURE_YOUNG, TEXTURE_OLD);
    }

    protected void preRenderCallback(EntityYakTFC yakTFC, float par2) {
        if (yakTFC.getGender() == EntityAnimalTFC.Gender.MALE)
            GlStateManager.scale(1.2f, 1.2f, 1.2f);
        else {
            GlStateManager.scale(1.15f, 1.15f, 1.15f);
        }
    }
}
