package com.jarhax.oretiers.compat.crt;

import java.util.Map;
import java.util.Map.Entry;

import com.jarhax.oretiers.api.OreTiersAPI;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.MineTweakerImplementationAPI.ReloadEvent;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.OreTiers")
public class OreTiersCrT {

    public static void init () {

        System.out.println("Registering CRT stuff");
        MineTweakerAPI.registerClass(OreTiersCrT.class);

        MineTweakerImplementationAPI.onReloadEvent( (e) -> preLoad(e));
        MineTweakerImplementationAPI.onPostReload( (e) -> postLoad(e));
    }

    public static void preLoad (ReloadEvent event) {

        OreTiersAPI.enableReload();
    }

    public static void postLoad (ReloadEvent event) {

        final Map<IBlockState, Tuple<String, IBlockState>> differences = OreTiersAPI.getKnownDiferences();

        if (!differences.isEmpty()) {

            for (final Entry<IBlockState, Tuple<String, IBlockState>> entry : differences.entrySet()) {

                // System.out.println("Replacing " + entry.getKey().toString());
                // OreTiers.NETWORK.sendToAll(new
                // PacketReloadModels(entry.getValue().getFirst(), entry.getKey(),
                // entry.getValue().getSecond()));
            }

            // OreTiers.NETWORK.sendToAll(new PacketRequestClientRefresh());
        }

        OreTiersAPI.disableReload();
    }

    @ZenMethod
    public static void addReplacement (String name, IIngredient original) {

        addReplacement(name, original, Blocks.STONE.getDefaultState());
    }

    @ZenMethod
    public static void addReplacement (String name, IIngredient original, IItemStack replacement) {

        addReplacement(name, original, getStateFromStack((ItemStack) replacement.getInternal()));
    }

    private static void addReplacement (String name, IIngredient original, IBlockState replacementState) {

        final Object internal = original.getInternal();

        if (internal instanceof Block) {
            MineTweakerAPI.apply(new ActionAddReplacement(name, ((Block) internal).getDefaultState(), replacementState));
        }
        else if (internal instanceof ItemStack) {
            MineTweakerAPI.apply(new ActionAddReplacement(name, getStateFromStack((ItemStack) internal), replacementState));
        }
        else if (internal instanceof String) {
            for (final ItemStack stack : OreDictionary.getOres((String) internal)) {
                MineTweakerAPI.apply(new ActionAddReplacement(name, getStateFromStack(stack), replacementState));
            }
        }
    }

    private static IBlockState getStateFromStack (ItemStack stack) {

        final Block block = Block.getBlockFromItem(stack.getItem());
        return block != null ? block.getStateFromMeta(stack.getMetadata()) : Blocks.STONE.getDefaultState();
    }
}
