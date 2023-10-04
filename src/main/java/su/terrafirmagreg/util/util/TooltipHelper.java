package su.terrafirmagreg.util.util;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;
import su.terrafirmagreg.util.UtilTFG;

import java.util.List;

public class TooltipHelper {

    public static final String TOOLTIP_COMMON_HOLD_SHIFT = "gui." + UtilTFG.MOD_ID + ".tooltip.common.hold_shift";

    public static String tooltipHoldShiftStringGet() {

        return TextFormatting.DARK_GRAY + I18n.format(
                TOOLTIP_COMMON_HOLD_SHIFT,
                TextFormatting.AQUA,
                TextFormatting.DARK_GRAY
        );
    }

    public static void addTooltip(List<String> tooltip, String text, int preferredIndex) {

        if (tooltip.size() > preferredIndex) {
            tooltip.add(preferredIndex, text);

        } else {
            tooltip.add(text);
        }
    }
}
