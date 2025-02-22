package su.terrafirmagreg.modules.animal.client.model;

import su.terrafirmagreg.api.util.MathUtils;
import su.terrafirmagreg.modules.animal.object.entity.EntityAnimalBase;
import su.terrafirmagreg.modules.animal.object.entity.livestock.EntityAnimalPig;

import net.minecraft.client.model.ModelQuadruped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)

public class ModelAnimalPig extends ModelQuadruped {

  private final ModelRenderer tusk1;
  private final ModelRenderer tusk2;
  private final ModelRenderer snout;

  public ModelAnimalPig() {
    super(6, 0);
    this.head.setTextureOffset(16, 16).addBox(-2.0F, 0.0F, -9.0F, 4, 3, 1, 0);
    this.childYOffset = 4.0F;
    tusk1 = new ModelRenderer(this, 32, 0);
    tusk1.addBox(0F, 0F, 0F, 1, 2, 1, 0F);
    tusk1.setRotationPoint(-3f, 0.5f, -9f);
    tusk1.rotateAngleX = MathUtils.PI / 12;

    tusk2 = new ModelRenderer(this, 32, 0);
    tusk2.addBox(0F, 0F, 0F, 1, 2, 1, 0F);
    tusk2.setRotationPoint(2f, 0.5f, -9f);
    tusk2.rotateAngleX = MathUtils.PI / 12;

    snout = new ModelRenderer(this, 0, 26);
    snout.addBox(-2.0F, 0.0F, -10.0F, 4, 3, 3, 0);
    snout.addChild(tusk1);
    snout.addChild(tusk2);
    this.head.addChild(snout);
  }

  @Override
  public void render(@NotNull Entity entity, float par2, float par3, float par4, float par5,
                     float par6, float par7) {
    EntityAnimalPig pig = ((EntityAnimalPig) entity);

    float percent = (float) pig.getPercentToAdulthood();
    float ageScale = 2.0F - percent;

    this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);
    if (pig.getGender() == EntityAnimalBase.Gender.MALE) {
      if (!pig.isChild()) {
        tusk1.isHidden = false;
        tusk2.isHidden = false;
      }
    }

    GlStateManager.pushMatrix();
    GlStateManager.scale(1 / ageScale, 1 / ageScale, 1 / ageScale);
    GlStateManager.translate(0.0F, 1.5f - (1.5f * percent), 0f);

    head.render(par7);
    body.render(par7);
    leg1.render(par7);
    leg2.render(par7);
    leg3.render(par7);
    leg4.render(par7);
    GlStateManager.popMatrix();
  }

  @Override
  public void setRotationAngles(float par1, float par2, float par3, float par4, float par5,
                                float par6, Entity par7Entity) {
    tusk1.isHidden = true;
    tusk2.isHidden = true;
    this.head.rotateAngleX = par5 / (180F / MathUtils.PI);
    this.head.rotateAngleY = par4 / (180F / MathUtils.PI);
    this.body.rotateAngleX = MathUtils.PI / 2F;
    this.leg1.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
    this.leg2.rotateAngleX = MathHelper.cos(par1 * 0.6662F + MathUtils.PI) * 1.4F * par2;
    this.leg3.rotateAngleX = MathHelper.cos(par1 * 0.6662F + MathUtils.PI) * 1.4F * par2;
    this.leg4.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * 1.4F * par2;
  }
}
