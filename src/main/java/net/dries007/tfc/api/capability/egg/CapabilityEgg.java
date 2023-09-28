package net.dries007.tfc.api.capability.egg;

import net.dries007.tfc.TerraFirmaGreg;
import net.dries007.tfc.api.capability.DumbStorage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityEgg {
    public static final ResourceLocation KEY = TerraFirmaGreg.getID("egg");
    @CapabilityInject(IEgg.class)
    public static Capability<IEgg> CAPABILITY;

    public static void preInit() {
        CapabilityManager.INSTANCE.register(IEgg.class, new DumbStorage<>(), EggHandler::new);
    }
}
