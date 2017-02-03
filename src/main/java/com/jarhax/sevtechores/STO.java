package com.jarhax.sevtechores;

import static com.jarhax.sevtechores.STO.MODID;
import static com.jarhax.sevtechores.STO.NAME;
import static com.jarhax.sevtechores.STO.VERSION;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jarhax.sevtechores.commands.CommandSevTech;
import com.jarhax.sevtechores.handler.PlayerSaveHandler;
import com.jarhax.sevtechores.handler.WorldGenHandler;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = MODID, name = NAME, version = VERSION)
public class STO {

	public static final String MODID = "sevtechores";

	public static final String NAME = "SevTech-Ores";

	public static final String VERSION = "1.0.0";

	public static final Logger LOGGER = LogManager.getLogger(NAME);

	@Mod.EventHandler
	public void preInit (FMLPreInitializationEvent ev) {

		CapabilityManager.INSTANCE.register(PlayerSaveHandler.ICustomData.class, new PlayerSaveHandler.Storage(), PlayerSaveHandler.Default.class);
		MinecraftForge.EVENT_BUS.register(new PlayerSaveHandler());
		MinecraftForge.ORE_GEN_BUS.register(new WorldGenHandler());
	}

	@Mod.EventHandler
	public void serverLoad (FMLServerStartingEvent event) {

		event.registerServerCommand(new CommandSevTech());
	}
}
