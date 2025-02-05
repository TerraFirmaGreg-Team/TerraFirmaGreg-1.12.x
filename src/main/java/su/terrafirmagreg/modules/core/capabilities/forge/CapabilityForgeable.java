package su.terrafirmagreg.modules.core.capabilities.forge;

import su.terrafirmagreg.api.util.ModUtils;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public final class CapabilityForgeable {

  public static final ResourceLocation KEY = ModUtils.resource("forge_capability");

  @CapabilityInject(ICapabilityForge.class)
  public static final Capability<ICapabilityForge> CAPABILITY = ModUtils.getNull();

  public static void register() {
    CapabilityManager.INSTANCE.register(ICapabilityForge.class, new CapabilityStorageForge(), CapabilityProviderForge::new);
  }


}
