package com.jarhax.sevtechores.commands;

import com.jarhax.sevtechores.STO;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandSevTech extends CommandTreeBase {

	public CommandSevTech () {

		this.addSubcommand(new CommandMinableInfo());
	}

	@Override
	public String getCommandName () {

		return STO.MODID;
	}

	@Override
	public String getCommandUsage (ICommandSender sender) {

		return "The root command for all commands added by the mod.";
	}
}
