package net.darkhax.orestages.compat.crt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.darkhax.orestages.OreStages;
import net.darkhax.orestages.api.OreTiersAPI;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ActionAddReplacement implements IAction {

    private final String crtStage;
    private final IIngredient crtOriginal;
    private final IItemStack crtReplacement;
    
    private List<IBlockState> originals;
    private IBlockState replacement;
    
    public ActionAddReplacement(String name, IIngredient original) {
        
        this(name, original, null);
    }
    
    public ActionAddReplacement(String name, IIngredient original, IItemStack replacement) {
        
        checkBadIngredient(original);
        
        this.crtStage = name;
        this.crtOriginal = original;
        this.crtReplacement = replacement;
        this.originals = new ArrayList<>();
        
        getOriginals(this.crtOriginal);
        this.replacement = this.crtReplacement != null ? getStateFromStack((ItemStack) this.crtReplacement.getInternal()) : Blocks.STONE.getDefaultState();
    }


    @Override
    public void apply () {
        
        for (IBlockState original : originals) {
            
            OreTiersAPI.addReplacement(this.crtStage, original, this.replacement);
        }
    }

    @Override
    public String describe () {

        return String.format("Adding a replacement for stage %s. %s will become %s", this.crtStage, Arrays.toString(this.originals.toArray(new IBlockState[0])), this.replacement.toString());
    }
    
    private void getOriginals (IIngredient original) {

        final Object internal = original.getInternal();

        if (internal instanceof Block) {

            this.originals.add(((Block) internal).getDefaultState());
        }

        else if (internal instanceof ItemStack) {

            final List<IBlockState> states = getStatesFromStack((ItemStack) internal);

            for (final IBlockState state : states) {

                this.originals.add(state);
            }
        }

        else if (internal instanceof String) {

            for (final ItemStack stack : OreDictionary.getOres((String) internal)) {

                final List<IBlockState> states = getStatesFromStack(stack);

                for (final IBlockState state : states) {

                    this.originals.add(state);
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
    

    private static void checkBadIngredient (IIngredient ingredient) {

        if (ingredient == null) {

            throw new RuntimeException("Ingredient is null!");
        }

        else if (ingredient.getInternal() == Items.AIR || ingredient.getInternal() == Blocks.AIR) {

            throw new RuntimeException("Ingredient was air!");
        }

        else if (ingredient.getInternal() instanceof ItemStack && ((ItemStack) ingredient.getInternal()).isEmpty()) {

            throw new RuntimeException("Ingredient was an empty stack!");
        }

        else if (ingredient.getInternal() instanceof String) {

            boolean broken = false;

            for (final ItemStack stack : OreDictionary.getOres((String) ingredient.getInternal())) {

                if (stack.isEmpty()) {

                    OreStages.LOG.error("Found an empty oredict entry for " + ingredient.getInternal());
                    broken = true;
                }
            }

            if (broken) {

                throw new RuntimeException("Ore Dictionary Ingredient contains empty entries!");
            }
        }
    }
}