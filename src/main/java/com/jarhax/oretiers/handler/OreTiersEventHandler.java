package com.jarhax.oretiers.handler;

import com.jarhax.oretiers.api.OreTiersAPI;

import net.darkhax.bookshelf.client.render.block.RenderBlockEvent;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Tuple;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OreTiersEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockDrops (HarvestDropsEvent event) {

        if (OreTiersAPI.isBlacklisted(event.getState())) {
            return;
        }

        final Tuple<String, IBlockState> stageInfo = OreTiersAPI.STATE_MAP.get(event.getState());

        if (stageInfo != null && (event.getHarvester() == null || !OreTiersAPI.hasStage(event.getHarvester(), stageInfo.getFirst()))) {
            event.getDrops().clear();
            event.setDropChance(ForgeEventFactory.fireBlockHarvesting(event.getDrops(), event.getWorld(), event.getPos(), stageInfo.getSecond(), event.getFortuneLevel(), event.getDropChance(), event.isSilkTouching(), event.getHarvester()));
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onBlockRender (RenderBlockEvent event) {

        if (OreTiersAPI.isBlacklisted(event.getState())) {
            return;
        }

        final Tuple<String, IBlockState> stageInfo = OreTiersAPI.STATE_MAP.get(event.getState());

        if (stageInfo != null && !OreTiersAPI.hasStage(PlayerUtils.getClientPlayer(), stageInfo.getFirst())) {
            event.setState(stageInfo.getSecond());
            event.setModel(Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(stageInfo.getSecond()));
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onClientTick (TickEvent.ClientTickEvent event) {

        if (OreTiersAPI.requireReload) {

            Minecraft.getMinecraft().renderGlobal.loadRenderers();
            OreTiersAPI.requireReload = false;
        }
    }
}
