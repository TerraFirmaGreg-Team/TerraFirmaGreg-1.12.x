package net.dries007.tfc.api.capability.worldtracker;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.api.capability.DumbStorage;
import net.dries007.tfc.util.Helpers;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class CapabilityWorldTracker {
    public static final ResourceLocation KEY = TerraFirmaCraft.identifier("world_tracker");
    @CapabilityInject(WorldTracker.class)
    public static Capability<WorldTracker> CAPABILITY = Helpers.getNull();

    public static void preInit() {
        CapabilityManager.INSTANCE.register(WorldTracker.class, new DumbStorage<>(), WorldTracker::new);
    }
}
