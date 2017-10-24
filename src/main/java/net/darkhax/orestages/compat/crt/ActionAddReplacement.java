package net.darkhax.orestages.compat.crt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import net.minecraftforge.oredict.OreDictionary;

public class ActionAddReplacement implements IAction {

    private final String stage;   
    private final List<IBlockState> originals;
    private final List<IBlockState> replacements;
    
    public ActionAddReplacement(String stage, IIngredient original) {
        
        this(stage, original, null);
    }
    
    public ActionAddReplacement(String stage, IIngredient original, IItemStack replacement) {
        
        this(stage, getStatesFromIngredient(original), (replacement != null ? getStatesFromStack(CraftTweakerMC.getItemStack(replacement)) : Arrays.asList(Blocks.STONE.getDefaultState())));
    }
    
    public ActionAddReplacement(String stage, List<IBlockState> originals, List<IBlockState> replacements) {
    	
    	this.stage = stage;
    	this.originals = originals;
    	this.replacements = replacements;
    }

    @Override
    public void apply () {
        
    	if (this.originals.isEmpty()) {
    		
    		throw new IllegalArgumentException("No valid blocks to replace!");
    	}
    	
    	if (this.replacements.isEmpty()) {
    		
    		throw new IllegalArgumentException("No valid blocks to replace with!");
    	}
    	
        for (IBlockState original : originals) {
            
        	for (IBlockState replacement : replacements) {
        		
                OreTiersAPI.addReplacement(this.stage, original, replacement);
        	}
        }
    }

    @Override
    public String describe () {

        return String.format("Adding a replacement for stage %s. %s will become %s", this.stage, listToString(this.originals), listToString(this.replacements));
    }
    
    private static String listToString(List<?> list) {
    	
    	return list.stream().map(Object::toString).collect(Collectors.joining(", "));
    }
    
    private static List<IBlockState> getStatesFromIngredient (IIngredient original) {

    	final List<IBlockState> states = new ArrayList<>();
    	
    	for (ItemStack stack : CraftTweakerMC.getItemStacks(original.getItems())) {
    		
    		states.addAll(getStatesFromStack(stack));
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
}