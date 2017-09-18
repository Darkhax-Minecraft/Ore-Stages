package net.darkhax.orestages.compat.crt;

import java.util.List;

import com.google.common.collect.Lists;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.OreTiers")
public class OreTiersCrT {

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

            CraftTweakerAPI.apply(new ActionAddReplacement(name, ((Block) internal).getDefaultState(), replacementState));
        }

        else if (internal instanceof ItemStack) {

            final List<IBlockState> states = getStatesFromStack((ItemStack) internal);

            for (final IBlockState state : states) {

                CraftTweakerAPI.apply(new ActionAddReplacement(name, state, replacementState));
            }
        }

        else if (internal instanceof String) {

            for (final ItemStack stack : OreDictionary.getOres((String) internal)) {

                final List<IBlockState> states = getStatesFromStack(stack);

                for (final IBlockState state : states) {

                    CraftTweakerAPI.apply(new ActionAddReplacement(name, state, replacementState));
                }
            }
        }
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
