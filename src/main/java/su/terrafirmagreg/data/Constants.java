package su.terrafirmagreg.data;

import su.terrafirmagreg.Tags;
import su.terrafirmagreg.data.lib.json.LowercaseEnumTypeAdapterFactory;
import su.terrafirmagreg.data.lib.json.ResourceLocationJson;
import su.terrafirmagreg.modules.world.classic.objects.generator.vein.VeinType;

import net.minecraft.util.ResourceLocation;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dries007.tfc.objects.entity.animal.AnimalFood;
import net.dries007.tfc.util.json.AnimalFoodJson;
import net.dries007.tfc.util.json.VeinTypeJson;

public final class Constants {

    public static final String MOD_ID = Tags.MOD_ID;
    public static final String MOD_NAME = Tags.MOD_NAME;
    public static final String MOD_VERSION = Tags.MOD_VERSION;
    public static final String DEPENDENCIES = "required:forge@[14.23.5.2847,);after:jei;after:gregtech;after:top;after:tfc;";
    public static final String SERVER_PROXY = "su.terrafirmagreg.proxy.ServerProxy";
    public static final String CLIENT_PROXY = "su.terrafirmagreg.proxy.ClientProxy";
    public static final Gson GSON = new GsonBuilder()
            .disableHtmlEscaping()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocationJson())
            .registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
            .registerTypeAdapter(VeinType.class, new VeinTypeJson())
            .registerTypeAdapter(AnimalFood.class, new AnimalFoodJson())
            .create();

    public static final String MODID_TFC = "tfc";
    public static final String MODID_TFCF = "tfcflorae";
    public static final String MODID_TFCTECH = "tfctech";
    public static final String MODID_HORSEPOWER = "horsepower";
    public static final String MODID_CAFFEINEADDON = "ca";
    public static final String MODID_TIME4TFC = "time4tfc";
    public static final String MODID_OSA = "oversizediteminstoragearea";
    public static final String MODID_CELLARS = "cellars";
    public static final String MODID_TFCTHINGS = "tfcthings";
    public static final String MODID_HOTORNOT = "hotornot";
    public static final String MODID_FL = "firmalife";
    public static final String MODID_AGEDDRINKS = "aged_drinks";
    public static final String MODID_DDD = "deathdairydespair";
    public static final String MODID_FF = "floraefixes";
    public static final String MODID_TFCFARMING = "tfcfarming";
    public static final String MODID_TFCPASSINGDAYS = "tfcpassingdays";

    public static final String FLUIDLOGGED = "fluidlogged_api";

    private Constants() {
        throw new IllegalAccessError("Utility class");
    }

}
