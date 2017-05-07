package com.jarhax.oretiers.proxy;

import com.jarhax.oretiers.api.OreTiersAPI;

import net.darkhax.bookshelf.client.render.block.RenderBlockEvent;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Tuple;
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

        if (OreTiersAPI.isBlacklisted(event.getState())) {
            return;
        }

        final Tuple<String, IBlockState> stageInfo = OreTiersAPI.STATE_MAP.get(event.getState());

        if (stageInfo != null && !OreTiersAPI.hasStage(PlayerUtils.getClientPlayer(), stageInfo.getFirst())) {
            event.setState(stageInfo.getSecond());
        }
    }
}
