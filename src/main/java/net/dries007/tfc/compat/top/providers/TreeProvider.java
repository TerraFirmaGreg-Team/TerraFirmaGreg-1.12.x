/*
 * Work under Copyright. Licensed under the EUPL.
 * See the project README.md and LICENSE.txt for more information.
 */

package net.dries007.tfc.compat.top.providers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.dries007.tfc.TerraFirmaCraft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import net.dries007.tfc.api.types.Tree;
import net.dries007.tfc.compat.top.interfaces.IWailaBlock;
import net.dries007.tfc.objects.blocks.wood.BlockSaplingTFC;
import net.dries007.tfc.objects.te.TETickCounter;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.ICalendar;

public class TreeProvider implements IProbeInfoProvider
{
    @Override
    public String getID() {
        return TerraFirmaCraft.MOD_ID + ":tree";
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, EntityPlayer entityPlayer, World world, IBlockState iBlockState, IProbeHitData iProbeHitData) {
        var blockPos = iProbeHitData.getPos();
        var state = world.getBlockState(blockPos);

        if (state.getBlock() instanceof BlockSaplingTFC block)
        {
            var wood = block.getWood();
            var te = Helpers.getTE(world, blockPos, TETickCounter.class);

            if (te != null)
            {
                long days = te.getTicksSinceUpdate() / ICalendar.TICKS_IN_DAY;
                float perc = Math.min(0.99F, days / wood.getMinGrowthTime()) * 100;
                var growth = String.format("%d%%", Math.round(perc));
                iProbeInfo.text(new TextComponentTranslation("waila.tfc.crop.growth", growth).getFormattedText());
            }
        }
    }
}
