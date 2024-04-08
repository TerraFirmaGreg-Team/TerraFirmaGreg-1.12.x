package su.terrafirmagreg.modules.arboriculture;

import su.terrafirmagreg.api.module.ModuleBase;
import su.terrafirmagreg.api.spi.creativetab.CreativeTabBase;
import su.terrafirmagreg.modules.arboriculture.init.BlocksArboriculture;
import su.terrafirmagreg.modules.arboriculture.init.ItemsArboriculture;

import net.minecraft.creativetab.CreativeTabs;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.jetbrains.annotations.NotNull;

//@Module(moduleID = "Arboriculture", name = "TFG Module Arboriculture")
public final class ModuleArboriculture extends ModuleBase {

    public static final Logger LOGGER = LogManager.getLogger("Module Arboriculture");
    public static final CreativeTabs ARBORICULTURE_TAB = new CreativeTabBase("arboriculture", "arboriculture/log/pine");

    public ModuleArboriculture() {
        super(6);
        this.enableAutoRegistry(ARBORICULTURE_TAB);

    }

    @Override
    public void onRegister() {
        BlocksArboriculture.onRegister(registryManager);
        ItemsArboriculture.onRegister(registryManager);
    }

    @Override
    public @NotNull Logger getLogger() {
        return LOGGER;
    }
}
