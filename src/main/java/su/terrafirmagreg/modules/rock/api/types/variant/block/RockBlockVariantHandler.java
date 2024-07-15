package su.terrafirmagreg.modules.rock.api.types.variant.block;

import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockAnvil;
import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockBricks;
import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockButton;
import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockCobble;
import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockGravel;
import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockMagma;
import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockMossy;
import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockPressurePlate;
import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockRaw;
import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockSand;
import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockSlab;
import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockSmooth;
import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockSpeleothem;
import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockStairs;
import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockStandGem;
import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockSurface;
import su.terrafirmagreg.modules.rock.objects.blocks.BlockRockWall;


import static net.dries007.tfc.api.util.FallingBlockManager.Specification.*;
import static su.terrafirmagreg.modules.rock.api.types.variant.block.RockBlockVariants.*;

public final class RockBlockVariantHandler {
    
    public static void init() {

        COBBLE = RockBlockVariant
                .builder("cobble")
                .setBaseHardness(6f)
                .setFactory(BlockRockCobble::new)
                .setFallingSpecification(VERTICAL_AND_HORIZONTAL_ROCK)
                .build();

        COBBLE_STAIRS = RockBlockVariant
                .builder("cobble/stairs")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockStairs(COBBLE, v, t))
                .build();

        COBBLE_SLAB_DOUBLE = RockBlockVariant
                .builder("cobble/slab_double")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockSlab.Double(COBBLE, v, t))
                .build();

        COBBLE_SLAB = RockBlockVariant
                .builder("cobble/slab")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockSlab.Half(COBBLE, COBBLE_SLAB_DOUBLE, v, t))
                .build();

        COBBLE_WALL = RockBlockVariant
                .builder("cobble/wall")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockWall(COBBLE, v, t))
                .build();

        MOSSY_COBBLE = RockBlockVariant
                .builder("cobble_mossy")
                .setBaseHardness(6f)
                .setFactory(BlockRockMossy::new)
                .setFallingSpecification(VERTICAL_AND_HORIZONTAL_ROCK)
                .build();

        MOSSY_COBBLE_STAIRS = RockBlockVariant
                .builder("cobble_mossy/stairs")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockStairs(MOSSY_COBBLE, v, t))
                .build();

        MOSSY_COBBLE_SLAB_DOUBLE = RockBlockVariant
                .builder("cobble_mossy/slab_double")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockSlab.Double(MOSSY_COBBLE, v, t))
                .build();

        MOSSY_COBBLE_SLAB = RockBlockVariant
                .builder("cobble_mossy/slab")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockSlab.Half(MOSSY_COBBLE, MOSSY_COBBLE_SLAB_DOUBLE, v, t))
                .build();

        MOSSY_COBBLE_WALL = RockBlockVariant
                .builder("cobble_mossy/wall")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockWall(MOSSY_COBBLE, v, t))
                .build();

        RAW = RockBlockVariant
                .builder("raw")
                .setBaseHardness(6f)
                .setFactory(BlockRockRaw::new)
                .setStoneType()
                .setFallingSpecification(COLLAPSABLE_ROCK)
                .build();

        RAW_STAIRS = RockBlockVariant
                .builder("raw/stairs")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockStairs(RAW, v, t))
                .build();

        RAW_SLAB_DOUBLE = RockBlockVariant
                .builder("raw/slab_double")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockSlab.Double(RAW, v, t))
                .build();

        RAW_SLAB = RockBlockVariant
                .builder("raw/slab")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockSlab.Half(RAW, RAW_SLAB_DOUBLE, v, t))
                .build();

        RAW_WALL = RockBlockVariant
                .builder("raw/wall")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockWall(RAW, v, t))
                .build();

        MOSSY_RAW = RockBlockVariant
                .builder("raw_mossy")
                .setBaseHardness(6f)
                .setFactory(BlockRockRaw::new)
                .setStoneType()
                .setFallingSpecification(COLLAPSABLE_ROCK)
                .build();

        MOSSY_RAW_STAIRS = RockBlockVariant
                .builder("raw_mossy/stairs")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockStairs(MOSSY_RAW, v, t))
                .build();

        MOSSY_RAW_SLAB_DOUBLE = RockBlockVariant
                .builder("raw_mossy/slab_double")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockSlab.Double(MOSSY_RAW, v, t))
                .build();

        MOSSY_RAW_SLAB = RockBlockVariant
                .builder("raw_mossy/slab")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockSlab.Half(MOSSY_RAW, MOSSY_RAW_SLAB_DOUBLE, v, t))
                .build();

        MOSSY_RAW_WALL = RockBlockVariant
                .builder("raw_mossy/wall")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockWall(MOSSY_RAW, v, t))
                .build();

        BRICKS = RockBlockVariant
                .builder("bricks")
                .setBaseHardness(6f)
                .setFactory(BlockRockBricks::new)
                .build();

        BRICKS_STAIRS = RockBlockVariant
                .builder("bricks/stairs")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockStairs(BRICKS, v, t))
                .build();

        BRICKS_SLAB_DOUBLE = RockBlockVariant
                .builder("bricks/slab_double")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockSlab.Double(BRICKS, v, t))
                .build();

        BRICKS_SLAB = RockBlockVariant
                .builder("bricks/slab")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockSlab.Half(BRICKS, BRICKS_SLAB_DOUBLE, v, t))
                .build();

        BRICKS_WALL = RockBlockVariant
                .builder("bricks/wall")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockWall(BRICKS, v, t))
                .build();

        BRICKS_MOSSY = RockBlockVariant
                .builder("bricks_mossy")
                .setBaseHardness(6f)
                .setFactory(BlockRockMossy::new)
                .build();

        BRICKS_MOSSY_STAIRS = RockBlockVariant
                .builder("bricks_mossy/stairs")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockStairs(BRICKS_MOSSY, v, t))
                .build();

        BRICKS_MOSSY_SLAB_DOUBLE = RockBlockVariant
                .builder("bricks_mossy/slab_double")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockSlab.Double(BRICKS_MOSSY, v, t))
                .build();

        BRICKS_MOSSY_SLAB = RockBlockVariant
                .builder("bricks_mossy/slab")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockSlab.Half(BRICKS_MOSSY, BRICKS_MOSSY_SLAB_DOUBLE, v, t))
                .build();

        BRICKS_MOSSY_WALL = RockBlockVariant
                .builder("bricks_mossy/wall")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockWall(BRICKS_MOSSY, v, t))
                .build();

        SMOOTH = RockBlockVariant
                .builder("smooth")
                .setBaseHardness(6f)
                .setFactory(BlockRockSmooth::new)
                .build();

        SMOOTH_STAIRS = RockBlockVariant
                .builder("smooth/stairs")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockStairs(SMOOTH, v, t))
                .build();

        SMOOTH_SLAB_DOUBLE = RockBlockVariant
                .builder("smooth/slab_double")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockSlab.Double(SMOOTH, v, t))
                .build();

        SMOOTH_SLAB = RockBlockVariant
                .builder("smooth/slab")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockSlab.Half(SMOOTH, SMOOTH_SLAB_DOUBLE, v, t))
                .build();

        SMOOTH_WALL = RockBlockVariant
                .builder("smooth/wall")
                .setBaseHardness(6f)
                .setFactory((v, t) -> new BlockRockWall(SMOOTH, v, t))
                .build();

        BRICKS_CRACKED = RockBlockVariant
                .builder("bricks_cracked")
                .setBaseHardness(6f)
                .setFactory(BlockRockBricks::new)
                .build();

        CHISELED = RockBlockVariant
                .builder("chiseled")
                .setBaseHardness(6f)
                .setFactory(BlockRockBricks::new)
                .build();

        GRAVEL = RockBlockVariant
                .builder("gravel")
                .setBaseHardness(6f)
                .setFactory(BlockRockGravel::new)
                .setFallingSpecification(VERTICAL_AND_HORIZONTAL)
                .build();

        SAND = RockBlockVariant
                .builder("sand")
                .setBaseHardness(6f)
                .setFactory(BlockRockSand::new)
                .setFallingSpecification(VERTICAL_AND_HORIZONTAL)
                .build();

        SURFACE = RockBlockVariant
                .builder("surface")
                .setBaseHardness(6f)
                .setFactory(BlockRockSurface::new)
                .setFallingSpecification(VERTICAL_ONLY)
                .build();

        SPELEOTHEM = RockBlockVariant
                .builder("speleothem")
                .setBaseHardness(6f)
                .setFactory(BlockRockSpeleothem::new)
                .build();

        BUTTON = RockBlockVariant
                .builder("button")
                .setBaseHardness(6f)
                .setFactory(BlockRockButton::new)
                .build();

        PRESSURE_PLATE = RockBlockVariant
                .builder("pressure_plate")
                .setBaseHardness(6f)
                .setFactory(BlockRockPressurePlate::new)
                .build();

        ANVIL = RockBlockVariant
                .builder("anvil")
                .setBaseHardness(6f)
                .setFactory(BlockRockAnvil::new)
                .setFallingSpecification(COLLAPSABLE_ROCK)
                .build();

        MAGMA = RockBlockVariant
                .builder("magma")
                .setBaseHardness(6f)
                .setFactory(BlockRockMagma::new)
                .setFallingSpecification(COLLAPSABLE_ROCK)
                .build();

        STAND_GEM = RockBlockVariant
                .builder("stand_gem")
                .setBaseHardness(1F)
                .setFactory(BlockRockStandGem::new)
                .build();
    }
}
