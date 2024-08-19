package su.terrafirmagreg.modules.world.objects.biome.overworld;

import su.terrafirmagreg.api.base.biome.BaseBiome;

import net.minecraftforge.common.BiomeDictionary;

public class BiomeFlatlands extends BaseBiome {

    public BiomeFlatlands() {
        super(new Settings("Flatlands")
                .guiColour(0x013240)
                .baseHeight(-1.7F)
                .heightVariation(-2.88F)
                .spawnBiome()
                .enableWorldGen());
    }

    @Override
    public int getBiomeWeight() {
        return 0;
    }

    @Override
    public BiomeDictionary.Type[] getTypes() {

        return new BiomeDictionary.Type[] {
                BiomeDictionary.Type.PLAINS,
                BiomeDictionary.Type.LUSH
        };
    }
}
