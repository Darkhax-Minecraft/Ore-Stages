package com.jarhax.oretiers;

import com.jarhax.oretiers.api.PlayerDataHandler;
import com.jarhax.oretiers.compat.crt.CompatCRT;
import com.jarhax.oretiers.packet.PacketUnlockStage;

import net.darkhax.bookshelf.network.NetworkHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = "oretiers", name = "Ore Tiers", version = "@VERSION@", dependencies ="required-after:bookshelf@[1.5.0.370,);required-after:crafttweaker@[3.0.23,)")
public class OreTiers {

    public static NetworkHandler network = new NetworkHandler("oretiers");

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent ev) {

        network.register(PacketUnlockStage.class, Side.CLIENT);
        PlayerDataHandler.initialize();
    }

    @Mod.EventHandler
    public void init (FMLInitializationEvent ev) {

    }

    @Mod.EventHandler
    public void postInit (FMLPostInitializationEvent ev) {
        CompatCRT.postInit();
    }
}
