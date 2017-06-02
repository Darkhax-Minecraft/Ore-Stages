package com.jarhax.oretiers;

import org.apache.logging.log4j.Logger;

import com.jarhax.oretiers.api.OreTiersAPI;
import com.jarhax.oretiers.api.StageDataHandler;
import com.jarhax.oretiers.compat.crt.OreTiersCrT;
import com.jarhax.oretiers.packet.PacketReloadModels;
import com.jarhax.oretiers.packet.PacketRequestClientRefresh;

import net.darkhax.bookshelf.network.NetworkHandler;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "oretiers", name = "Ore Tiers", version = "@VERSION@", dependencies = "required-after:bookshelf@[2.0.0.380,);required-after:crafttweaker@[3.0.25.,)")
public class OreTiers {

    public static Logger log;
    public static NetworkHandler NETWORK = new NetworkHandler("oretiers");

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent ev) {

        NETWORK.register(PacketReloadModels.class, Side.CLIENT);
        NETWORK.register(PacketRequestClientRefresh.class, Side.CLIENT);

        log = ev.getModLog();
        MinecraftForge.EVENT_BUS.register(new OreTiersEventHandler());

        OreTiersAPI.addReplacement("one", Blocks.IRON_ORE, Blocks.STONE);
        OreTiersAPI.addReplacement("two", Blocks.GOLD_ORE, Blocks.STONE);
        PlayerDataHandler.registerDataHandler("oretiers", new StageDataHandler());
    }

    @Mod.EventHandler
    public void postInit (FMLPostInitializationEvent ev) {

        OreTiersCrT.init();
    }
}
