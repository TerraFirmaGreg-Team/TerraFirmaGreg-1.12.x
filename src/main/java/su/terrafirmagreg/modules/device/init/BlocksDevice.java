package su.terrafirmagreg.modules.device.init;

import su.terrafirmagreg.api.registry.RegistryManager;
import su.terrafirmagreg.modules.device.object.block.BlockAlloyCalculator;
import su.terrafirmagreg.modules.device.object.block.BlockBearTrap;
import su.terrafirmagreg.modules.device.object.block.BlockBellows;
import su.terrafirmagreg.modules.device.object.block.BlockBlastFurnace;
import su.terrafirmagreg.modules.device.object.block.BlockBloom;
import su.terrafirmagreg.modules.device.object.block.BlockBloomery;
import su.terrafirmagreg.modules.device.object.block.BlockCellarDoor;
import su.terrafirmagreg.modules.device.object.block.BlockCellarShelf;
import su.terrafirmagreg.modules.device.object.block.BlockCellarWall;
import su.terrafirmagreg.modules.device.object.block.BlockCharcoalForge;
import su.terrafirmagreg.modules.device.object.block.BlockCharcoalPile;
import su.terrafirmagreg.modules.device.object.block.BlockCrate;
import su.terrafirmagreg.modules.device.object.block.BlockCrucible;
import su.terrafirmagreg.modules.device.object.block.BlockDryingMat;
import su.terrafirmagreg.modules.device.object.block.BlockElectricForge;
import su.terrafirmagreg.modules.device.object.block.BlockFirePit;
import su.terrafirmagreg.modules.device.object.block.BlockFreezeDryer;
import su.terrafirmagreg.modules.device.object.block.BlockFridge;
import su.terrafirmagreg.modules.device.object.block.BlockGreenhouseDoor;
import su.terrafirmagreg.modules.device.object.block.BlockGreenhouseRoof;
import su.terrafirmagreg.modules.device.object.block.BlockGreenhouseWall;
import su.terrafirmagreg.modules.device.object.block.BlockGrindstoneManual;
import su.terrafirmagreg.modules.device.object.block.BlockIceBunker;
import su.terrafirmagreg.modules.device.object.block.BlockInductionCrucible;
import su.terrafirmagreg.modules.device.object.block.BlockInfectedAir;
import su.terrafirmagreg.modules.device.object.block.BlockLatexExtractor;
import su.terrafirmagreg.modules.device.object.block.BlockLeafMat;
import su.terrafirmagreg.modules.device.object.block.BlockLogPile;
import su.terrafirmagreg.modules.device.object.block.BlockMolten;
import su.terrafirmagreg.modules.device.object.block.BlockNestBox;
import su.terrafirmagreg.modules.device.object.block.BlockOven;
import su.terrafirmagreg.modules.device.object.block.BlockOvenChimney;
import su.terrafirmagreg.modules.device.object.block.BlockOvenWall;
import su.terrafirmagreg.modules.device.object.block.BlockPitKiln;
import su.terrafirmagreg.modules.device.object.block.BlockPowderKeg;
import su.terrafirmagreg.modules.device.object.block.BlockQuernHorse;
import su.terrafirmagreg.modules.device.object.block.BlockQuernManual;
import su.terrafirmagreg.modules.device.object.block.BlockSmelteryCauldron;
import su.terrafirmagreg.modules.device.object.block.BlockSmelteryFirebox;
import su.terrafirmagreg.modules.device.object.block.BlockSnare;
import su.terrafirmagreg.modules.device.object.block.BlockThatchBed;

import static su.terrafirmagreg.modules.device.ModuleDevice.REGISTRY;

public final class BlocksDevice {

  public static final BlockAlloyCalculator ALLOY_CALCULATOR = REGISTRY.block(new BlockAlloyCalculator());
  public static final BlockBearTrap BEAR_TRAP = REGISTRY.block(new BlockBearTrap());
  public static BlockSnare SNARE;
  public static BlockCrate CRATE;
  public static BlockBellows BELLOWS;
  public static BlockBlastFurnace BLAST_FURNACE;
  public static BlockBloom BLOOM;
  public static BlockBloomery BLOOMERY;
  public static BlockCharcoalForge CHARCOAL_FORGE;
  public static BlockCharcoalPile CHARCOAL_PILE;
  public static BlockCrucible CRUCIBLE;
  public static BlockFirePit FIRE_PIT;
  public static BlockPitKiln PIT_KILN;
  public static BlockQuernManual QUERN_MANUAL;
  public static BlockQuernHorse QUERN_HORSE;
  public static BlockMolten MOLTEN;
  public static BlockLogPile LOG_PILE;
  public static BlockCellarShelf CELLAR_SHELF;
  public static BlockCellarDoor CELLAR_DOOR;
  public static BlockCellarWall CELLAR_WALL;
  public static BlockIceBunker ICE_BUNKER;
  public static BlockInfectedAir INFECTED_AIR;
  public static BlockFreezeDryer FREEZE_DRYER;
  public static BlockNestBox NEST_BOX;
  public static BlockPowderKeg POWDERKEG;
  public static BlockThatchBed THATCH_BED;
  public static BlockGrindstoneManual GRINDSTONE_MANUAL;
  public static BlockElectricForge ELECTRIC_FORGE;
  public static BlockInductionCrucible INDUCTION_CRUCIBLE;
  public static BlockFridge FRIDGE;
  public static BlockLatexExtractor LATEX_EXTRACTOR;
  public static BlockSmelteryCauldron SMELTERY_CAULDRON;
  public static BlockSmelteryFirebox SMELTERY_FIREBOX;
  public static BlockGreenhouseDoor GREENHOUSE_DOOR;
  public static BlockGreenhouseRoof GREENHOUSE_ROOF;
  public static BlockGreenhouseWall GREENHOUSE_WALL;
  public static BlockOven OVEN;
  public static BlockOvenWall OVEN_WALL;
  public static BlockOvenChimney OVEN_CHIMNEY;
  public static BlockLeafMat LEAF_MAT;
  public static BlockDryingMat DRYING_MAT;

  public static void onRegister(RegistryManager registryManager) {
    //==== Other =================================================================================================//

//    ALLOY_CALCULATOR = registryManager.block(new BlockAlloyCalculator());
//    BEAR_TRAP = registryManager.block(new BlockBearTrap());
    SNARE = registryManager.block(new BlockSnare());
    CRATE = registryManager.block(new BlockCrate());
    BELLOWS = registryManager.block(new BlockBellows());
    BLAST_FURNACE = registryManager.block(new BlockBlastFurnace());
    BLOOM = registryManager.block(new BlockBloom());
    BLOOMERY = registryManager.block(new BlockBloomery());
    CHARCOAL_FORGE = registryManager.block(new BlockCharcoalForge());
    CHARCOAL_PILE = registryManager.block(new BlockCharcoalPile());
    CRUCIBLE = registryManager.block(new BlockCrucible());
    FIRE_PIT = registryManager.block(new BlockFirePit());
    PIT_KILN = registryManager.block(new BlockPitKiln());
    QUERN_MANUAL = registryManager.block(new BlockQuernManual());
    QUERN_HORSE = registryManager.block(new BlockQuernHorse());
    MOLTEN = registryManager.block(new BlockMolten());
    LOG_PILE = registryManager.block(new BlockLogPile());
    CELLAR_SHELF = registryManager.block(new BlockCellarShelf());
    CELLAR_DOOR = registryManager.block(new BlockCellarDoor());
    CELLAR_WALL = registryManager.block(new BlockCellarWall());
    ICE_BUNKER = registryManager.block(new BlockIceBunker());
    INFECTED_AIR = registryManager.block(new BlockInfectedAir());
    FREEZE_DRYER = registryManager.block(new BlockFreezeDryer());
    NEST_BOX = registryManager.block(new BlockNestBox());
    POWDERKEG = registryManager.block(new BlockPowderKeg());
    THATCH_BED = registryManager.block(new BlockThatchBed());
    GRINDSTONE_MANUAL = registryManager.block(new BlockGrindstoneManual());
    ELECTRIC_FORGE = registryManager.block(new BlockElectricForge());
    INDUCTION_CRUCIBLE = registryManager.block(new BlockInductionCrucible());
    FRIDGE = registryManager.block(new BlockFridge());
    LATEX_EXTRACTOR = registryManager.block(new BlockLatexExtractor());
    SMELTERY_CAULDRON = registryManager.block(new BlockSmelteryCauldron());
    SMELTERY_FIREBOX = registryManager.block(new BlockSmelteryFirebox());
    GREENHOUSE_DOOR = registryManager.block(new BlockGreenhouseDoor());
    GREENHOUSE_ROOF = registryManager.block(new BlockGreenhouseRoof());
    GREENHOUSE_WALL = registryManager.block(new BlockGreenhouseWall());
    OVEN = registryManager.block(new BlockOven());
    OVEN_WALL = registryManager.block(new BlockOvenWall());
    OVEN_CHIMNEY = registryManager.block(new BlockOvenChimney());
    LEAF_MAT = registryManager.block(new BlockLeafMat());
    DRYING_MAT = registryManager.block(new BlockDryingMat());
  }

}
