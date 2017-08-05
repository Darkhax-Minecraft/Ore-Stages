package com.jarhax.oretiers;

import org.apache.logging.log4j.Logger;

import com.jarhax.oretiers.api.StageDataHandler;

import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "oretiers", name = "Ore Tiers", version = "@VERSION@", dependencies = "required-after:bookshelf@[2.0.0.380,);required-after:crafttweaker@[3.0.25.,)")
public class OreTiers {

    public static Logger log;

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent ev) {

        log = ev.getModLog();
        MinecraftForge.EVENT_BUS.register(new OreTiersEventHandler());
        PlayerDataHandler.registerDataHandler("oretiers", new StageDataHandler());

        if (Loader.isModLoaded("waila")) {

            FMLInterModComms.sendMessage("waila", "register", "com.jarhax.oretiers.compat.waila.OreTiersProvider.register");
        }
    }
}
