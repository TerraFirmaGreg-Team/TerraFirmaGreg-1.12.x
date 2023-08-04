package net.dries007.tfc.compat.top.providers;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.objects.blocks.wood.tree.BlockWoodSapling;
import net.dries007.tfc.objects.te.TETickCounter;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.ICalendar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class TreeProvider implements IProbeInfoProvider {
		@Override
		public String getID() {
				return TerraFirmaCraft.MOD_ID + ":tree";
		}

		@Override
		public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, EntityPlayer entityPlayer, World world, IBlockState iBlockState, IProbeHitData iProbeHitData) {
				var blockPos = iProbeHitData.getPos();
				var state = world.getBlockState(blockPos);

				if (state.getBlock() instanceof BlockWoodSapling block) {
						var wood = block.getWood();
						var te = Helpers.getTE(world, blockPos, TETickCounter.class);

						if (te != null) {
								long days = te.getTicksSinceUpdate() / ICalendar.TICKS_IN_DAY;
								float perc = Math.min(0.99F, days / 8) * 100; //todo wood.getMinGrowthTime()
								var growth = String.format("%d%%", Math.round(perc));
								iProbeInfo.text(new TextComponentTranslation("waila.tfc.crop.growth", growth).getFormattedText());
						}
				}
		}
}
