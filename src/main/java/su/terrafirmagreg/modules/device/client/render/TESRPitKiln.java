package su.terrafirmagreg.modules.device.client.render;

import su.terrafirmagreg.api.base.client.tesr.spi.BaseTESR;
import su.terrafirmagreg.api.util.ModUtils;
import su.terrafirmagreg.modules.device.object.tile.TilePitKiln;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

@SideOnly(Side.CLIENT)
public class TESRPitKiln extends BaseTESR<TilePitKiln> {

  private static final ResourceLocation THATCH = ModUtils.resource("textures/blocks/thatch.png");
  private static final ResourceLocation BARK = ModUtils.resource("textures/blocks/wood/log/oak.png");
  private static final ModelStraw[] STRAW = new ModelStraw[TilePitKiln.STRAW_NEEDED];
  private static final int LOG_ROWS = 2;
  private static final int LOGS_PER_ROW = TilePitKiln.WOOD_NEEDED / LOG_ROWS;
  private static final ModelLog LOG = new ModelLog();
  private static final float SCALE = 1f / 16f;

  static {
    for (int i = 0; i < TilePitKiln.STRAW_NEEDED; i++) {
      STRAW[i] = new ModelStraw(i);
    }
  }

  @Override
  public void render(TilePitKiln tile, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
    RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
    World world = tile.getWorld();
    //noinspection ConstantConditions
    if (world == null) {
      return;
    }

    GlStateManager.pushMatrix();
    GlStateManager.pushAttrib();
    GlStateManager.disableLighting();
    GlStateManager.translate(x, y, z);

    GlStateManager.pushMatrix();
    IItemHandler cap = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    if (cap != null) {
      float timeD = (float) (360.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
      GlStateManager.translate(0.25, 0.25, 0.25);
      RenderHelper.enableStandardItemLighting();
      GlStateManager.pushAttrib();

      if (tile.holdingLargeItem()) {
        GlStateManager.scale(1.0F, 1.0F, 1.0F);
        ItemStack stack = cap.getStackInSlot(0);
        if (!stack.isEmpty()) {
          GlStateManager.pushMatrix();
          GlStateManager.translate(0.25, -0.001, 0.25);
          GlStateManager.rotate(timeD, 0, 1, 0);
          renderItem.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
          GlStateManager.popMatrix();
        }
      } else {
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        for (int i = 0; i < cap.getSlots(); i++) {
          ItemStack stack = cap.getStackInSlot(i);
          if (stack.isEmpty()) {
            continue;
          }
          GlStateManager.pushMatrix();
          GlStateManager.translate((i % 2 == 0 ? 1 : 0), -0.001, (i < 2 ? 1 : 0));
          GlStateManager.rotate(timeD, 0, 1, 0);
          renderItem.renderItem(stack, ItemCameraTransforms.TransformType.FIXED);
          GlStateManager.popMatrix();
        }
      }

      RenderHelper.disableStandardItemLighting();
      GlStateManager.popAttrib();
      GlStateManager.popMatrix();

      int straw = tile.getStrawCount();
      if (straw != 0) {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();

        bindTexture(THATCH);
        STRAW[straw - 1].render(null, 0, 0, 0, 0, 0, SCALE);

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
      }

      int logs = tile.getLogCount();
      if (logs != 0) {
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        GlStateManager.enableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.translate(0, 0.5d, 0);

        bindTexture(BARK);

        for (int row = 0; row < LOG_ROWS && logs > 0; row++) {
          GlStateManager.pushMatrix();
          GlStateManager.translate(0, row * 0.5d / (double) LOG_ROWS, 0);
          for (int i = 0; i < LOGS_PER_ROW && logs > 0; i++, logs--) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, i / (double) LOGS_PER_ROW);
            LOG.render(null, 0, 0, 0, 0, 0, SCALE);
            GlStateManager.popMatrix();
          }
          GlStateManager.popMatrix();
        }
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
      }

      GlStateManager.popAttrib();
      GlStateManager.popMatrix();
    }
  }

  private static class ModelStraw extends ModelBase {

    private final ModelRenderer strawRenderer;

    public ModelStraw(int height) {
      textureHeight = 16;
      textureWidth = 16;
      strawRenderer = new ModelRenderer(this, 0, 0);
      strawRenderer.addBox(0, 0, 0, 16, height + 1, 16);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      strawRenderer.render(scale);
    }
  }

  private static class ModelLog extends ModelBase {

    private final ModelRenderer logRenderer;

    public ModelLog() {
      textureHeight = 16;
      textureWidth = 16;
      logRenderer = new ModelRenderer(this, 0, 0);
      logRenderer.addBox(0, 0, 0, 16, 8 / LOG_ROWS, 16 / LOGS_PER_ROW);
    }

    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      logRenderer.render(scale);
    }
  }
}
