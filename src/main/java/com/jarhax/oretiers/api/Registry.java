package com.jarhax.oretiers.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public final class Registry {

    /**
     * A map which links every stage, by name to an OreStage object.
     */
    public static final Map<String, OreStage> STAGE_MAP = new HashMap<>();

    /**
     * A list of blocks which shouldn't be skipped completely by our system. This can be used
     * for minor performance improvements (blacklisting stone and dirt), or to prevent any bugs
     * this mod may introduce. Not that the performance benefit of the blacklist is decreased
     * the more entries are added.
     */
    public static final List<IBlockState> BLACKLIST = new ArrayList<>();

    /**
     * Creates a new OreStage and registers it.
     *
     * @param stageName The name of the stage to create.
     * @return The newly created stage.
     */
    public static OreStage createStage (String stageName) {

        return STAGE_MAP.put(stageName, new OreStage(stageName));
    }

    /**
     * Adds a replacement for a block state.
     *
     * @param stage The stage to add the replacement to.
     * @param original The original block.
     * @param originalMeta The original block meta.
     * @param replacement The replacement block.
     * @param replacementMeta The replacement block meta.
     */
    public static void addReplacement (String stage, Block original, int originalMeta, Block replacement, int replacementMeta) {

        addReplacement(stage, original.getStateFromMeta(originalMeta), replacement.getStateFromMeta(replacementMeta));
    }

    /**
     * Adds a replacement for a block state.
     *
     * @param stage The stage to add the replacement to.
     * @param original The original block.
     * @param replacement The block to replace it with.
     */
    public static void addReplacement (String stage, Block original, Block replacement) {

        addReplacement(stage, original.getDefaultState(), replacement.getDefaultState());
    }

    /**
     * Adds a replacement for a block state.
     *
     * @param stage The stage to add the replacement to.
     * @param original The original block state.
     * @param replacement The state to replace it with.
     */
    public static void addReplacement (String stage, IBlockState original, IBlockState replacement) {

        getStage(stage).addReplacement(original, replacement);
    }

    /**
     * Checks if a state has a replacement for it.
     *
     * @param stage The stage to check.
     * @param state The state to check for.
     * @return Whether or not the state has a replacement.
     */
    public static boolean hasReplacement (String stage, IBlockState state) {

        return getStage(stage).hasReplacement(state);
    }

    /**
     * Gets the replacement block state for a given state.
     *
     * @param stage The stage to grab from.
     * @param state The state to get the replacement for.
     * @return The replacement state for the passed state.
     */
    public static IBlockState getReplacement (String stage, IBlockState state) {

        return getStage(stage).getReplacement(state);
    }

    /**
     * Gets an OreStage reference by it's name.
     *
     * @param stage The stage you want to get.
     * @return The OreStage associated with the passed tier.
     */
    public static OreStage getStage (String stage) {

        return STAGE_MAP.get(stage);
    }

    /**
     * Blacklists a block from the mod.
     *
     * @param block The block to blacklist.
     * @param meta The meta of the block.
     */
    public static void blacklist (Block block, int meta) {

        blacklist(block.getStateFromMeta(meta));
    }

    /**
     * Blacklists a block from the mod.
     *
     * @param block The block to blacklist.
     */
    public static void blacklist (Block block) {

        blacklist(block.getDefaultState());
    }

    /**
     * Blacklists a state from the mod.
     *
     * @param state The block state to blacklist.
     */
    public static void blacklist (IBlockState state) {

        BLACKLIST.add(state);
    }

    /**
     * Checks if a state has been blacklisted.
     *
     * @param state The state to check.
     * @return Whether or not the state was blacklisted.
     */
    public static boolean isBlacklisted (IBlockState state) {

        return BLACKLIST.contains(state);
    }
}
