package net.dries007.tfc.client.gui;

import net.dries007.tfc.TerraFirmaCraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class GuiSalad extends GuiContainerTFC {
    private static final ResourceLocation BACKGROUND = TerraFirmaCraft.getID("textures/gui/salad.png");

    public GuiSalad(Container container, InventoryPlayer playerInv) {
        super(container, playerInv, BACKGROUND);
    }
}
