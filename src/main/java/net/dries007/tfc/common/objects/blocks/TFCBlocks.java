package net.dries007.tfc.common.objects.blocks;

import gregtech.api.GregTechAPI;
import gregtech.api.unification.material.Material;
import net.dries007.tfc.api.types.GroundcoverType;
import net.dries007.tfc.api.types.bush.IBushBlock;
import net.dries007.tfc.api.types.bush.type.BushType;
import net.dries007.tfc.api.types.crop.ICropBlock;
import net.dries007.tfc.api.types.crop.type.CropType;
import net.dries007.tfc.api.types.crop.variant.block.CropBlockVariant;
import net.dries007.tfc.api.types.metal.IMetalBlock;
import net.dries007.tfc.api.types.metal.variant.block.MetalBlockVariant;
import net.dries007.tfc.api.types.plant.IPlantBlock;
import net.dries007.tfc.api.types.plant.type.PlantType;
import net.dries007.tfc.api.types.plant.variant.block.PlantBlockVariant;
import net.dries007.tfc.api.types.rock.IRockBlock;
import net.dries007.tfc.api.types.rock.type.RockType;
import net.dries007.tfc.api.types.rock.variant.block.RockBlockVariant;
import net.dries007.tfc.api.types.soil.ISoilBlock;
import net.dries007.tfc.api.types.soil.type.SoilType;
import net.dries007.tfc.api.types.soil.variant.block.SoilBlockVariant;
import net.dries007.tfc.api.types.wood.IWoodBlock;
import net.dries007.tfc.api.types.wood.type.WoodType;
import net.dries007.tfc.api.types.wood.variant.block.WoodBlockVariant;
import net.dries007.tfc.api.util.Pair;
import net.dries007.tfc.common.objects.blocks.berrybush.BlockBerryBush;
import net.dries007.tfc.common.objects.blocks.devices.*;
import net.dries007.tfc.common.objects.blocks.fluid.BlockFluidHotWater;
import net.dries007.tfc.common.objects.blocks.fluid.BlockFluidWater;
import net.dries007.tfc.common.objects.blocks.metal.BlockMetalCladding;
import net.dries007.tfc.common.objects.blocks.rock.BlockAlabaster;
import net.dries007.tfc.common.objects.blocks.soil.peat.BlockPeat;
import net.dries007.tfc.common.objects.blocks.soil.peat.BlockPeatGrass;
import net.dries007.tfc.common.objects.items.itemblocks.ItemBlockCrucible;
import net.dries007.tfc.common.objects.items.itemblocks.ItemBlockLargeVessel;
import net.dries007.tfc.common.objects.items.itemblocks.ItemBlockPowderKeg;
import net.dries007.tfc.common.objects.items.itemblocks.ItemBlockTFC;
import net.dries007.tfc.compat.dynamictrees.blocks.BlockSoilRootyDirt;
import net.dries007.tfc.compat.gregtech.material.TFGMaterialFlags;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidRegistry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.dries007.tfc.api.types.rock.variant.block.RockBlockVariants.*;
import static net.dries007.tfc.api.types.soil.variant.block.SoilBlockVariants.*;
import static net.minecraft.block.material.Material.WATER;

public class TFCBlocks {

    //==== Block =====================================================================================================//

    public static final Map<Pair<RockBlockVariant, RockType>, IRockBlock> ROCK_BLOCKS = new LinkedHashMap<>();
    public static final Map<Pair<SoilBlockVariant, SoilType>, ISoilBlock> SOIL_BLOCKS = new LinkedHashMap<>();
    public static final Map<Pair<WoodBlockVariant, WoodType>, IWoodBlock> WOOD_BLOCKS = new LinkedHashMap<>();
    public static final Map<Pair<PlantBlockVariant, PlantType>, IPlantBlock> PLANT_BLOCKS = new LinkedHashMap<>();
    public static final Map<Pair<CropBlockVariant, CropType>, ICropBlock> CROP_BLOCKS = new LinkedHashMap<>();
    public static final Map<Pair<MetalBlockVariant, Material>, IMetalBlock> METAL_BLOCKS = new LinkedHashMap<>();
    public static final Map<Pair<String, RockBlockVariant>, BlockAlabaster> ALABASTER_BLOCKS = new LinkedHashMap<>();
    public static final Map<BushType, IBushBlock> BUSH_BLOCKS = new LinkedHashMap<>();
    public static final Map<GroundcoverType, BlockGroundcover> GROUNDCOVER_BLOCKS = new LinkedHashMap<>();

    public static final ArrayList<Block> TREE_BLOCKS = new ArrayList<>();


    // Блоки, имеющие предмет
    public static final List<ItemBlock> ITEM_BLOCKS = new ArrayList<>();

    // Блоки, не имеющие предмета
    public static final List<Block> BLOCKS = new ArrayList<>();

    // Жидкости
    public static final List<BlockFluidBase> FLUID = new ArrayList<>();

    public static BlockSoilRootyDirt blockRootyDirt;


    public static BlockDebug DEBUG;
    public static BlockPeat PEAT;
    public static BlockPeatGrass PEAT_GRASS;
    public static BlockAggregate AGGREGATE;
    public static BlockFireClay FIRE_CLAY_BLOCK;
    public static BlockThatch THATCH;
    public static BlockQuern QUERN;
    public static BlockCrucible CRUCIBLE;
    public static BlockBlastFurnace BLAST_FURNACE;
    public static BlockBellows BELLOWS;
    public static BlockBloomery BLOOMERY;
    public static BlockNestBox NEST_BOX;
    public static BlockLargeVessel FIRED_LARGE_VESSEL;
    public static BlockFirePit FIREPIT;
    public static BlockThatchBed THATCH_BED;
    public static BlockPitKiln PIT_KILN;
    public static BlockPlacedItemFlat PLACED_ITEM_FLAT;
    public static BlockPlacedItem PLACED_ITEM;
    public static BlockPlacedHide PLACED_HIDE;
    public static BlockCharcoalPile CHARCOAL_PILE;
    public static BlockLogPile LOG_PILE;
    public static BlockCharcoalForge CHARCOAL_FORGE;
    public static BlockMolten MOLTEN;
    public static BlockBloom BLOOM;
    public static BlockIceTFC SEA_ICE;
    public static BlockPowderKeg POWDERKEG;
    public static BlockMetalCladding CLADDING;

    public static void preInit() {

        //==== Crop ==================================================================================================//

        for (var variant : CropBlockVariant.getCropBlockVariants()) {
            for (var type : CropType.getCropTypes()) {
                var cropBlock = variant.create(type);

                if (CROP_BLOCKS.put(new Pair<>(variant, type), cropBlock) != null)
                    throw new RuntimeException(String.format("Duplicate registry detected: %s, %s", variant, type));
            }
        }

        //==== Rock ==================================================================================================//

        for (var variant : RockBlockVariant.getRockBlockVariants()) {
            for (var type : RockType.getRockTypes()) {
                var rockBlock = variant.create(type);

                if (ROCK_BLOCKS.put(new Pair<>(variant, type), rockBlock) != null)
                    throw new RuntimeException(String.format("Duplicate registry detected: %s, %s", variant, type));
            }
        }

        //==== Soil ==================================================================================================//

        for (var variant : SoilBlockVariant.getSoilBlockVariants()) {
            for (var type : SoilType.getSoilTypes()) {
                var soilBlock = variant.create(type);

                if (SOIL_BLOCKS.put(new Pair<>(variant, type), soilBlock) != null)
                    throw new RuntimeException(String.format("Duplicate registry detected: %s, %s", variant, type));
            }
        }

        //==== Wood ==================================================================================================//

        for (var variant : WoodBlockVariant.getWoodBlockVariants()) {
            for (var type : WoodType.getWoodTypes()) {
                var woodBlock = variant.create(type);

                if (WOOD_BLOCKS.put(new Pair<>(variant, type), woodBlock) != null)
                    throw new RuntimeException(String.format("Duplicate registry detected: %s, %s", variant, type));
            }
        }

        //==== Plant =================================================================================================//

        for (var type : PlantType.getPlantTypes()) {
            var plantBlock = type.getPlantVariant().create(type);

            if (PLANT_BLOCKS.put(new Pair<>(type.getPlantVariant(), type), plantBlock) != null)
                throw new RuntimeException(String.format("Duplicate registry detected: %s, %s", type.getPlantVariant(), type));
        }

        //==== BlockBush =============================================================================================//

        for (var type : BushType.getBushTypes()) {
            if (BUSH_BLOCKS.put(type, new BlockBerryBush(type)) != null)
                throw new RuntimeException(String.format("Duplicate registry detected: %s", type));
        }

        //==== Metal =================================================================================================//

        for (var material : GregTechAPI.materialManager.getRegistry("gregtech")) {
            if (material.hasFlag(TFGMaterialFlags.GENERATE_ANVIL)) {
                for (var variant : MetalBlockVariant.getMetalBlockVariants()) {
                    var metalBlock = variant.create(material);

                    if (METAL_BLOCKS.put(new Pair<>(variant, material), metalBlock) != null)
                        throw new RuntimeException(String.format("Duplicate registry detected: %s, %s, %s", variant, material, metalBlock));
                }
            }
        }

        //==== Alabaster =============================================================================================//

        for (var variant : new RockBlockVariant[]{RAW, BRICKS, SMOOTH}) {
            var alabasterBlock = new BlockAlabaster(variant);

            ALABASTER_BLOCKS.put(new Pair<>("plain", variant), alabasterBlock);

            for (EnumDyeColor dyeColor : EnumDyeColor.values()) {
                var alabasterColorBlock = new BlockAlabaster(variant, dyeColor);

                if (ALABASTER_BLOCKS.put(new Pair<>(dyeColor.getName(), variant), alabasterColorBlock) != null)
                    throw new RuntimeException(String.format("Duplicate registry detected: %s, %s", dyeColor, variant));
            }
        }

        //==== Groundcover ===========================================================================================//

        for (var type : GroundcoverType.values()) {
            var groundcoverBlock = new BlockGroundcover(type);

            if (GROUNDCOVER_BLOCKS.put(type, groundcoverBlock) != null)
                throw new RuntimeException(String.format("Duplicate registry detected: %s, %s", type, groundcoverBlock));
        }

        //==== Fluid =================================================================================================//

        FLUID.add(new BlockFluidHotWater());
        FLUID.add(new BlockFluidWater(FluidRegistry.getFluid("fresh_water"), WATER, false));
        FLUID.add(new BlockFluidWater(FluidRegistry.getFluid("salt_water"), WATER, true));

        //==== Other =================================================================================================//

        ITEM_BLOCKS.add(new ItemBlockTFC(PEAT = new BlockPeat()));
        ITEM_BLOCKS.add(new ItemBlockTFC(PEAT_GRASS = new BlockPeatGrass()));
        ITEM_BLOCKS.add(new ItemBlockTFC(DEBUG = new BlockDebug()));
        ITEM_BLOCKS.add(new ItemBlockTFC(AGGREGATE = new BlockAggregate()));
        ITEM_BLOCKS.add(new ItemBlockTFC(FIRE_CLAY_BLOCK = new BlockFireClay()));
        ITEM_BLOCKS.add(new ItemBlockTFC(THATCH = new BlockThatch()));
        ITEM_BLOCKS.add(new ItemBlockTFC(QUERN = new BlockQuern()));
        ITEM_BLOCKS.add(new ItemBlockCrucible(CRUCIBLE = new BlockCrucible()));
        ITEM_BLOCKS.add(new ItemBlockTFC(BLAST_FURNACE = new BlockBlastFurnace()));
        ITEM_BLOCKS.add(new ItemBlockTFC(BELLOWS = new BlockBellows()));
        ITEM_BLOCKS.add(new ItemBlockTFC(BLOOMERY = new BlockBloomery()));
        ITEM_BLOCKS.add(new ItemBlockTFC(NEST_BOX = new BlockNestBox()));
        ITEM_BLOCKS.add(new ItemBlockLargeVessel(FIRED_LARGE_VESSEL = new BlockLargeVessel()));
        ITEM_BLOCKS.add(new ItemBlock(FIREPIT = new BlockFirePit()));
        ITEM_BLOCKS.add(new ItemBlock(PIT_KILN = new BlockPitKiln()));
        ITEM_BLOCKS.add(new ItemBlock(PLACED_ITEM = new BlockPlacedItem()));
        ITEM_BLOCKS.add(new ItemBlock(CHARCOAL_FORGE = new BlockCharcoalForge()));
        ITEM_BLOCKS.add(new ItemBlockTFC(SEA_ICE = new BlockIceTFC(FluidRegistry.getFluid("salt_water"))));
        ITEM_BLOCKS.add(new ItemBlockPowderKeg(POWDERKEG = new BlockPowderKeg()));

        BLOCKS.add(PLACED_ITEM_FLAT = new BlockPlacedItemFlat());
        BLOCKS.add(PLACED_HIDE = new BlockPlacedHide());
        BLOCKS.add(CHARCOAL_PILE = new BlockCharcoalPile());
        BLOCKS.add(LOG_PILE = new BlockLogPile());
        BLOCKS.add(MOLTEN = new BlockMolten());
        BLOCKS.add(BLOOM = new BlockBloom());
        BLOCKS.add(THATCH_BED = new BlockThatchBed());
        BLOCKS.add(CLADDING = new BlockMetalCladding());

        blockRootyDirt = new BlockSoilRootyDirt();
    }

    @Nonnull
    public static Block getRockBlock(@Nonnull RockBlockVariant variant, @Nonnull RockType type) {
        var block = (Block) ROCK_BLOCKS.get(new Pair<>(variant, type));
        if (block != null) return block;
        throw new RuntimeException(String.format("Block is null: %s, %s", variant, type));
    }

    @Nonnull
    public static Block getSoilBlock(@Nonnull SoilBlockVariant variant, @Nonnull SoilType type) {
        var block = (Block) SOIL_BLOCKS.get(new Pair<>(variant, type));
        if (block != null) return block;
        throw new RuntimeException(String.format("Block is null: %s, %s", variant, type));
    }

    @Nonnull
    public static Block getWoodBlock(@Nonnull WoodBlockVariant variant, @Nonnull WoodType type) {
        var block = (Block) WOOD_BLOCKS.get(new Pair<>(variant, type));
        if (block != null) return block;
        throw new RuntimeException(String.format("Block is null: %s, %s", variant, type));
    }

    @Nonnull
    public static Block getPlantBlock(@Nonnull PlantBlockVariant variant, @Nonnull PlantType type) {
        var block = (Block) PLANT_BLOCKS.get(new Pair<>(variant, type));
        if (block != null) return block;
        throw new RuntimeException(String.format("Block is null: %s, %s", variant, type));
    }

    @Nonnull
    public static Block getCropBlock(@Nonnull CropBlockVariant variant, @Nonnull CropType type) {
        var block = (Block) CROP_BLOCKS.get(new Pair<>(variant, type));
        if (block != null) return block;
        throw new RuntimeException(String.format("Block is null: %s, %s", variant, type));
    }

    @Nonnull
    public static Block getAlabasterBlock(@Nonnull String string, @Nonnull RockBlockVariant variant) {
        var block = (Block) ALABASTER_BLOCKS.get(new Pair<>(string, variant));
        if (block != null) return block;
        throw new RuntimeException(String.format("Block is null: %s, %s", string, variant));
    }

    @Nonnull
    public static Block getBushBlock(@Nonnull BushType type) {
        var block = (Block) BUSH_BLOCKS.get(type);
        if (block != null) return block;
        throw new RuntimeException(String.format("Block is null: %s", type));
    }

    @Nonnull
    public static Block getGroundcoverBlock(@Nonnull GroundcoverType type) {
        var block = (Block) GROUNDCOVER_BLOCKS.get(type);
        if (block != null) return block;
        throw new RuntimeException(String.format("Block is null: %s", type));
    }

    @Nonnull
    public static Block getMetalBlock(@Nonnull MetalBlockVariant variant, @Nonnull Material material) {
        var block = (Block) METAL_BLOCKS.get(new Pair<>(variant, material));
        if (block != null) return block;
        throw new RuntimeException(String.format("Block is null: %s, %s", variant, material));
    }


    public static boolean isWater(IBlockState current) {
        return current.getMaterial() == WATER;
    }

    public static boolean isFreshWater(IBlockState current) {
        return current == FluidRegistry.getFluid("fresh_water").getBlock().getDefaultState();
    }

    public static boolean isSaltWater(IBlockState current) {
        return current == FluidRegistry.getFluid("salt_water").getBlock().getDefaultState();
    }

    public static boolean isFreshWaterOrIce(IBlockState current) {
        return current.getBlock() == Blocks.ICE || isFreshWater(current);
    }

    public static boolean isRawStone(IBlockState current) {
        if (current.getBlock() instanceof IRockBlock rockTypeBlock)
            return rockTypeBlock.getBlockVariant() == RAW;
        return false;
    }

    public static boolean isClay(IBlockState current) {
        if (current.getBlock() instanceof ISoilBlock soilTypeBlock) {
            var soilBlockVariant = soilTypeBlock.getBlockVariant();
            return soilBlockVariant == CLAY || soilBlockVariant == CLAY_GRASS;
        }
        return false;
    }

    public static boolean isDirt(IBlockState current) {
        if (current.getBlock() instanceof ISoilBlock soilTypeBlock)
            return soilTypeBlock.getBlockVariant() == DIRT;
        return false;
    }

    public static boolean isSand(IBlockState current) {
        if (current.getBlock() instanceof IRockBlock rockTypeBlock) {
            return rockTypeBlock.getBlockVariant() == SAND;
        }
        return false;
    }

    public static boolean isGravel(IBlockState current) {
        if (current.getBlock() instanceof IRockBlock rockTypeBlock) {
            return rockTypeBlock.getBlockVariant() == GRAVEL;
        }
        return false;
    }

    public static boolean isSoil(IBlockState current) {
        if (current.getBlock() instanceof BlockPeat) return true;
        if (current.getBlock() instanceof ISoilBlock soilTypeBlock) {
            var soilBlockVariant = soilTypeBlock.getBlockVariant();
            return soilBlockVariant == GRASS || soilBlockVariant == DRY_GRASS ||
                    soilBlockVariant == DIRT || soilBlockVariant == CLAY || soilBlockVariant == CLAY_GRASS;
        }
        return false;
    }

    public static boolean isGrowableSoil(IBlockState current) {
        if (current.getBlock() instanceof ISoilBlock soilTypeBlock) {
            var soilBlockVariant = soilTypeBlock.getBlockVariant();
            return soilBlockVariant == GRASS || soilBlockVariant == DRY_GRASS ||
                    soilBlockVariant == DIRT || soilBlockVariant == CLAY || soilBlockVariant == CLAY_GRASS;
        }
        return false;
    }

    public static boolean isSoilOrGravel(IBlockState current) {
        if (current.getBlock() instanceof BlockPeat) return true;
        if (current.getBlock() instanceof ISoilBlock soilTypeBlock) {
            var soilBlockVariant = soilTypeBlock.getBlockVariant();
            return soilBlockVariant == GRASS || soilBlockVariant == DRY_GRASS || soilBlockVariant == DIRT;
        }
        if (current.getBlock() instanceof IRockBlock rockTypeBlock)
            return rockTypeBlock.getBlockVariant() == GRAVEL;
        return false;
    }

    public static boolean isGrass(IBlockState current) {
        if (current.getBlock() instanceof BlockPeatGrass) return true;
        if (current.getBlock() instanceof ISoilBlock soilTypeBlock) {
            var soilBlockVariant = soilTypeBlock.getBlockVariant();
            return soilBlockVariant == GRASS || soilBlockVariant == DRY_GRASS || soilBlockVariant == CLAY_GRASS;
        }
        return false;
    }

    public static boolean isDryGrass(IBlockState current) {
        if (current.getBlock() instanceof ISoilBlock soilTypeBlock)
            return soilTypeBlock.getBlockVariant() == DRY_GRASS;
        return false;
    }

    public static boolean isGround(IBlockState current) {
        if (current.getBlock() instanceof IRockBlock rockTypeBlock) {
            var rockBlockVariant = rockTypeBlock.getBlockVariant();
            return rockBlockVariant == GRAVEL || rockBlockVariant == SAND || rockBlockVariant == RAW;
        }
        if (current.getBlock() instanceof ISoilBlock soilTypeBlock) {
            var soilBlockVariant = soilTypeBlock.getBlockVariant();
            return soilBlockVariant == GRASS || soilBlockVariant == DRY_GRASS || soilBlockVariant == DIRT;
        }
        return false;
    }
}
