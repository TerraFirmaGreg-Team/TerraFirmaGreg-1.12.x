package su.terrafirmagreg.modules.core.init;

import su.terrafirmagreg.api.network.IPacketRegistry;
import su.terrafirmagreg.modules.core.network.CSPacketGuiButton;
import su.terrafirmagreg.modules.core.network.SCPacketCalendarUpdate;
import su.terrafirmagreg.modules.core.network.SCPacketChunkData;
import su.terrafirmagreg.modules.core.network.SCPacketSimple;
import su.terrafirmagreg.modules.core.network.SCPacketTemperature;

import net.minecraftforge.fml.relauncher.Side;

public final class PacketsCore {

  public static void onRegister(IPacketRegistry registry) {

    registry.register(Side.SERVER, CSPacketGuiButton.class);

    registry.register(Side.CLIENT, SCPacketSimple.class);
    registry.register(Side.CLIENT, SCPacketTemperature.class);
    registry.register(Side.CLIENT, SCPacketChunkData.class);
    registry.register(Side.CLIENT, SCPacketCalendarUpdate.class);
  }
}
