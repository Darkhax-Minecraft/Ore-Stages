package com.jarhax.oretiers.compat.crt.handlers;

import com.jarhax.oretiers.api.OreTiersAPI;
import com.jarhax.oretiers.compat.crt.util.BaseUndoableAction;

import minetweaker.MineTweakerAPI;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import minetweaker.mc1112.block.MCBlockDefinition;
import net.minecraft.block.state.IBlockState;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.OreTiers.Tiers")
public class OreTiersCrT {

    @ZenMethod
    public static void addStage (String name) {

        MineTweakerAPI.apply(new BaseUndoableAction() {
            @Override
            public void apply () {

                if (OreTiersAPI.getStage(name) == null)
                    OreTiersAPI.createStage(name);
            }

            @Override
            public String describe () {

                return "Adding stage: " + name;
            }
        });
    }

    @ZenMethod
    public static void addReplacement (String name, IIngredient original, IItemStack replacement) {

        final IBlockState replace = ((MCBlockDefinition) replacement.asBlock().getDefinition()).getInternalBlock().getStateFromMeta(replacement.getDamage());
        MineTweakerAPI.apply(new BaseUndoableAction() {
            @Override
            public void apply () {

                original.getItems().forEach(item -> {
                    final IBlockState origin = ((MCBlockDefinition) item.asBlock().getDefinition()).getInternalBlock().getStateFromMeta(item.getDamage());
                    if (!OreTiersAPI.hasReplacement(origin))
                        OreTiersAPI.addReplacement(name, origin, replace);
                });
            }

            @Override
            public String describe () {

                return "Adding replacement: " + name + "    " + original + " -> " + replace;
            }
        });
    }

    @ZenMethod
    public static void blacklist (IIngredient state) {

        MineTweakerAPI.apply(new BaseUndoableAction() {
            @Override
            public void apply () {

                state.getItems().forEach(item -> {
                    final IBlockState block = ((MCBlockDefinition) item.asBlock().getDefinition()).getInternalBlock().getStateFromMeta(item.getDamage());
                    if (!OreTiersAPI.isBlacklisted(block))
                        OreTiersAPI.blacklist(block);
                });

            }

            @Override
            public String describe () {

                return "Adding " + state + " to the blacklist";
            }
        });
    }
}
