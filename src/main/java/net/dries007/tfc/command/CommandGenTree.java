package net.dries007.tfc.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.gen.structure.template.TemplateManager;


import mcp.MethodsReturnNonnullByDefault;
import net.dries007.tfc.api.registries.TFCRegistries;
import net.dries007.tfc.api.types.Tree;

import static su.terrafirmagreg.api.lib.Constants.MODID_TFC;
import static su.terrafirmagreg.api.lib.MathConstants.RNG;

@MethodsReturnNonnullByDefault
public class CommandGenTree extends CommandBase {

    @Override
    public String getName() {
        return "maketree";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "tfc.command.gentree.useage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length != 1) throw new WrongUsageException("tfc.command.gentree.failed");

        Tree tree = TFCRegistries.TREES.getValue(new ResourceLocation(args[0]));
        if (tree == null) tree = TFCRegistries.TREES.getValue(new ResourceLocation(MODID_TFC, args[0]));
        if (tree == null) throw new WrongUsageException("tfc.command.gentree.failed.woodtype", args[0]);

        if (sender.getCommandSenderEntity() == null) return;

        final World world = sender.getEntityWorld();
        final BlockPos center = new BlockPos(sender.getCommandSenderEntity());
        final TemplateManager manager = ((WorldServer) world).getStructureTemplateManager();

        if (!tree.makeTree(manager, world, center, RNG, false)) {
            sender.sendMessage(new TextComponentTranslation("tfc.command.gentree.failed.grow"));
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}
