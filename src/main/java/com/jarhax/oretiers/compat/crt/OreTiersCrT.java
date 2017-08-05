package com.jarhax.oretiers.compat.crt;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.jarhax.oretiers.api.OreTiersAPI;
import com.jarhax.oretiers.client.renderer.block.model.BakedModelTiered;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.CrafttweakerImplementationAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.darkhax.bookshelf.util.GameUtils;
import net.darkhax.bookshelf.util.RenderUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.OreTiers")
public class OreTiersCrT {

    @ZenMethod
    public static void addReplacement(String name, IIngredient original) {

        addReplacement(name, original, Blocks.STONE.getDefaultState());
    }

    @ZenMethod
    public static void addReplacement(String name, IIngredient original, IItemStack replacement) {

        addReplacement(name, original, getStateFromStack((ItemStack) replacement.getInternal()));
    }

    private static void addReplacement(String name, IIngredient original, IBlockState replacementState) {

        System.out.println("Adding CRT Replacement for " + original.toString());
        final Object internal = original.getInternal();
        if (internal instanceof Block) {
            CraftTweakerAPI.apply(new ActionAddReplacement(name, ((Block) internal).getDefaultState(), replacementState));
        } else if (internal instanceof ItemStack) {
            List<IBlockState> states = getStatesFromStack((ItemStack) internal);
            for (IBlockState state : states)
                CraftTweakerAPI.apply(new ActionAddReplacement(name, state, replacementState));
        } else if (internal instanceof String) {
            for (final ItemStack stack : OreDictionary.getOres((String) internal)) {
                CraftTweakerAPI.apply(new ActionAddReplacement(name, getStateFromStack(stack), replacementState));
            }
        }
    }

    private static List<IBlockState> getStatesFromStack(ItemStack stack) {
        final Block block = Block.getBlockFromItem(stack.getItem());
        if (stack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
            if(block instanceof BlockRedstoneOre) {
                return Lists.newArrayList(Blocks.REDSTONE_ORE.getDefaultState(),Blocks.LIT_REDSTONE_ORE.getDefaultState());
            }
            return block.getBlockState().getValidStates();
        }
        return Lists.newArrayList(getStateFromStack(stack));
    }

    private static IBlockState getStateFromStack(ItemStack stack) {
        final Block block = Block.getBlockFromItem(stack.getItem());
        return block != null ? block.getStateFromMeta(stack.getMetadata()) : Blocks.STONE.getDefaultState();
    }
}
