package com.jarhax.oretiers.compat.crt;

import java.util.Map;
import java.util.Map.Entry;

import com.jarhax.oretiers.api.OreTiersAPI;
import com.jarhax.oretiers.client.renderer.block.model.BakedModelTiered;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.MineTweakerImplementationAPI.ReloadEvent;
import minetweaker.api.item.IIngredient;
import minetweaker.api.item.IItemStack;
import net.darkhax.bookshelf.util.GameUtils;
import net.darkhax.bookshelf.util.RenderUtils;
import net.minecraft.block.Block;
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

@ZenClass("mods.OreTiers")
public class OreTiersCrT {

    public static void init () {

        MineTweakerAPI.registerClass(OreTiersCrT.class);

        if (GameUtils.isClient()) {

            MineTweakerImplementationAPI.onReloadEvent( (e) -> preLoad(e));
            MineTweakerImplementationAPI.onPostReload( (e) -> postLoad(e));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void preLoad (ReloadEvent event) {

        OreTiersAPI.enableReload();
    }

    @SideOnly(Side.CLIENT)
    public static void postLoad (ReloadEvent event) {

        boolean requireReload = false;

        // Check to see what was removed by the CrT reload
        final Map<IBlockState, Tuple<String, IBlockState>> removals = OreTiersAPI.getKnownRemovals();

        if (!removals.isEmpty()) {

            for (final Entry<IBlockState, Tuple<String, IBlockState>> entry : removals.entrySet()) {

                final IBakedModel model = RenderUtils.getModelForState(entry.getKey());

                if (model instanceof BakedModelTiered) {

                    final BakedModelTiered tierModel = (BakedModelTiered) model;
                    RenderUtils.setModelForState(entry.getKey(), tierModel.getOriginal());
                }
            }

            requireReload = true;
        }

        // Check to see what was added by the CrT reload
        final Map<IBlockState, Tuple<String, IBlockState>> differences = OreTiersAPI.getKnownDiferences();

        if (!differences.isEmpty()) {

            for (final Entry<IBlockState, Tuple<String, IBlockState>> entry : differences.entrySet()) {

                RenderUtils.setModelForState(entry.getKey(), new BakedModelTiered(entry.getValue().getFirst(), entry.getKey(), entry.getValue().getSecond()));
            }

            requireReload = true;
        }

        // If any render changes happened, mark chunk renderers for reload.
        if (requireReload) {

            RenderUtils.markRenderersForReload(true);
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
