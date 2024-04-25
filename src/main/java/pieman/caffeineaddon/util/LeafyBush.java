package pieman.caffeineaddon.util;

import net.minecraft.item.ItemStack;


import net.dries007.tfc.api.types.IBerryBush;
import net.dries007.tfc.util.calendar.CalendarTFC;
import net.dries007.tfc.util.calendar.ICalendar;
import net.dries007.tfc.util.calendar.Month;
import pieman.caffeineaddon.init.ModItems;

public enum LeafyBush implements IBerryBush {

    TEA(Month.MAY, 4, 5f, 35f, 100f, 400f, 0.8f, true);

    private final Month harvestMonthStart;
    private final int harvestingMonths;
    private final float growthTime;
    private final float minTemp;
    private final float maxTemp;
    private final float minRain;
    private final float maxRain;
    private final boolean hasSpikes;

    LeafyBush(Month harvestMonthStart, int harvestingMonths, float minTemp, float maxTemp, float minRain, float maxRain, float growthTime,
              boolean spiky) {
        this.harvestMonthStart = harvestMonthStart;
        this.harvestingMonths = harvestingMonths;
        this.growthTime = growthTime * CalendarTFC.CALENDAR_TIME.getDaysInMonth() * ICalendar.HOURS_IN_DAY;

        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.minRain = minRain;
        this.maxRain = maxRain;

        this.hasSpikes = spiky;
    }

    @Override
    public float getGrowthTime() {
        return this.growthTime;
    }

    @Override
    public boolean isHarvestMonth(Month month) {
        Month testing = this.harvestMonthStart;
        for (int i = 0; i < this.harvestingMonths; i++) {
            if (testing.equals(month)) return true;
            testing = testing.next();
        }
        return false;
    }

    @Override
    public boolean isValidConditions(float temperature, float rainfall) {
        return minTemp - 5 < temperature && temperature < maxTemp + 5 && minRain - 50 < rainfall && rainfall < maxRain + 50;
    }

    @Override
    public boolean isValidForGrowth(float temperature, float rainfall) {
        return minTemp < temperature && temperature < maxTemp && minRain < rainfall && rainfall < maxRain;
    }

    @Override
    public ItemStack getFoodDrop() {
        return new ItemStack(ModItems.GroundCoffee);
    }

    public Size getSize() {
        return null;
    }

    public String getName() {
        return this.name().toLowerCase();
    }

    @Override
    public boolean isSpiky() {
        return hasSpikes;
    }

}
