package su.terrafirmagreg.api.data;

import su.terrafirmagreg.api.library.json.LowercaseEnumTypeAdapterFactory;
import su.terrafirmagreg.api.library.json.ResourceLocationJson;

import net.minecraft.util.ResourceLocation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class Reference {

  public static final Gson GSON = new GsonBuilder()
    .disableHtmlEscaping()
    .registerTypeAdapter(ResourceLocation.class, new ResourceLocationJson())
    .registerTypeAdapterFactory(new LowercaseEnumTypeAdapterFactory())
    .create();

  public static final String MODID_TFC = "tfc";
  public static final String MODID_TFCF = "net/dries007/tfcflorae";
  public static final String MODID_TFCTECH = "net/dries007/tfctech";
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

  private Reference() {
    throw new IllegalAccessError("Utility class");
  }

}
