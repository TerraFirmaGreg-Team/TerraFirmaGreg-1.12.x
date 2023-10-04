package net.dries007.tfc.module.metal.init;

import su.terrafirmagreg.util.registry.Registry;
import net.dries007.tfc.module.core.api.util.IHasModel;
import net.dries007.tfc.module.metal.api.types.variant.Item.IMetalItem;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.dries007.tfc.module.metal.StorageMetal.METAL_ITEMS;

public class ItemsMetal {

    public static void onRegister(Registry registry) {
        for (var item : METAL_ITEMS.values()) {
            registry.registerItem((Item) item, item.getName());
        }
    }

    @SideOnly(Side.CLIENT)
    public static void onClientRegister(Registry registry) {
        registry.registerClientModelRegistrationStrategy(() -> {
            METAL_ITEMS.values().forEach(IHasModel::onModelRegister);
        });
    }

    @SideOnly(Side.CLIENT)
    public static void onClientInitialization() {
        var itemColors = Minecraft.getMinecraft().getItemColors();


        itemColors.registerItemColorHandler((s, i) -> ((IMetalItem) s.getItem()).getMaterial().getMaterialRGB(),
                METAL_ITEMS.values()
                        .stream()
                        .map(s -> (Item) s)
                        .toArray(Item[]::new));
    }
}
