package com.jarhax.sevtechores.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandMinableInfo extends CommandBase {

	@Override
	public String getCommandName () {

		return "mineinfo";
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {

		return "Prints a list of all blocks this mod could have messed with.";
	}

	@Override
	public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

	}
}