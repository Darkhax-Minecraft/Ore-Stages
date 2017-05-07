package com.jarhax.oretiers.proxy;

import com.jarhax.oretiers.api.OreTiersAPI;

import net.darkhax.bookshelf.client.render.block.RenderBlockEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ProxyClient extends ProxyCommon {

    @Override
    public void onPreInit () {

        super.onPreInit();

        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onInit () {

        super.onInit();
    }

    @Override
    public void onPostInit () {

        super.onPostInit();
    }

    @SubscribeEvent
    public void onBlockRender (RenderBlockEvent event) {

        if (OreTiersAPI.isBlacklisted(event.getState()))
            return;
    }
}
