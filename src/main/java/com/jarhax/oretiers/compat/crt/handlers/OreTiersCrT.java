package com.jarhax.oretiers.compat.crt.handlers;

import com.jarhax.oretiers.api.OreTiersAPI;
import com.jarhax.oretiers.compat.crt.util.BaseUndoableAction;
import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IItemStack;
import minetweaker.mc1112.block.MCBlockDefinition;
import net.minecraft.block.state.IBlockState;
import stanhebben.zenscript.annotations.*;

@ZenClass("mods.OreTiers.Tiers")
public class OreTiersCrT {
    
    @ZenMethod
    public static void addStage(String name) {
        
        MineTweakerAPI.apply(new BaseUndoableAction() {
            @Override
            public void apply() {
                
                if(OreTiersAPI.getStage(name) == null)
                    OreTiersAPI.createStage(name);
            }
            
            @Override
            public String describe() {
                
                return "Adding stage: " + name;
            }
        });
    }
    
    @ZenMethod
    public static void addReplacement(String name, IItemStack original, IItemStack replacement) {
        IBlockState origin = ((MCBlockDefinition) original.asBlock().getDefinition()).getInternalBlock().getStateFromMeta(original.getDamage());
        IBlockState replace = ((MCBlockDefinition) replacement.asBlock().getDefinition()).getInternalBlock().getStateFromMeta(replacement.getDamage());
        MineTweakerAPI.apply(new BaseUndoableAction() {
            @Override
            public void apply() {
                if(!OreTiersAPI.hasReplacement(name, origin)) {
                    OreTiersAPI.addReplacement(name, origin, replace);
                }
            }
            
            @Override
            public String describe() {
                return "Adding replacement: " + name + "    " + origin + " -> " + replace;
            }
        });
    }
    
    @ZenMethod
    public static void blacklist(IItemStack state) {
        IBlockState block = ((MCBlockDefinition)state.asBlock().getDefinition()).getInternalBlock(). getStateFromMeta(state.getDamage());
        MineTweakerAPI.apply(new BaseUndoableAction() {
            @Override
            public void apply() {
                if(!OreTiersAPI.isBlacklisted(block)){
                    OreTiersAPI.blacklist(block);
                }
            }
    
            @Override
            public String describe() {
                return "Adding " + block + " to the blacklist";
            }
        });
    }
}
