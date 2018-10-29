package net.darkhax.orestages.compat.crt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.math.NumberUtils;

import com.google.common.collect.Lists;

import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.darkhax.orestages.api.OreTiersAPI;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ActionAddReplacement implements IAction {
    
    private final String stage;
    private final List<IBlockState> originals;
    private final List<IBlockState> replacements;
    private final boolean allowDefaultDrop;
    
    public ActionAddReplacement (String stage, String original, String replacement, boolean defDrop) {
        
        this(stage, getState(original), getState(replacement), defDrop);
    }
    
    public ActionAddReplacement (String stage, IIngredient original, boolean defDrop) {
        
        this(stage, original, null, defDrop);
    }
    
    public ActionAddReplacement (String stage, IIngredient original, IItemStack replacement, boolean defDrop) {
        
        this(stage, getStatesFromIngredient(original), replacement != null ? getStatesFromStack(CraftTweakerMC.getItemStack(replacement)) : Arrays.asList(Blocks.STONE.getDefaultState()), defDrop);
    }
    
    public ActionAddReplacement (String stage, List<IBlockState> originals, List<IBlockState> replacements, boolean defDrop) {
        
        this.stage = stage;
        this.originals = originals;
        this.replacements = replacements;
        this.allowDefaultDrop = defDrop;
    }
    
    @Override
    public void apply () {
        
        if (this.originals.isEmpty()) {
            
            throw new IllegalArgumentException("No valid blocks to replace!");
        }
        
        if (this.replacements.isEmpty()) {
            
            throw new IllegalArgumentException("No valid blocks to replace with!");
        }
        
        for (final IBlockState original : this.originals) {
            
            for (final IBlockState replacement : this.replacements) {
                
                if (original == replacement) {
                    
                    throw new IllegalArgumentException("You can not replace a block with itself!");
                }
                
                OreTiersAPI.addReplacement(this.stage, original, replacement, this.allowDefaultDrop);
            }
        }
    }
    
    @Override
    public String describe () {
        
        return String.format("Adding a replacement for stage %s. %s will become %s", this.stage, listToString(this.originals), listToString(this.replacements));
    }
    
    private static String listToString (List<?> list) {
        
        return list.stream().map(Object::toString).collect(Collectors.joining(", "));
    }
    
    private static List<IBlockState> getStatesFromIngredient (IIngredient original) {
        
        final List<IBlockState> states = new ArrayList<>();
        
        if (original != null) {
          
            for (final ItemStack stack : CraftTweakerMC.getItemStacks(original.getItems())) {
                
                states.addAll(getStatesFromStack(stack));
            }
        }
        
        return states;
    }
    
    private static List<IBlockState> getStatesFromStack (ItemStack stack) {
        
        final Block block = Block.getBlockFromItem(stack.getItem());
        
        // Special case for redstone ore
        if (block == Blocks.REDSTONE_ORE) {
            
            return Lists.newArrayList(Blocks.REDSTONE_ORE.getDefaultState(), Blocks.LIT_REDSTONE_ORE.getDefaultState());
        }
        
        // Special case for wildcard meta
        if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
            
            return block.getBlockState().getValidStates();
        }
        
        return Lists.newArrayList(getStateFromStack(stack));
    }
    
    private static IBlockState getStateFromStack (ItemStack stack) {
        
        final Block block = Block.getBlockFromItem(stack.getItem());
        return block != null ? block.getStateFromMeta(stack.getMetadata()) : Blocks.STONE.getDefaultState();
    }
    
    private static NonNullList<IBlockState> getState (String string) {
        
        final NonNullList<IBlockState> states = NonNullList.create();
        
        final String[] parts = string.split(":");
        
        // Only two or 3 arguments is valid.
        if (parts.length < 2 || parts.length > 3) {
            
            throw new IllegalArgumentException("Invalid block ID. Format is modid:blockid:meta The meta is optional!");
        }
        
        // Gets block and checks if it is valid
        final Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(parts[0], parts[1]));
        
        if (block == null) {
            
            throw new IllegalArgumentException("No block found for " + parts[0] + ":" + parts[1]);
        }
        
        int meta = 0;
        
        // Check if meta arg exists
        if (parts.length == 3) {
            
            // Handles non integer strings
            if (!NumberUtils.isCreatable(parts[2])) {
                
                // Convert * to the wildcard meta
                if ("*".equalsIgnoreCase(parts[2])) {
                    
                    meta = OreDictionary.WILDCARD_VALUE;
                }
                
                // Not a valid argument
                else {
                    
                    throw new IllegalArgumentException("Invalid meta for " + string + ". " + parts[2] + " is not a valid meta!");
                }
            }
            
            // Converts meta string to integer
            else {
                
                meta = Integer.parseInt(parts[2]);
            }
        }
        
        // If wildcard, add all valid states.
        if (meta == OreDictionary.WILDCARD_VALUE) {
            
            states.addAll(block.getBlockState().getValidStates());
        }
        
        // Add just the requested state.
        else {
            
            states.add(block.getStateFromMeta(meta));
        }
        
        return states;
    }
}