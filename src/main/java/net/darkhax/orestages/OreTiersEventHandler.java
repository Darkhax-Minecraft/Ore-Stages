package net.darkhax.orestages;

import java.util.ListIterator;

import net.darkhax.bookshelf.util.BlockUtils;
import net.darkhax.bookshelf.util.RenderUtils;
import net.darkhax.gamestages.event.GameStageEvent;
import net.darkhax.orestages.api.OreTiersAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OreTiersEventHandler {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockBreak (BreakEvent event) {

        final Tuple<String, IBlockState> stageInfo = OreTiersAPI.getStageInfo(event.getState());

        if (stageInfo != null && (event.getPlayer() == null || !OreTiersAPI.hasStage(event.getPlayer(), stageInfo.getFirst()))) {

            event.setExpToDrop(0);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBreakSpeed (BreakSpeed event) {

        try {

            final Tuple<String, IBlockState> stageInfo = OreTiersAPI.getStageInfo(event.getState());

            if (stageInfo != null && (event.getEntityPlayer() == null || !OreTiersAPI.hasStage(event.getEntityPlayer(), stageInfo.getFirst()))) {

                event.setNewSpeed(BlockUtils.getBreakSpeedToMatch(event.getState(), stageInfo.getSecond(), event.getEntityPlayer().world, event.getEntityPlayer(), event.getPos()));
            }
        }

        catch (final Exception e) {

            OreStages.LOG.trace("Error calculating mining speed!", e);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockDrops (HarvestDropsEvent event) {

        final Tuple<String, IBlockState> stageInfo = OreTiersAPI.getStageInfo(event.getState());

        if (stageInfo != null && (event.getHarvester() == null || !OreTiersAPI.hasStage(event.getHarvester(), stageInfo.getFirst()))) {

            event.getDrops().clear();
            event.getDrops().addAll(stageInfo.getSecond().getBlock().getDrops(event.getWorld(), event.getPos(), stageInfo.getSecond(), event.getFortuneLevel()));
            event.setDropChance(ForgeEventFactory.fireBlockHarvesting(event.getDrops(), event.getWorld(), event.getPos(), stageInfo.getSecond(), event.getFortuneLevel(), event.getDropChance(), event.isSilkTouching(), event.getHarvester()));
        }
    }

    @SubscribeEvent()
    @SideOnly(Side.CLIENT)
    public void onStageSync (GameStageEvent.ClientSync event) {

        RenderUtils.markRenderersForReload(true);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onOverlayRendered (RenderGameOverlayEvent.Text event) {

        final Minecraft mc = Minecraft.getMinecraft();

        if (mc.gameSettings.showDebugInfo && event.getRight() != null) {

            for (final ListIterator<String> iterator = event.getRight().listIterator(); iterator.hasNext();) {

                final String line = iterator.next();

                for (final String string : OreTiersAPI.REPLACEMENT_IDS.keySet()) {

                    if (line.equalsIgnoreCase(string)) {

                        iterator.set(OreTiersAPI.REPLACEMENT_IDS.get(line));
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    @SideOnly(Side.CLIENT)
    public void onBlockHighlight (DrawBlockHighlightEvent event) {

    }
}
