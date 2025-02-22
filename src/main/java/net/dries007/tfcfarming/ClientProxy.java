package net.dries007.tfcfarming;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import net.dries007.tfc.api.types.ICrop;
import net.dries007.tfc.objects.blocks.BlockHangingPlanter;
import net.dries007.tfc.objects.blocks.BlockLargePlanter;
import net.dries007.tfc.objects.blocks.BlockQuadPlanter;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropDead;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropTFC;
import net.dries007.tfc.objects.blocks.blocktype.farmland.FarmlandTFCF;
import net.dries007.tfc.objects.blocks.stone.BlockFarmlandTFC;
import net.dries007.tfc.objects.items.ItemSeedsTFC;
import net.dries007.tfc.objects.items.metal.ItemMetalHoe;
import net.dries007.tfc.objects.items.rock.ItemRockHoe;
import net.dries007.tfc.objects.items.tools.ItemHoeTFCF;
import net.dries007.tfcfarming.network.CPacketRequestNutrientData;
import net.dries007.tfcfarming.network.PacketHandler;
import net.dries007.tfcfarming.network.SPacketNutrientDataResponse;

public class ClientProxy extends CommonProxy {

  boolean wasRendering = false;
  float time = 0;
  private SPacketNutrientDataResponse lastResponse = null;
  private long ticksSinceLastResponse = 0;

  private static void drawTooltipBox(int tooltipX, int tooltipY, int width, int height, int backgroundColor, int borderColorStart, int borderColorEnd) {
    GuiUtils.drawGradientRect(0, tooltipX - 3, tooltipY - 4, tooltipX + width + 3, tooltipY - 3, backgroundColor, backgroundColor);
    GuiUtils.drawGradientRect(0, tooltipX - 3, tooltipY + height + 3, tooltipX + width + 3, tooltipY + height + 4, backgroundColor, backgroundColor);
    GuiUtils.drawGradientRect(0, tooltipX - 3, tooltipY - 3, tooltipX + width + 3, tooltipY + height + 3, backgroundColor, backgroundColor);
    GuiUtils.drawGradientRect(0, tooltipX - 4, tooltipY - 3, tooltipX - 3, tooltipY + height + 3, backgroundColor, backgroundColor);
    GuiUtils.drawGradientRect(0, tooltipX + width + 3, tooltipY - 3, tooltipX + width + 4, tooltipY + height + 3, backgroundColor, backgroundColor);
    GuiUtils.drawGradientRect(0, tooltipX - 3, tooltipY - 3 + 1, tooltipX - 3 + 1, tooltipY + height + 3 - 1, borderColorStart, borderColorEnd);
    GuiUtils.drawGradientRect(0, tooltipX + width + 2, tooltipY - 3 + 1, tooltipX + width + 3, tooltipY + height + 3 - 1, borderColorStart, borderColorEnd);
    GuiUtils.drawGradientRect(0, tooltipX - 3, tooltipY - 3, tooltipX + width + 3, tooltipY - 3 + 1, borderColorStart, borderColorStart);
    GuiUtils.drawGradientRect(0, tooltipX - 3, tooltipY + height + 2, tooltipX + width + 3, tooltipY + height + 3, borderColorEnd, borderColorEnd);
  }

  @SubscribeEvent
  public void onToolTip(ItemTooltipEvent event) {

    if (TFCFarmingContent.isFertilizer(event.getItemStack())) {
      Item item = event.getItemStack().getItem();
      int prc = 0;
      int val = TFCFarmingContent.getFertilizerValue(event.getItemStack());
      prc = ((int) (val / 255.0f * 100));
      String line = "\u00A79Fertilizer value: " + TFCFarmingContent.getFertilizerClass(event.getItemStack()).name + "  \u00A7b" + prc + "%\u00A7r";
      event.getToolTip().add(line);
    }

    if ((event.getItemStack().getItem() instanceof ItemSeedsTFC seedsTFC)) {
      ICrop crop = seedsTFC.getCrop();
      if (GuiScreen.isShiftKeyDown()) {
        CropNutrients n = CropNutrients.getCropNValues(crop);
        event.getToolTip().add("");
        event.getToolTip().add("\u00A79Required nutrient: " + n.favouriteNutrient.name);
      }
    }

    if (event.getItemStack().getItem() instanceof ItemRockHoe || event.getItemStack().getItem() instanceof ItemMetalHoe) {
      if (GuiScreen.isShiftKeyDown()) {
        event.getToolTip().add(1, "\u00a73Sneak while looking at a block to see nutrient info");
        // TODO:                                                                     V config
        event.getToolTip().add(2, "\u00a73Farming skill must be at least Adept!");
      } else {
        event.getToolTip().add(1, "\u00a77Hold (Shift) for more information");
      }
    }

  }

  private void rectangleAt(int x, int y, int width, int height, int color) {
    GlStateManager.pushMatrix();
    GlStateManager.enableAlpha();
    GlStateManager.enableBlend();
    GlStateManager.resetColor();
    Gui.drawRect(x, y - height, x + width, y, color);
    GlStateManager.popMatrix();
  }

  @SubscribeEvent
  public void onClientTick(TickEvent.ClientTickEvent event) {
    ticksSinceLastResponse++;
  }

  public void setLastResponse(SPacketNutrientDataResponse response) {
    lastResponse = response;
    ticksSinceLastResponse = 0;
  }

  @SubscribeEvent
  public void onGuiIngame(RenderGameOverlayEvent.Post event) {
    if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) {return;}
    Minecraft mc = Minecraft.getMinecraft();
    EntityPlayer player = Minecraft.getMinecraft().player;
    if (
      player.isSneaking() &&
      mc.objectMouseOver != null &&
      mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK &&
      mc.objectMouseOver.getBlockPos() != null &&
      (
        mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemRockHoe ||
        mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemMetalHoe ||
        mc.player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemHoeTFCF
      )

    ) {

      BlockPos blockpos = mc.objectMouseOver.getBlockPos();
      Block b = mc.world.getBlockState(blockpos).getBlock();

      boolean isTFCCrop = b instanceof BlockCropTFC;
      boolean isTFCDeadCrop = b instanceof BlockCropDead;
      boolean isFarmlandTFCF = b instanceof FarmlandTFCF;
      boolean isFarmlandTFC = b instanceof BlockFarmlandTFC;
      boolean isPlanter = b instanceof BlockLargePlanter || (Config.hangingPlanters && b instanceof BlockHangingPlanter);

      if (!(isFarmlandTFC || isFarmlandTFCF || isPlanter || isTFCCrop || isTFCDeadCrop)) {return;}

      boolean invalidResponse = lastResponse == null ||
                                lastResponse.x != blockpos.getX() ||
                                lastResponse.z != blockpos.getZ() ||
                                !lastResponse.accepted ||
                                (isPlanter && lastResponse.y != blockpos.getY());

      if (invalidResponse || ticksSinceLastResponse > 20) {
        Minecraft.getMinecraft().addScheduledTask(new Runnable() {
          @Override
          public void run() {
            if (!isPlanter) {
              PacketHandler.INSTANCE.sendToServer(new CPacketRequestNutrientData(blockpos.getX(), blockpos.getZ()));
            } else {
              PacketHandler.INSTANCE.sendToServer(new CPacketRequestNutrientData(blockpos.getX(), blockpos.getY(), blockpos.getZ()));
            }
          }
        });
        if (!wasRendering || lastResponse == null) {
          return; // if were already rendering and the response is not null keep rendering
        }
        // to avoid flickering
        // if we were not to check if we were rendering before the animation might look scuffed
      }

      if (!wasRendering) {time = 0;}
      time += 4.0f / Minecraft.getDebugFPS();
      if (time > 1) {time = 1;}

      ScaledResolution sr = new ScaledResolution(mc);
      NutrientValues nutrientValues = new NutrientValues(lastResponse.n, lastResponse.p, lastResponse.k);

      boolean enoughNutrients = true;

      if (isTFCCrop) {
        BlockCropTFC plant = (BlockCropTFC) b;
        ICrop crop = plant.getCrop();
        CropNutrients nValues = CropNutrients.getCropNValues(crop);
        enoughNutrients = nutrientValues.getNutrient(nValues.favouriteNutrient) >= nValues.stepCost;
      }

      int x = sr.getScaledWidth() / 2 + 20;
      int y = sr.getScaledHeight() / 2 + 20;

      // TODO: planter warnings
      if (!enoughNutrients) {
        y = drawTextBox(mc, x, y, "\u00a7cLow nutrients!", "\u00a7c30% growth speed!");
      }

      if (isTFCDeadCrop) {
        y = drawTextBox(mc, x, y, "\u00a7cDead crop!", "\u00a7c" + (int) (Config.growthDead * 100) + "% nutrient recovery rate!");
      }

      if (isPlanter) {
        if (lastResponse.lowInPlanter) {
          if (b instanceof BlockQuadPlanter) {
            y = drawTextBox(mc, x, y, "\u00a7cOne or more plants have low nutrients!");
          } else {
            y = drawTextBox(mc, x, y, "\u00a7cLow nutrients!");
          }
        }
      }

      drawTooltipBox(x, y - 40, 96, 40, 0xF0100010, 0x505000FF, 0x5028007F);

      float n = nutrientValues.getNPKSet()[0] / 255.0f;
      float p = nutrientValues.getNPKSet()[1] / 255.0f;
      float k = nutrientValues.getNPKSet()[2] / 255.0f;

      rectangleAt(x, y, 10, (int) (40 * n * time), 0xff5555FF);
      rectangleAt(x + 15, y, 10, (int) (40 * p * time), 0xffAA00AA);
      rectangleAt(x + 30, y, 10, (int) (40 * k * time), 0xffFFAA00);

      mc.fontRenderer.drawStringWithShadow(String.format("\u00a79N: %6.2f%%", n * 100 * time), x + 45, y - 39, 0xffffffff);
      mc.fontRenderer.drawStringWithShadow(String.format("\u00a75P: %6.2f%%", p * 100 * time), x + 45, y - 24, 0xffffffff);
      mc.fontRenderer.drawStringWithShadow(String.format("\u00a76K: %6.2f%%", k * 100 * time), x + 45, y - 9, 0xffffffff);
      wasRendering = true;

    } else {
      wasRendering = false;
    }
  }

  private int drawTextBox(Minecraft mc, int x, int y, String... text) {
    y -= -2 + 10 * text.length;
    int maxL = 0;
    for (String s : text) {
      int l = mc.fontRenderer.getStringWidth(s);
      if (l > maxL) {maxL = l;}
    }
    drawTooltipBox(x, y + 13, maxL + 2, 10 * text.length, 0xF0100010, 0x505000FF, 0x5028007F);
    for (int i = 0; i < text.length; i++) {
      mc.fontRenderer.drawStringWithShadow(text[i], x + 2, y + 14 + 10 * i, 0xffffffff);
    }
    return y;
  }
}
