package net.dries007.tfc.util.agriculture;

import su.terrafirmagreg.modules.world.classic.objects.generator.GeneratorWildCrops;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


import net.dries007.tfc.api.types.ICrop;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropDead;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropSimple;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropSpreading;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropTFC;
import net.dries007.tfc.objects.items.ItemsTFCF;
import net.dries007.tfc.objects.items.food.ItemFoodTFCF;
import net.dries007.tfc.util.calendar.Calendar;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.skills.Skill;
import net.dries007.tfc.util.skills.SkillTier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static net.dries007.tfc.util.agriculture.CropTFCF.CropType.PICKABLE;
import static net.dries007.tfc.util.agriculture.CropTFCF.CropType.SIMPLE;

public enum CropTFCF implements ICrop {
    AMARANTH(ItemsTFCF.AMARANTH, 0f, 8f, 35f, 40f, 50f, 100f, 400f, 450f, 6, 0.5f, SIMPLE),
    BUCKWHEAT(ItemsTFCF.BUCKWHEAT, -5f, 0f, 30f, 35f, 50f, 100f, 400f, 450f, 6, 0.5f, SIMPLE),
    FONIO(ItemsTFCF.MILLET, 7f, 15f, 40f, 50f, 50f, 70f, 200f, 250f, 6, 0.5f, SIMPLE),
    MILLET(ItemsTFCF.MILLET, 0f, 4f, 35f, 40f, 70f, 90f, 400f, 450f, 6, 0.5f, SIMPLE),
    QUINOA(ItemsTFCF.QUINOA, -10f, -5f, 35f, 40f, 50f, 100f, 400f, 450f, 6, 0.5f, SIMPLE),
    SPELT(ItemsTFCF.SPELT, 0f, 4f, 35f, 40f, 70f, 90f, 400f, 450f, 6, 0.5f, SIMPLE),
    //WILD_RICE(ItemsTFCF.WILD_RICE, 0f, 4f, 35f, 40f, 50f, 100f, 400f, 450f, 8, 0.5f, SIMPLE),
    BLACK_EYED_PEAS(ItemsTFCF.BLACK_EYED_PEAS, 0f, 8f, 35f, 40f, 50f, 100f, 400f, 450f, 7, 0.5f, PICKABLE),
    CAYENNE_PEPPER(() -> new ItemStack(ItemFoodTFCF.get(ItemsTFCF.RED_CAYENNE_PEPPER)),
            () -> new ItemStack(ItemFoodTFCF.get(ItemsTFCF.GREEN_CAYENNE_PEPPER)), 4f, 12f, 35f, 40f, 50f, 100f, 400f, 450f, 7, 0.5f, PICKABLE),
    //CELERY(ItemsTFCF.CELERY, 0f, 8f, 35f, 40f, 50f, 100f, 400f, 450f, 8, 0.5f, SIMPLE),
    GINGER(ItemsTFCF.GINGER, 0f, 5f, 35f, 40f, 50f, 100f, 400f, 450f, 5, 0.5f, SIMPLE),
    GINSENG(ItemsTFCF.GINSENG, 0f, 5f, 35f, 40f, 50f, 100f, 400f, 450f, 5, 0.5f, SIMPLE),
    //LETTUCE(ItemsTFCF.LETTUCE, 0f, 10f, 35f, 40f, 50f, 100f, 400f, 450f, 8, 0.5f, SIMPLE),
    //PEANUT(ItemsTFCF.PEANUT, 0f, 8f, 35f, 40f, 50f, 100f, 400f, 450f, 6, 0.5f, SIMPLE),
    RUTABAGA(ItemsTFCF.RUTABAGA, 0f, 8f, 35f, 40f, 50f, 100f, 400f, 450f, 7, 0.5f, SIMPLE),
    TURNIP(ItemsTFCF.TURNIP, 0f, 8f, 35f, 40f, 50f, 100f, 400f, 450f, 7, 0.5f, SIMPLE),
    //MUSTARD(ItemsTFCF.MUSTARD, 0f, 8f, 35f, 40f, 50f, 100f, 400f, 450f, 7, 0.5f, SIMPLE),
    //SWEET_POTATO(ItemsTFCF.SWEET_POTATO, 0f, 4f, 35f, 40f, 50f, 100f, 400f, 450f, 7, 0.5f, SIMPLE),
    SUGAR_BEET(ItemsTFCF.SUGAR_BEET, 0f, 5f, 35f, 40f, 50f, 100f, 400f, 450f, 7, 0.5f, SIMPLE),
    PURPLE_GRAPE(ItemsTFCF.PURPLE_GRAPE, 0f, 8f, 35f, 40f, 50f, 100f, 400f, 450f, 8, 0.5f, PICKABLE),
    GREEN_GRAPE(ItemsTFCF.GREEN_GRAPE, 0f, 8f, 35f, 40f, 50f, 100f, 400f, 450f, 8, 0.5f, PICKABLE),
    LIQUORICE_ROOT(ItemsTFCF.LIQUORICE_ROOT, -20f, -1f, 18f, 26f, 50f, 60f, 310f, 340f, 8, 0.5f, SIMPLE),
    COFFEA(() -> new ItemStack(ItemsTFCF.COFFEA_CHERRIES), () -> ItemStack.EMPTY, 7f, 15f, 40f, 50f, 50f, 70f, 200f, 250f, 8, 0.5f, PICKABLE),
    AGAVE(() -> new ItemStack(ItemsTFCF.AGAVE), () -> ItemStack.EMPTY, 12f, 18f, 35f, 40f, 50f, 100f, 400f, 450f, 6, 0.5f, SIMPLE),
    COCA(() -> new ItemStack(ItemsTFCF.COCA_LEAF), () -> ItemStack.EMPTY, 0f, 18f, 35f, 40f, 50f, 100f, 400f, 450f, 6, 0.5f, PICKABLE),
    COTTON(() -> new ItemStack(ItemsTFCF.COTTON_BOLL), () -> ItemStack.EMPTY, 0f, 8f, 35f, 40f, 50f, 100f, 400f, 450f, 6, 0.5f, PICKABLE),
    FLAX(() -> new ItemStack(ItemsTFCF.FLAX), () -> ItemStack.EMPTY, 0f, 18f, 35f, 40f, 50f, 100f, 400f, 450f, 6, 0.5f, SIMPLE),
    HEMP(() -> new ItemStack(ItemsTFCF.HEMP), () -> new ItemStack(ItemsTFCF.CANNABIS_BUD), 0f, 18f, 35f, 40f, 50f, 100f, 400f, 450f, 5, 0.5f,
            PICKABLE),
    HOP(() -> new ItemStack(ItemsTFCF.HOPS), () -> ItemStack.EMPTY, 0f, 18f, 35f, 40f, 50f, 100f, 400f, 450f, 6, 0.5f, PICKABLE),
    INDIGO(() -> new ItemStack(ItemsTFCF.INDIGO), () -> ItemStack.EMPTY, 0f, 18f, 35f, 40f, 50f, 100f, 400f, 450f, 5, 0.5f, SIMPLE),
    MADDER(() -> new ItemStack(ItemsTFCF.MADDER), () -> ItemStack.EMPTY, 0f, 18f, 35f, 40f, 50f, 100f, 400f, 450f, 5, 0.5f, SIMPLE),
    OPIUM_POPPY(() -> new ItemStack(ItemsTFCF.OPIUM_POPPY_BULB), () -> ItemStack.EMPTY, 0f, 4f, 35f, 40f, 50f, 100f, 400f, 450f, 6, 0.5f, PICKABLE),
    RAPE(() -> new ItemStack(ItemsTFCF.RAPE), () -> ItemStack.EMPTY, 0f, 10f, 35f, 40f, 50f, 100f, 400f, 450f, 6, 0.5f, SIMPLE),
    WELD(() -> new ItemStack(ItemsTFCF.WELD), () -> ItemStack.EMPTY, 0f, 18f, 35f, 40f, 50f, 100f, 400f, 450f, 5, 0.5f, SIMPLE),
    WOAD(() -> new ItemStack(ItemsTFCF.WOAD), () -> ItemStack.EMPTY, 0f, 18f, 35f, 40f, 50f, 100f, 400f, 450f, 6, 0.5f, SIMPLE),
    TOBACCO(() -> new ItemStack(ItemsTFCF.TOBACCO_LEAF), () -> ItemStack.EMPTY, 0f, 18f, 35f, 40f, 50f, 100f, 400f, 450f, 7, 0.5f, PICKABLE);

    static {
        for (ICrop crop : values()) {
            GeneratorWildCrops.register(crop);
        }
    }

    // how this crop generates food items
    private final Supplier<ItemStack> foodDrop;
    private final Supplier<ItemStack> foodDropEarly;
    // temperature compatibility range
    private final float tempMinAlive, tempMinGrow, tempMaxGrow, tempMaxAlive;
    // rainfall compatibility range
    private final float rainMinAlive, rainMinGrow, rainMaxGrow, rainMaxAlive;
    // growth
    private final int growthStages; // the number of blockstates the crop has for growing, ignoring wild state
    private final float growthTime; // Time is measured in % of months, scales with calendar month length
    // which crop block behavior implementation is used
    private final CropType type;

    CropTFCF(ItemFoodTFCF foodDrop, float tempMinAlive, float tempMinGrow, float tempMaxGrow, float tempMaxAlive,
             float rainMinAlive, float rainMinGrow, float rainMaxGrow, float rainMaxAlive, int growthStages,
             float growthTime, CropType type) {
        this(() -> new ItemStack(ItemFoodTFCF.get(foodDrop)), () -> ItemStack.EMPTY, tempMinAlive, tempMinGrow, tempMaxGrow, tempMaxAlive,
                rainMinAlive, rainMinGrow, rainMaxGrow, rainMaxAlive, growthStages, growthTime, type);
    }

    CropTFCF(Supplier<ItemStack> foodDrop, Supplier<ItemStack> foodDropEarly, float tempMinAlive, float tempMinGrow, float tempMaxGrow,
             float tempMaxAlive, float rainMinAlive, float rainMinGrow, float rainMaxGrow, float rainMaxAlive, int growthStages, float growthTime,
             CropType type) {
        this.foodDrop = foodDrop;
        this.foodDropEarly = foodDropEarly;

        this.tempMinAlive = tempMinAlive;
        this.tempMinGrow = tempMinGrow;
        this.tempMaxGrow = tempMaxGrow;
        this.tempMaxAlive = tempMaxAlive;

        this.rainMinAlive = rainMinAlive;
        this.rainMinGrow = rainMinGrow;
        this.rainMaxGrow = rainMaxGrow;
        this.rainMaxAlive = rainMaxAlive;

        this.growthStages = growthStages;
        this.growthTime = growthTime; // This is measured in % of months

        this.type = type;
    }

    /**
     * the count to add to the amount of food dropped when applying the skill bonus
     *
     * @param skill  agriculture skill of the harvester
     * @param random random instance to use, generally Block.RANDOM
     * @return amount to add to item stack count
     */
    public static int getSkillFoodBonus(Skill skill, Random random) {
        return random.nextInt(2 + (int) (6 * skill.getTotalLevel()));
    }

    /**
     * the count to add to the amount of seeds dropped when applying the skill bonus
     *
     * @param skill  agriculture skill of the harvester
     * @param random random instance to use, generally Block.RANDOM
     * @return amount to add to item stack count
     */
    public static int getSkillSeedBonus(Skill skill, Random random) {
        if (skill.getTier().isAtLeast(SkillTier.ADEPT) && random.nextInt(10 - 2 * skill.getTier().ordinal()) == 0)
            return 1;
        else
            return 0;
    }

    @Override
    public long getGrowthTicks() {
        return (long) (growthTime * Calendar.CALENDAR_TIME.getDaysInMonth() * ICalendar.TICKS_IN_DAY);
    }

    @Override
    public int getMaxStage() {
        return growthStages - 1;
    }

    @Override
    public boolean isValidConditions(float temperature, float rainfall) {
        return tempMinAlive < temperature && temperature < tempMaxAlive && rainMinAlive < rainfall && rainfall < rainMaxAlive;
    }

    @Override
    public boolean isValidForGrowth(float temperature, float rainfall) {
        return tempMinGrow < temperature && temperature < tempMaxGrow && rainMinGrow < rainfall && rainfall < rainMaxGrow;
    }

    @NotNull
    @Override
    public ItemStack getFoodDrop(int currentStage) {
        if (currentStage == getMaxStage()) {
            return foodDrop.get();
        } else if (currentStage == getMaxStage() - 1) {
            return foodDropEarly.get();
        }
        return ItemStack.EMPTY;
    }

    public BlockCropTFC createGrowingBlock() {
        if (type == CropType.SIMPLE || type == CropType.PICKABLE) {
            return BlockCropSimple.create(this, type == CropType.PICKABLE);
        } else if (type == CropType.SPREADING) {
            return BlockCropSpreading.create(this);
        }
        throw new IllegalStateException("Invalid growthstage property " + growthStages + " for crop");
    }

    public BlockCropDead createDeadBlock() {
        return new BlockCropDead(this);
    }

    @SideOnly(Side.CLIENT)
    public void addInfo(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (GuiScreen.isShiftKeyDown()) {
            tooltip.add(TextFormatting.GRAY + I18n.format("tfc.tooltip.climate_info"));
            tooltip.add(TextFormatting.BLUE + I18n.format("tfc.tooltip.climate_info_rainfall", (int) rainMinGrow, (int) rainMaxGrow));
            tooltip.add(TextFormatting.GOLD +
                    I18n.format("tfc.tooltip.climate_info_temperature", String.format("%.1f", tempMinGrow), String.format("%.1f", tempMaxGrow)));
        } else {
            tooltip.add(TextFormatting.GRAY + I18n.format("tfc.tooltip.hold_shift_for_climate_info"));
        }
    }

    enum CropType {
        SIMPLE,
        PICKABLE,
        SPREADING
    }
}
