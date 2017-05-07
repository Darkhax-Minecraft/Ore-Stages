package com.jarhax.oretiers.proxy;

import com.jarhax.oretiers.OreTiers;
import com.jarhax.oretiers.api.PlayerDataHandler;
import com.jarhax.oretiers.compat.crt.CompatCRT;
import com.jarhax.oretiers.packet.PacketUnlockStage;

import net.minecraftforge.fml.relauncher.Side;

public class ProxyCommon {

    public void onPreInit () {

        OreTiers.network.register(PacketUnlockStage.class, Side.CLIENT);
        PlayerDataHandler.initialize();
    }

    public void onInit () {

    }

    public void onPostInit () {

        CompatCRT.postInit();
    }
}