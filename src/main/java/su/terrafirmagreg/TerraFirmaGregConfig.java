package su.terrafirmagreg;

import net.minecraftforge.common.config.Config;

import com.cleanroommc.configanytime.ConfigAnytime;

import static su.terrafirmagreg.Tags.MOD_ID;
import static su.terrafirmagreg.Tags.MOD_NAME;

@Config(modid = MOD_ID, name = MOD_NAME + "/modules")
public class TerraFirmaGregConfig {


  static {
    ConfigAnytime.register(TerraFirmaGregConfig.class);
  }
}
