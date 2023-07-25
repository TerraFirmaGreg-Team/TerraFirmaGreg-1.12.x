package net.dries007.tfc.compat.gregtech.material;

import gregtech.api.unification.material.Material;
import gregtech.api.unification.material.properties.PropertyKey;
import gregtech.api.unification.material.properties.ToolProperty;

import static gregtech.api.unification.material.Materials.*;
import static gregtech.api.unification.material.info.MaterialFlags.NO_UNIFICATION;
import static gregtech.api.unification.material.info.MaterialIconSet.METALLIC;
import static gregtech.api.util.GTUtility.gregtechId;
import static net.dries007.tfc.compat.gregtech.material.TFGMaterials.*;

public class TFGMaterialHandler {

    public static void init() {
        Unknown = new Material.Builder(32000, gregtechId("unknown"))
                .fluid()
                .color(0x2F2B27).iconSet(METALLIC)
                .fluidTemp(1250)
                .flags(NO_UNIFICATION)
                .build();

        PigIron = new Material.Builder(32001, gregtechId("pig_iron"))
                .ingot().fluid()
                .color(0x6A595C).iconSet(METALLIC)
                .fluidTemp(1535)
                .build();

        HighCarbonSteel = new Material.Builder(32002, gregtechId("high_carbon_steel"))
                .ingot().fluid()
                .color(0x5F5F5F).iconSet(METALLIC)
                .fluidTemp(1540)
                .build();

        HighCarbonBlackSteel = new Material.Builder(32003, gregtechId("high_carbon_black_steel"))
                .ingot().fluid()
                .color(0x111111).iconSet(METALLIC)
                .fluidTemp(1540)
                .build();

        HighCarbonRedSteel = new Material.Builder(32004, gregtechId("high_carbon_red_steel"))
                .ingot().fluid()
                .color(0x700503).iconSet(METALLIC)
                .fluidTemp(1540)
                .build();

        HighCarbonBlueSteel = new Material.Builder(32005, gregtechId("high_carbon_blue_steel"))
                .ingot().fluid()
                .color(0x2D5596).iconSet(METALLIC)
                .fluidTemp(1540)
                .build();

        WeakSteel = new Material.Builder(32006, gregtechId("weak_steel"))
                .ingot().fluid()
                .color(0x111111).iconSet(METALLIC)
                .fluidTemp(1540)
                .build();

        WeakBlueSteel = new Material.Builder(32007, gregtechId("weak_blue_steel"))
                .ingot().fluid()
                .color(0x2D5596).iconSet(METALLIC)
                .fluidTemp(1540)
                .build();

        WeakRedSteel = new Material.Builder(32008, gregtechId("weak_red_steel"))
                .ingot().fluid()
                .color(0x700503).iconSet(METALLIC)
                .fluidTemp(1540)
                .build();

        Catlinite = new Material.Builder(32101, gregtechId("catlinite"))
                .dust()
                .color(0xB46C62)
                .build();

        Chalk = new Material.Builder(32102, gregtechId("chalk"))
                .dust()
                .color(0xA4A39F)
                .build();

        Chert = new Material.Builder(32103, gregtechId("chert"))
                .dust()
                .color(0x7A6756)
                .build();

        Claystone = new Material.Builder(32104, gregtechId("claystone"))
                .dust()
                .color(0xAF9377)
                .build();

        Conglomerate = new Material.Builder(32105, gregtechId("conglomerate"))
                .dust()
                .color(0xA3977F)
                .build();

        Dacite = new Material.Builder(32106, gregtechId("dacite"))
                .dust()
                .color(0x979797)
                .build();

        Dolomite = new Material.Builder(32107, gregtechId("dolomite"))
                .dust()
                .color(0x515155)
                .build();

        Gabbro = new Material.Builder(32108, gregtechId("gabbro"))
                .dust()
                .color(0x7F8081)
                .build();

        Gneiss = new Material.Builder(32109, gregtechId("gneiss"))
                .dust()
                .color(0x6A6D60)
                .build();

        Limestone = new Material.Builder(32111, gregtechId("limestone"))
                .dust()
                .color(0xA09885)
                .build();

        Phyllite = new Material.Builder(32115, gregtechId("phyllite"))
                .dust()
                .color(0x706B69)
                .build();

        Rhyolite = new Material.Builder(32117, gregtechId("rhyolite"))
                .dust()
                .color(0x726D69)
                .build();


        Schist = new Material.Builder(32119, gregtechId("schist"))
                .dust()
                .color(0x6E735C)
                .build();

        Shale = new Material.Builder(32120, gregtechId("shale"))
                .dust()
                .color(0x686567)
                .build();

        Slate = new Material.Builder(32122, gregtechId("slate"))
                .dust()
                .color(0x989287)
                .build();

        Stone.setProperty(PropertyKey.TOOL, new ToolProperty(1.0f, 1f, 6, 1));
        Copper.setProperty(PropertyKey.TOOL, new ToolProperty(1.5f, 2f, 88, 2));
        BismuthBronze.setProperty(PropertyKey.TOOL, new ToolProperty(1.8f, 2f, 174, 2));
        Bismuth.setProperty(PropertyKey.TOOL, new ToolProperty(2.0f, 2f, 192, 2));
        BlackBronze.setProperty(PropertyKey.TOOL, new ToolProperty(2.2f, 2f, 212, 2));
        BlackSteel.setProperty(PropertyKey.TOOL, new ToolProperty(6.0f, 3f, 784, 3));

        Copper.setProperty(TFGPropertyKey.HEAT, new HeatProperty(1080, 0.35f));
        Bismuth.setProperty(TFGPropertyKey.HEAT, new HeatProperty(270, 0.35F));
        Brass.setProperty(TFGPropertyKey.HEAT, new HeatProperty(930, 0.35F));
        Lead.setProperty(TFGPropertyKey.HEAT, new HeatProperty(328, 0.22F));
        Nickel.setProperty(TFGPropertyKey.HEAT, new HeatProperty(1453, 0.48F));
        RoseGold.setProperty(TFGPropertyKey.HEAT, new HeatProperty(960, 0.35F));
        Silver.setProperty(TFGPropertyKey.HEAT, new HeatProperty(961, 0.48F));
        Tin.setProperty(TFGPropertyKey.HEAT, new HeatProperty(230, 0.14F));
        Zinc.setProperty(TFGPropertyKey.HEAT, new HeatProperty(420, 0.21F));
        SterlingSilver.setProperty(TFGPropertyKey.HEAT, new HeatProperty(900, 0.35F));
        Bronze.setProperty(TFGPropertyKey.HEAT, new HeatProperty(950, 0.35F));
        BlackBronze.setProperty(TFGPropertyKey.HEAT, new HeatProperty(1070, 0.35F));
        BismuthBronze.setProperty(TFGPropertyKey.HEAT, new HeatProperty(985, 0.35F));
        Gold.setProperty(TFGPropertyKey.HEAT, new HeatProperty(1060, 0.6F));
        PigIron.setProperty(TFGPropertyKey.HEAT, new HeatProperty(0.35F, 1535));
        HighCarbonSteel.setProperty(TFGPropertyKey.HEAT, new HeatProperty(0.35F, 1540));
        WroughtIron.setProperty(TFGPropertyKey.HEAT, new HeatProperty(1535, 0.35F));
        HighCarbonBlackSteel.setProperty(TFGPropertyKey.HEAT, new HeatProperty(0.35F, 1540));
        Steel.setProperty(TFGPropertyKey.HEAT, new HeatProperty(1540, 0.35F));
        WeakSteel.setProperty(TFGPropertyKey.HEAT, new HeatProperty(0.35F, 1540));
        Platinum.setProperty(TFGPropertyKey.HEAT, new HeatProperty(1730, 0.35F));
        BlackSteel.setProperty(TFGPropertyKey.HEAT, new HeatProperty(1485, 0.35F));
        WeakBlueSteel.setProperty(TFGPropertyKey.HEAT, new HeatProperty(0.35F, 1540));
        WeakRedSteel.setProperty(TFGPropertyKey.HEAT, new HeatProperty(0.35F, 1540));
        HighCarbonBlueSteel.setProperty(TFGPropertyKey.HEAT, new HeatProperty(0.35F, 1540));
        HighCarbonRedSteel.setProperty(TFGPropertyKey.HEAT, new HeatProperty(0.35F, 1540));
        BlueSteel.setProperty(TFGPropertyKey.HEAT, new HeatProperty(1540, 0.35F));
        RedSteel.setProperty(TFGPropertyKey.HEAT, new HeatProperty(1540, 0.35F));
        Unknown.setProperty(TFGPropertyKey.HEAT, new HeatProperty(1250, 0.3f));
        Iron.setProperty(TFGPropertyKey.HEAT, new HeatProperty(1535, 0.35F));
    }
}
