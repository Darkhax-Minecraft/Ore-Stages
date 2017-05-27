package com.jarhax.oretiers;

import org.apache.logging.log4j.Logger;

import com.jarhax.oretiers.api.OreTiersAPI;
import com.jarhax.oretiers.api.PlayerDataHandler;
import com.jarhax.oretiers.command.CommandStage;
import com.jarhax.oretiers.compat.crt.OreTiersCrT;
import com.jarhax.oretiers.packet.PacketStage;

import net.darkhax.bookshelf.network.NetworkHandler;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "oretiers", name = "Ore Tiers", version = "@VERSION@", dependencies = "required-after:bookshelf@[1.5.0.370,);required-after:crafttweaker@[3.0.23,)")
public class OreTiers {

    public static NetworkHandler network = new NetworkHandler("oretiers");
    public static Logger log;

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent ev) {

        log = ev.getModLog();
        network.register(PacketStage.class, Side.CLIENT);
        PlayerDataHandler.initialize();
        MinecraftForge.EVENT_BUS.register(new OreTiersEventHandler());

        OreTiersAPI.addReplacement("test", Blocks.IRON_ORE.getDefaultState(), Blocks.STONE.getDefaultState());
    }

    @Mod.EventHandler
    public void postInit (FMLPostInitializationEvent ev) {

        OreTiersCrT.init();
    }

    @Mod.EventHandler
    public void serverLoad (FMLServerStartingEvent ev) {

        ev.registerServerCommand(new CommandStage());
    }
}
