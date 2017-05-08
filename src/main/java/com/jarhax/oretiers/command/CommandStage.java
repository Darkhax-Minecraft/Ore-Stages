package com.jarhax.oretiers.command;

import com.jarhax.oretiers.api.OreTiersAPI;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandStage extends CommandBase {

    @Override
    public String getName () {

        return "orestage";
    }

    @Override
    public int getRequiredPermissionLevel () {

        return 2;
    }

    @Override
    public String getUsage (ICommandSender sender) {

        return "commands.oretiers.stage.usage";
    }

    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

        if (args.length == 3) {

            final EntityPlayer player = getPlayer(server, sender, args[0]);

            if (args[1].equalsIgnoreCase("add")) {

                OreTiersAPI.unlockStage(player, args[2]);
            }

            else if (args[1].equalsIgnoreCase("remove")) {

                OreTiersAPI.lockStage(player, args[2]);
            }
        }
    }
}