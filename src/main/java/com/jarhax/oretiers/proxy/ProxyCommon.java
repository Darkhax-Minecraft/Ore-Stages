package com.jarhax.oretiers.proxy;

import com.jarhax.oretiers.OreTiers;
import com.jarhax.oretiers.api.*;
import com.jarhax.oretiers.compat.crt.OreTiersCrT;
import com.jarhax.oretiers.packet.PacketUnlockStage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class ProxyCommon {

    public void onPreInit () {

        OreTiers.network.register(PacketUnlockStage.class, Side.CLIENT);
        PlayerDataHandler.initialize();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onInit () {

    }

    public void onPostInit () {

        OreTiersCrT.init();
    }
    
    @SubscribeEvent
    public void getDrops(BlockEvent.HarvestDropsEvent e) {
        
        if(OreTiersAPI.isBlacklisted(e.getState()))
            return;
        if(OreTiersAPI.hasReplacement(e.getState())) {
            Tuple<String, IBlockState> state = OreTiersAPI.STATE_MAP.get(e.getState());
            if(e.getHarvester() == null || !OreTiersAPI.hasStage(e.getHarvester(), state.getFirst())) {
                e.getDrops().clear();
                e.getDrops().addAll(state.getSecond().getBlock().getDrops(e.getWorld(), e.getPos(), e.getState(), e.getFortuneLevel()));
            }
        }
    }
    
}