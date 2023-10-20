/*
 * Work under Copyright. Licensed under the EUPL.
 * See the project README.md and LICENSE.txt for more information.
 */

package net.dries007.tfc.api.types;

/**
 * Use this to tell TFC this is a livestock animal (for respawning mechanics)
 * Used only in TFC worlds.
 */
public interface ILivestock extends ICreatureTFC {
    @Override
    default CreatureType getCreatureType() {return CreatureType.LIVESTOCK;}
}
