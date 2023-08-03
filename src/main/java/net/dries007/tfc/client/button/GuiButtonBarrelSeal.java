package net.dries007.tfc.client.button;

import static net.dries007.tfc.TerraFirmaCraft.MOD_ID;
import static net.dries007.tfc.client.gui.GuiBarrel.BARREL_BACKGROUND;

import javax.annotation.Nonnull;
import net.dries007.tfc.objects.te.TEBarrel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;


public class GuiButtonBarrelSeal extends GuiButtonTFC implements IButtonTooltip {
    private final TEBarrel tile;

    public GuiButtonBarrelSeal(TEBarrel tile, int buttonId, int guiTop, int guiLeft) {
        super(buttonId, guiLeft + 123, guiTop + 35, 20, 20, "");
        this.tile = tile;
    }

    @Override
    public String getTooltip() {
        return MOD_ID + ".tooltip." + (tile.isSealed() ? "barrel_unseal" : "barrel_seal");
    }

    @Override
    public boolean hasTooltip() {
        return true;
    }

    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            GlStateManager.color(1, 1, 1, 1);
            mc.getTextureManager().bindTexture(BARREL_BACKGROUND);
            hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            if (tile.isSealed()) {
                drawModalRectWithCustomSizedTexture(x, y, 236, 0, 20, 20, 256, 256);
            } else {
                drawModalRectWithCustomSizedTexture(x, y, 236, 20, 20, 20, 256, 256);
            }
            mouseDragged(mc, mouseX, mouseY);
        }
    }
}
