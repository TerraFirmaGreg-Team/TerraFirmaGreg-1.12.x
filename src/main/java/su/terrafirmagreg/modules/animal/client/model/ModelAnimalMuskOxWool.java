package su.terrafirmagreg.modules.animal.client.model;

import su.terrafirmagreg.modules.animal.object.entity.livestock.EntityAnimalMuskOx;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * ModelAlpacaWoolTFC Created using Tabula 7.1.0
 */

@SideOnly(Side.CLIENT)

public class ModelAnimalMuskOxWool extends ModelBase {

  public ModelRenderer bodyShoulderQiviut;
  public ModelRenderer bodyHairQiviut;
  public ModelRenderer bodyMainQiviut;
  public ModelRenderer humpQiviut;

  public ModelAnimalMuskOxWool() {
    textureWidth = 128;
    textureHeight = 128;

    bodyMainQiviut = new ModelRenderer(this, 61, 48);
    bodyMainQiviut.setRotationPoint(0.0F, 10.0F, 0.0F);
    bodyMainQiviut.addBox(-4.5F, -8.5F, -8.5F, 9, 16, 23, 0.0F);
    bodyHairQiviut = new ModelRenderer(this, 55, 89);
    bodyHairQiviut.setRotationPoint(0.0F, 11.0F, -1.5F);
    bodyHairQiviut.addBox(-7.0F, -8.0F, -8.0F, 14, 16, 22, 0.0F);
    bodyShoulderQiviut = new ModelRenderer(this, 40, 38);
    bodyShoulderQiviut.setRotationPoint(0.0F, 9.0F, -7.0F);
    bodyShoulderQiviut.addBox(-5.5F, -8.5F, -5.5F, 11, 15, 11, 0.0F);
    humpQiviut = new ModelRenderer(this, 28, 110);
    humpQiviut.setRotationPoint(0.0F, 0.4F, -9.0F);
    humpQiviut.addBox(-2.0F, -1.2F, -2.2F, 4, 4, 9, 0.2F);
  }

  @Override
  public void render(Entity entity, float par2, float par3, float par4, float par5, float par6,
                     float par7) {
    this.setRotationAngles(par2, par3, par4, par5, par6, par7, entity);
    EntityAnimalMuskOx muskox = ((EntityAnimalMuskOx) entity);

    float percent = (float) muskox.getPercentToAdulthood();
    float ageScale = 2.0F - percent;

    if (percent < 0.5) {
      bodyHairQiviut.isHidden = true;
      bodyMainQiviut.isHidden = true;
      bodyShoulderQiviut.isHidden = true;
      humpQiviut.isHidden = true;
    } else {
      bodyHairQiviut.isHidden = false;
      bodyMainQiviut.isHidden = false;
      bodyShoulderQiviut.isHidden = false;
      humpQiviut.isHidden = false;
    }

    GlStateManager.pushMatrix();
    GlStateManager.scale(1 / ageScale, 1 / ageScale, 1 / ageScale);
    GlStateManager.translate(0.0F, 1.5f - (1.5f * percent), 0f);

    bodyHairQiviut.render(par7);
    bodyMainQiviut.render(par7);
    bodyShoulderQiviut.render(par7);
    humpQiviut.render(par7);
    GlStateManager.popMatrix();
  }
}
