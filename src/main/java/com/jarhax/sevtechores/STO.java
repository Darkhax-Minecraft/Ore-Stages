package com.jarhax.sevtechores;

import com.jarhax.sevtechores.handler.PlayerSaveHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import static com.jarhax.sevtechores.STO.*;

@Mod(modid = MODID, name = NAME, version = VERSION)
public class STO {
	
	public static final String MODID = "sevtechores";
	public static final String NAME = "SevTech-Ores";
	public static final String VERSION = "1.0.0";
	
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent ev) {
		CapabilityManager.INSTANCE.register(PlayerSaveHandler.ICustomData.class, new PlayerSaveHandler.Storage(), PlayerSaveHandler.Default.class);
		MinecraftForge.EVENT_BUS.register(new PlayerSaveHandler());
	}
}
