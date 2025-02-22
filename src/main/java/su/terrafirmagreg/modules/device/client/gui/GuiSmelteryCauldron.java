package su.terrafirmagreg.modules.device.client.gui;

import su.terrafirmagreg.api.data.enums.Mods;
import su.terrafirmagreg.modules.core.capabilities.heat.spi.Heat;
import su.terrafirmagreg.modules.device.object.tile.TileSmelteryCauldron;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import net.dries007.tfc.client.gui.GuiContainerTE;
import net.dries007.tfctech.client.TechGuiHandler;

import java.util.List;

import static su.terrafirmagreg.api.data.enums.Mods.Names.TFCTECH;

public class GuiSmelteryCauldron extends GuiContainerTE<TileSmelteryCauldron> {

  private static final ResourceLocation CAULDRON_BACKGROUND = new ResourceLocation(TFCTECH, "textures/gui/smeltery_cauldron.png");

  public GuiSmelteryCauldron(Container container, InventoryPlayer playerInv, TileSmelteryCauldron tile) {
    super(container, playerInv, tile, CAULDRON_BACKGROUND);
  }

  @Override
  protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);
    // Draw elements

    //Draw the temperature bar
    TechGuiHandler.Drawing.drawTemperatureBar(mc, this, this.guiLeft + 26, this.guiTop + 13, tile.getField(0));

    // Draw tank
    TechGuiHandler.Drawing.drawTank(mc, this, this.guiLeft + 141, this.guiTop + 16, 4000, tile.getFluid());

    // If on fire
    if (tile.getField(0) > 0) {
      mc.getTextureManager().bindTexture(CAULDRON_BACKGROUND);
      for (int i = 0; i < 4; i++) {
        this.drawTexturedModalRect(guiLeft + 53 + (i * 18), guiTop + 57, 0, 166, 14, 14);
      }
    }
  }

  @Override
  protected void renderHoveredToolTip(int mouseX, int mouseY) {
    super.renderHoveredToolTip(mouseX, mouseY);
    int relX = mouseX - guiLeft;
    int relY = mouseY - guiTop;
    List<String> tooltip = TechGuiHandler.Drawing.getFluidTooltip(tile.getFluid(), relX, relY, 141, 16);
    if (tooltip != null) {
      String formatted = Heat.getTooltip(tile.getTemp());
      formatted += TextFormatting.WHITE;
      if (tile.isSolidified()) {
        formatted += I18n.format(Mods.Names.TFC + ".tooltip.solid");
      } else {
        formatted += I18n.format(Mods.Names.TFC + ".tooltip.liquid");
      }
      tooltip.add(formatted);
      this.drawHoveringText(tooltip, mouseX, mouseY, fontRenderer);
    }
  }
}
