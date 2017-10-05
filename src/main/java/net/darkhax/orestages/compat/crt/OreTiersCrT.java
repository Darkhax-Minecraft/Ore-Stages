package net.darkhax.orestages.compat.crt;

import java.util.List;

import com.google.common.collect.Lists;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.darkhax.orestages.OreStages;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.OreTiers")
public class OreTiersCrT {

    @ZenMethod
    public static void addReplacement (String name, IIngredient original) {

        CraftTweakerAPI.apply(new ActionAddReplacement(name, original));
    }

    @ZenMethod
    public static void addReplacement (String name, IIngredient original, IItemStack replacement) {

        CraftTweakerAPI.apply(new ActionAddReplacement(name, original, replacement));
    }
}
