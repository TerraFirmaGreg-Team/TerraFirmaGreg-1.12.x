package net.dries007.tfc.module.agriculture.plugin.top;

import mcjty.theoneprobe.api.ITheOneProbe;
import net.dries007.tfc.module.agriculture.plugin.top.provider.BerryBushProvider;
import net.dries007.tfc.module.agriculture.plugin.top.provider.CropProvider;

import java.util.function.Function;

@SuppressWarnings("unused")
public class PluginTOP {

    public static class Callback implements Function<ITheOneProbe, Void> {

        @Override
        public Void apply(ITheOneProbe top) {

            top.registerProvider(new CropProvider());
            top.registerProvider(new BerryBushProvider());
            return null;
        }
    }

}
