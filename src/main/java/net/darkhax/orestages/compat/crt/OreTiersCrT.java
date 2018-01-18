package net.darkhax.orestages.compat.crt;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods.OreTiers")
public class OreTiersCrT {

    @ZenMethod
    public static void addReplacement (String stage, IIngredient original) {

        CraftTweakerAPI.apply(new ActionAddReplacement(stage, original));
    }

    @ZenMethod
    public static void addReplacement (String stage, IIngredient original, IItemStack replacement) {

        CraftTweakerAPI.apply(new ActionAddReplacement(stage, original, replacement));
    }

    @ZenMethod
    public static void addReplacementById (String stage, String original, String replacement) {

        CraftTweakerAPI.apply(new ActionAddReplacement(stage, original, replacement));
    }
}
