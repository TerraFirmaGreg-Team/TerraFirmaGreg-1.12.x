package su.terrafirmagreg.modules.device.client.gui;

import su.terrafirmagreg.api.util.ModUtils;
import su.terrafirmagreg.modules.device.objects.tiles.TECharcoalForge;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


import net.dries007.tfc.api.capability.heat.Heat;
import net.dries007.tfc.client.gui.GuiContainerTE;

@SideOnly(Side.CLIENT)
public class GuiCharcoalForge extends GuiContainerTE<TECharcoalForge> {

    private static final ResourceLocation BACKGROUND = ModUtils.id("textures/gui/container/charcoal_forge.png");

    public GuiCharcoalForge(Container container, InventoryPlayer playerInv, TECharcoalForge tile) {
        super(container, playerInv, tile, BACKGROUND);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTicks, mouseX, mouseY);

        // Draw the temperature indicator
        int temperature = (int) (51 * tile.getField(TECharcoalForge.FIELD_TEMPERATURE) / Heat.maxVisibleTemperature());
        if (temperature > 0) {
            if (temperature > 51) {
                temperature = 51;
            }
            drawTexturedModalRect(guiLeft + 8, guiTop + 66 - temperature, 176, 0, 15, 5);
        }
    }
}
