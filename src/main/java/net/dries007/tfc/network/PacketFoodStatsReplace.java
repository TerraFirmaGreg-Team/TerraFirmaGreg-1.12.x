package net.dries007.tfc.network;

import su.terrafirmagreg.modules.food.api.FoodStatsTFC;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import net.dries007.tfc.TerraFirmaCraft;
import su.terrafirmagreg.modules.core.capabilities.food.CapabilityFood;

/**
 * This packet is send to client to signal that it may need to replace the vanilla FoodStats with a TFC version Since all the relevant events where this is
 * listened for are server only {@link CapabilityFood}
 */
public class PacketFoodStatsReplace implements IMessageEmpty {

  public static final class Handler implements IMessageHandler<PacketFoodStatsReplace, IMessage> {

    @Override
    public IMessage onMessage(PacketFoodStatsReplace message, MessageContext ctx) {
      TerraFirmaCraft.getProxy().getThreadListener(ctx).addScheduledTask(() -> {
        EntityPlayer player = TerraFirmaCraft.getProxy().getPlayer(ctx);
        if (player != null) {
          FoodStatsTFC.replaceFoodStats(player);
        }
      });
      return null;
    }
  }
}
