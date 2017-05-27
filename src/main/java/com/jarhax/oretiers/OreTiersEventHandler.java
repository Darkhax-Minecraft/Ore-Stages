package com.jarhax.oretiers;

import java.util.HashMap;
import java.util.Map;

import com.jarhax.oretiers.api.OreTiersAPI;
import com.jarhax.oretiers.client.renderer.block.model.BakedModelTiered;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OreTiersEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SideOnly(Side.CLIENT)
    public void onModelBake (ModelBakeEvent event) {

        final Map<IBlockState, ModelResourceLocation> releventModels = new HashMap<>();

        for (final Map.Entry<IBlockState, ModelResourceLocation> entry : event.getModelManager().getBlockModelShapes().getBlockStateMapper().putAllStateModelLocations().entrySet()) {

            if (OreTiersAPI.getRelevantStates().contains(entry.getKey())) {

                releventModels.put(entry.getKey(), entry.getValue());
            }
        }

        for (final IBlockState state : OreTiersAPI.getStatesToReplace()) {

            final Tuple<String, IBlockState> stageInfo = OreTiersAPI.getStageInfo(state);
            final IBakedModel originalModel = event.getModelRegistry().getObject(releventModels.get(state));
            final IBakedModel replacementModel = event.getModelRegistry().getObject(releventModels.get(stageInfo.getSecond()));
            event.getModelRegistry().putObject(releventModels.get(state), new BakedModelTiered(stageInfo.getFirst(), originalModel, replacementModel));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockDrops (HarvestDropsEvent event) {

        final Tuple<String, IBlockState> stageInfo = OreTiersAPI.getStageInfo(event.getState());

        if (stageInfo != null && (event.getHarvester() == null || !OreTiersAPI.hasStage(event.getHarvester(), stageInfo.getFirst()))) {
            event.getDrops().clear();
            event.setDropChance(ForgeEventFactory.fireBlockHarvesting(event.getDrops(), event.getWorld(), event.getPos(), stageInfo.getSecond(), event.getFortuneLevel(), event.getDropChance(), event.isSilkTouching(), event.getHarvester()));
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
