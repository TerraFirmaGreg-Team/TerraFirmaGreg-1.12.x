package su.terrafirmagreg.modules.soil.init;

import su.terrafirmagreg.api.registry.RegistryManager;
import su.terrafirmagreg.data.lib.Pair;
import su.terrafirmagreg.modules.soil.api.types.type.SoilType;
import su.terrafirmagreg.modules.soil.api.types.variant.block.SoilBlockVariant;
import su.terrafirmagreg.modules.soil.object.block.BlockSoilDirt;
import su.terrafirmagreg.modules.soil.object.block.BlockSoilFarmland;
import su.terrafirmagreg.modules.soil.object.block.BlockSoilGrass;
import su.terrafirmagreg.modules.soil.object.block.BlockSoilGrassPath;
import su.terrafirmagreg.modules.soil.object.block.BlockSoilMud;
import su.terrafirmagreg.modules.soil.object.block.BlockSoilMudBricks;
import su.terrafirmagreg.modules.soil.object.block.BlockSoilMudSlab;
import su.terrafirmagreg.modules.soil.object.block.BlockSoilMudStairs;
import su.terrafirmagreg.modules.soil.object.block.BlockSoilMudWall;
import su.terrafirmagreg.modules.soil.object.block.BlockSoilMycelium;
import su.terrafirmagreg.modules.soil.object.block.BlockSoilPeat;
import su.terrafirmagreg.modules.soil.object.block.BlockSoilPeatGrass;
import su.terrafirmagreg.modules.soil.object.block.BlockSoilPodzol;
import su.terrafirmagreg.modules.soil.object.block.BlockSoilRootedDirt;

import net.minecraft.block.Block;


import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;

import java.util.Map;
import java.util.Set;

import static su.terrafirmagreg.modules.core.feature.falling.FallingBlockManager.Specification.VERTICAL_AND_HORIZONTAL;
import static su.terrafirmagreg.modules.core.feature.falling.FallingBlockManager.Specification.VERTICAL_ONLY_SOIL;

public final class BlocksSoil {

  public static final Map<Pair<SoilBlockVariant, SoilType>, Block> SOIL_BLOCKS = new Object2ObjectLinkedOpenHashMap<>();

  public static final Map<Pair<SoilBlockVariant, Set<SoilType>>, Block> SOIL_BLOCKS2 = new Object2ObjectLinkedOpenHashMap<>();

  public static SoilBlockVariant GRASS;
  public static SoilBlockVariant DRY_GRASS;
  public static SoilBlockVariant SPARSE_GRASS;

  public static SoilBlockVariant PODZOL;
  public static SoilBlockVariant MYCELIUM;

  public static SoilBlockVariant COARSE_DIRT;
  public static SoilBlockVariant ROOTED_DIRT;
  public static SoilBlockVariant DIRT;

  public static SoilBlockVariant FARMLAND;
  public static SoilBlockVariant GRASS_PATH;

  public static SoilBlockVariant MUD;

  public static SoilBlockVariant MUD_BRICKS;
  public static SoilBlockVariant MUD_BRICKS_STAIRS;
  public static SoilBlockVariant MUD_BRICKS_SLAB_DOUBLE;
  public static SoilBlockVariant MUD_BRICKS_SLAB;
  public static SoilBlockVariant MUD_BRICKS_WALL;

  public static SoilBlockVariant MUD_BRICKS_MOSSY;
  public static SoilBlockVariant MUD_BRICKS_MOSSY_STAIRS;
  public static SoilBlockVariant MUD_BRICKS_MOSSY_SLAB_DOUBLE;
  public static SoilBlockVariant MUD_BRICKS_MOSSY_SLAB;
  public static SoilBlockVariant MUD_BRICKS_MOSSY_WALL;

  public static SoilBlockVariant DRYING_BRICKS;

  public static BlockSoilPeatGrass PEAT_GRASS;
  public static BlockSoilPeat PEAT;

  public static void onRegister(RegistryManager registry) {

    GRASS = SoilBlockVariant
            .builder("grass")
            .setFactory(BlockSoilGrass::new)
            .setFallingSpecification(VERTICAL_AND_HORIZONTAL)
            .build();

    DRY_GRASS = SoilBlockVariant
            .builder("dry_grass")
            .setFactory(BlockSoilGrass::new)
            .setFallingSpecification(VERTICAL_AND_HORIZONTAL)
            .build();

    SPARSE_GRASS = SoilBlockVariant
            .builder("sparse_grass")
            .setFactory(BlockSoilGrass::new)
            .setFallingSpecification(VERTICAL_AND_HORIZONTAL)
            .build();

    PODZOL = SoilBlockVariant
            .builder("podzol")
            .setFactory(BlockSoilPodzol::new)
            .setFallingSpecification(VERTICAL_AND_HORIZONTAL)
            .build();

    MYCELIUM = SoilBlockVariant
            .builder("mycelium")
            .setFactory(BlockSoilMycelium::new)
            .setFallingSpecification(VERTICAL_AND_HORIZONTAL)
            .build();

    DIRT = SoilBlockVariant
            .builder("dirt")
            .setFactory(BlockSoilDirt::new)
            .setFallingSpecification(VERTICAL_AND_HORIZONTAL)
            .build();

    COARSE_DIRT = SoilBlockVariant
            .builder("coarse_dirt")
            .setFactory(BlockSoilDirt::new)
            .setFallingSpecification(VERTICAL_AND_HORIZONTAL)
            .build();

    ROOTED_DIRT = SoilBlockVariant
            .builder("rooted_dirt")
            .setFactory(BlockSoilRootedDirt::new)
            .setFallingSpecification(VERTICAL_AND_HORIZONTAL)
            .build();

    FARMLAND = SoilBlockVariant
            .builder("farmland")
            .setFactory(BlockSoilFarmland::new)
            .setFallingSpecification(VERTICAL_ONLY_SOIL)
            .build();

    GRASS_PATH = SoilBlockVariant
            .builder("grass_path")
            .setFactory(BlockSoilGrassPath::new)
            .setFallingSpecification(VERTICAL_ONLY_SOIL)
            .build();

    MUD = SoilBlockVariant
            .builder("mud")
            .setFactory(BlockSoilMud::new)
            .setFallingSpecification(VERTICAL_AND_HORIZONTAL)
            .build();

    MUD_BRICKS = SoilBlockVariant
            .builder("mud_bricks")
            .setFactory(BlockSoilMudBricks::new)
            .build();

    MUD_BRICKS_STAIRS = SoilBlockVariant
            .builder("mud_bricks/stairs")
            .setFactory((v, t) -> new BlockSoilMudStairs(MUD_BRICKS, v, t))
            .build();

    MUD_BRICKS_SLAB_DOUBLE = SoilBlockVariant
            .builder("mud_bricks/slab_double")
            .setFactory((v, t) -> new BlockSoilMudSlab.Double(MUD_BRICKS, v, t))
            .build();

    MUD_BRICKS_SLAB = SoilBlockVariant
            .builder("mud_bricks/slab")
            .setFactory((v, t) -> new BlockSoilMudSlab.Half(MUD_BRICKS, MUD_BRICKS_SLAB_DOUBLE, v, t))
            .build();

    MUD_BRICKS_WALL = SoilBlockVariant
            .builder("mud_bricks/wall")
            .setFactory((v, t) -> new BlockSoilMudWall(MUD_BRICKS, v, t))
            .build();

    MUD_BRICKS_MOSSY = SoilBlockVariant
            .builder("mud_bricks_mossy")
            .setFactory(BlockSoilMudBricks::new)
            .setFallingSpecification(VERTICAL_ONLY_SOIL)
            .build();

    MUD_BRICKS_MOSSY_STAIRS = SoilBlockVariant
            .builder("mud_bricks_mossy/stairs")
            .setFactory((v, t) -> new BlockSoilMudStairs(MUD_BRICKS_MOSSY, v, t))
            .build();

    MUD_BRICKS_MOSSY_SLAB_DOUBLE = SoilBlockVariant
            .builder("mud_bricks_mossy/slab_double")
            .setFactory((v, t) -> new BlockSoilMudSlab.Double(MUD_BRICKS_MOSSY, v, t))
            .build();

    MUD_BRICKS_MOSSY_SLAB = SoilBlockVariant
            .builder("mud_bricks_mossy/slab")
            .setFactory(
                    (v, t) -> new BlockSoilMudSlab.Half(MUD_BRICKS_MOSSY, MUD_BRICKS_MOSSY_SLAB_DOUBLE, v,
                            t))
            .build();

    MUD_BRICKS_MOSSY_WALL = SoilBlockVariant
            .builder("mud_bricks_mossy/wall")
            .setFactory((v, t) -> new BlockSoilMudWall(MUD_BRICKS_MOSSY, v, t))
            .build();

    PEAT_GRASS = registry.block(new BlockSoilPeatGrass());
    PEAT = registry.block(new BlockSoilPeat());

    registry.blocks(SOIL_BLOCKS.values());

  }
}
