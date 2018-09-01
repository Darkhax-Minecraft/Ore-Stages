package net.darkhax.orestages;

import java.util.ListIterator;

import net.darkhax.bookshelf.util.BlockUtils;
import net.darkhax.bookshelf.util.RenderUtils;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.gamestages.event.StagesSyncedEvent;
import net.darkhax.orestages.api.OreTiersAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OreTiersEventHandler {
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onHarvestCheck (PlayerEvent.HarvestCheck event) {
        
        final Tuple<String, IBlockState> stageInfo = OreTiersAPI.getStageInfo(event.getTargetBlock());
        
        if (stageInfo != null && !GameStageHelper.hasStage(event.getEntityPlayer(), stageInfo.getFirst())) {
            
            // Check if player can harvest the hidden block instead of this one.
            event.setCanHarvest(event.getEntityPlayer().canHarvestBlock(stageInfo.getSecond()));
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBlockRightClick (PlayerInteractEvent.RightClickBlock event) {
        
        final Tuple<String, IBlockState> stageInfo = OreTiersAPI.getStageInfo(event.getWorld().getBlockState(event.getPos()));
        
        if (stageInfo != null && (event.getEntityPlayer() == null || !GameStageHelper.hasStage(event.getEntityPlayer(), stageInfo.getFirst()))) {
            
            event.setUseBlock(Result.DENY);
        }
    }
    
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBlockBreak (BreakEvent event) {
        
        final Tuple<String, IBlockState> stageInfo = OreTiersAPI.getStageInfo(event.getState());
        
        // Checks if the block has a stage and the player is not valid to break it.
        if (stageInfo != null && (event.getPlayer() == null || !GameStageHelper.hasStage(event.getPlayer(), stageInfo.getFirst()))) {
            
            // Sets the EXP to drop to 0
            event.setExpToDrop(0);
            
            // Checks if the player can't harvest the original block, but they can harvest the
            // replacement block
            if (!ForgeHooks.canHarvestBlock(event.getState().getBlock(), event.getPlayer(), event.getWorld(), event.getPos()) && BlockUtils.canHarvestSafely(stageInfo.getSecond(), event.getPlayer())) {
                
                // Drops the replacement block instead
                BlockUtils.dropBlockSafely(event.getWorld(), event.getPlayer(), event.getPos(), stageInfo.getSecond());
            }
        }
    }
    
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBreakSpeed (BreakSpeed event) {
        
        try {
            
            final Tuple<String, IBlockState> stageInfo = OreTiersAPI.getStageInfo(event.getState());
            
            // Checks if the block has a stage and the player is not valid to break it.
            if (stageInfo != null && (event.getEntityPlayer() == null || !GameStageHelper.hasStage(event.getEntityPlayer(), stageInfo.getFirst()))) {
                
                // Sets the new break speed to match the replacement block
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
        
        // Checks if the block has a stage
        if (stageInfo != null) {
            
            // If player is null, and the block is on the non defaulting whitelist, ignore it.
            if (event.getHarvester() == null && OreTiersAPI.NON_DEFAULTING.contains(event.getState())) {
                
                return;
            }
            
            // If player is null, and the block is not on the whitelist, or the player doesn't
            // have the correct stage
            else if (event.getHarvester() == null || !GameStageHelper.hasStage(event.getHarvester(), stageInfo.getFirst())) {
                
                // Clear the drop list and add the correct drops
                event.getDrops().clear();
                
                final NonNullList<ItemStack> drops = NonNullList.create();
                stageInfo.getSecond().getBlock().getDrops(drops, event.getWorld(), event.getPos(), stageInfo.getSecond(), event.getFortuneLevel());
                event.getDrops().addAll(drops);
                
                // Reset the drop chance
                event.setDropChance(ForgeEventFactory.fireBlockHarvesting(event.getDrops(), event.getWorld(), event.getPos(), stageInfo.getSecond(), event.getFortuneLevel(), event.getDropChance(), event.isSilkTouching(), event.getHarvester()));
            }
        }
    }
    
    @SubscribeEvent()
    @SideOnly(Side.CLIENT)
    public void onStageSync (StagesSyncedEvent event) {
        
        // Reload chunk renderers for the player
        RenderUtils.markRenderersForReload(true);
    }
    
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onOverlayRendered (RenderGameOverlayEvent.Text event) {
        
        final Minecraft mc = Minecraft.getMinecraft();
        
        // Checks if the F3 menu is open
        if (mc.gameSettings.showDebugInfo && event.getRight() != null) {
            
            // Iterates through the debug menu
            for (final ListIterator<String> iterator = event.getRight().listIterator(); iterator.hasNext();) {
                
                final String line = iterator.next();
                
                if (line != null) {
                    
                    for (final String string : OreTiersAPI.REPLACEMENT_IDS.keySet()) {
                        
                        // Checks if the ID is a hidden ID
                        if (line.equalsIgnoreCase(string)) {
                            
                            // Replaces the block id with the hidden ID
                            iterator.set(OreTiersAPI.REPLACEMENT_IDS.get(line));
                            break;
                        }
                    }
                }
            }
        }
    }
}
