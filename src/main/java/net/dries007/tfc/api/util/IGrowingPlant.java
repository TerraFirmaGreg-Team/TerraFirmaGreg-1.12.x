/*
 * Work under Copyright. Licensed under the EUPL.
 * See the project README.md and LICENSE.txt for more information.
 */

package net.dries007.tfc.api.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import su.terrafirmagreg.Constants;

public interface IGrowingPlant {
	public GrowthStatus getGrowingStatus(IBlockState state, World world, BlockPos pos);

	public enum GrowthStatus {
		/**
		 * The plant is dead.
		 */
		DEAD,
		/**
		 * The plant is done growing.
		 */
		GROWING,
		/**
		 * The plant is done growing.
		 */
		FULLY_GROWN,
		/**
		 * If the plant would grow but requires something, like a specific season.
		 */
		CAN_GROW,
		/**
		 * The plant cannot grow at the moment.
		 */
		NOT_GROWING;

		@Override
		public String toString() {
			return Constants.MODID_TFC + ".enum.growstatus." + name().toLowerCase();
		}
	}

}
