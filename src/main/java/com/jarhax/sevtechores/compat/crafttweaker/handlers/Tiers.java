package com.jarhax.sevtechores.compat.crafttweaker.handlers;


import com.jarhax.sevtechores.api.OreRegistry;
import com.jarhax.sevtechores.api.ores.OreTier;
import minetweaker.*;
import minetweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.*;

import java.util.Arrays;

@ZenClass("mods.oretiers.Tiers")
public class Tiers {
    
    @ZenMethod
    public static void addTier(String tierName, IItemStack[] ores) {
        ItemStack[] items = new ItemStack[ores.length];
        for(int i = 0; i < ores.length; i++) {
            items[i] = (ItemStack) ores[i].getInternal();
        }
        MineTweakerAPI.apply(new Add(tierName, items));
    }
    
    @ZenMethod
    public static void removeTier(String tierName) {
        MineTweakerAPI.apply(new Remove(tierName));
    }
    
    public static class Remove implements IUndoableAction {
        
        private String tierName;
        private OreTier oreTier;
    
        public Remove(String tierName) {
            this.tierName = tierName;
        }
    
        @Override
        public void apply() {
            OreTier tier = OreRegistry.removeTier(tierName);
            if(tier != null) {
                oreTier = tier;
            } else {
                MineTweakerAPI.logInfo("Unable to remove ");
            }
        }
        
        @Override
        public boolean canUndo() {
            return true;
        }
        
        @Override
        public void undo() {
            OreRegistry.registerTier(oreTier);
        }
        
        @Override
        public String describe() {
            return "Removing a Tier called: " + tierName;
        }
        
        @Override
        public String describeUndo() {
            return "Creating a Tier called: " + tierName;
        }
        
        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
    
    private static class Add implements IUndoableAction {
        
        private String tierName;
        private ItemStack[] tierOres;
        
        public Add(String tierName, ItemStack[] tierOres) {
            this.tierName = tierName;
            this.tierOres = tierOres;
        }
        
        @Override
        public void apply() {
            OreRegistry.registerTier(new OreTier(tierName, Arrays.asList(tierOres)));
        }
        
        @Override
        public boolean canUndo() {
            return true;
        }
        
        @Override
        public void undo() {
            OreRegistry.removeTier(tierName);
        }
        
        @Override
        public String describe() {
            return "Creating a Tier called: " + tierName;
        }
        
        @Override
        public String describeUndo() {
            return "Removing a Tier called: " + tierName;
        }
        
        @Override
        public Object getOverrideKey() {
            return null;
        }
    }
}
