package com.jarhax.sevtechores.commands;

import com.jarhax.sevtechores.STO;
import com.jarhax.sevtechores.handler.WorldGenHandler;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

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

		STO.LOGGER.info("Starting State Dump!");
		for (final IBlockState state : WorldGenHandler.KNOWN_STATES) {

			STO.LOGGER.info(state.toString());
		}
		final int amount = WorldGenHandler.KNOWN_STATES.size();
		STO.LOGGER.info(String.format("Ending State Dump! %d entries!", amount));
		sender.addChatMessage(new TextComponentTranslation("commands.sevores.known.size%n", new Object[] { amount }));
	}
}