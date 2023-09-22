package net.dries007.tfc.module.crop;

import com.codetaylor.mc.athenaeum.module.ModuleBase;
import com.codetaylor.mc.athenaeum.registry.Registry;
import net.dries007.tfc.module.core.common.objects.CreativeTabsTFC;
import net.dries007.tfc.module.crop.api.category.CropCategoryHandler;
import net.dries007.tfc.module.crop.api.type.CropTypeHandler;
import net.dries007.tfc.module.crop.api.variant.block.CropBlockVariantHandler;
import net.dries007.tfc.module.crop.api.variant.item.CropItemVariantHandler;
import net.dries007.tfc.module.crop.init.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static net.dries007.tfc.Tags.MOD_ID;
import static net.dries007.tfc.Tags.MOD_NAME;

public class CropModule extends ModuleBase {

    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME + "." + CropModule.class.getSimpleName());

    public CropModule() {
        super(0, MOD_ID);

        this.setRegistry(new Registry(MOD_ID, CreativeTabsTFC.CROP));
        this.enableAutoRegistry();

        //PACKET_SERVICE = this.enableNetwork();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onRegister(Registry registry) {
        CropCategoryHandler.init();
        CropTypeHandler.init();
        CropBlockVariantHandler.init();
        CropItemVariantHandler.init();

        BlockInitializer.onRegister(registry);
        ItemInitializer.onRegister(registry);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void onClientRegister(Registry registry) {
        BlockInitializer.onClientRegister(registry);
        ItemInitializer.onClientRegister(registry);
    }
}
