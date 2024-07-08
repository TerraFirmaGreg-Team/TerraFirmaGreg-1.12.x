package su.terrafirmagreg.modules.world.objects.biome.overworld;

import su.terrafirmagreg.api.spi.biome.BaseBiomeMesa;

import net.minecraftforge.common.BiomeDictionary;


import static su.terrafirmagreg.modules.world.init.BiomesWorld.MESA;

public class BiomeMesaBryce extends BaseBiomeMesa {

    public BiomeMesaBryce() {
        super(true, false, new Settings("Mesa Bryce")
                .guiColour(0x470285)
                .baseBiome(MESA)
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
                BiomeDictionary.Type.MESA,
                BiomeDictionary.Type.HOT,
                BiomeDictionary.Type.DRY,
                BiomeDictionary.Type.SPARSE,
                BiomeDictionary.Type.MOUNTAIN
        };
    }
}
