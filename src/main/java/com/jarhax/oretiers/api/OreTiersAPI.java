package com.jarhax.oretiers.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.jarhax.oretiers.OreTiers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Tuple;

public final class OreTiersAPI {

    /**
     * A map which links every stage, by name to an OreStage object.
     */
    public static final Map<String, OreStage> STAGE_MAP = new HashMap<>();

    /**
     * A map which links block states to their stage key.
     */
    public static final Map<IBlockState, Tuple<String, IBlockState>> STATE_MAP = new HashMap<>();

    /**
     * A list of blocks which should be skipped completely by our system. This can be used for
     * minor performance improvements (blacklisting stone and dirt), or to prevent any bugs
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
    public static OreStage createStage (@Nonnull String stageName) {

        if (stageExists(stageName)) {

            OreTiers.log.info("Attempted to create stage %s however a stage with this name already exists!");
            return STAGE_MAP.get(stageName);
        }

        return STAGE_MAP.put(stageName, new OreStage(stageName));
    }

    /**
     * Removes an OreStage.
     *
     * @param stageName The name of the stage to remove.
     */
    public static void removeStage (@Nonnull String stageName) {

        STAGE_MAP.remove(stageName);
    }

    /**
     * Checks if a stage already exists.
     *
     * @param stageName The name of the stage.
     * @return Whether or not the stage exists.
     */
    public static boolean stageExists (@Nonnull String stageName) {

        return STAGE_MAP.containsKey(stageName);
    }

    /**
     * Gets an OreStage reference by it's name.
     *
     * @param stage The stage you want to get.
     * @return The OreStage associated with the passed tier.
     */
    public static OreStage getStage (@Nonnull String stage) {

        return STAGE_MAP.get(stage);
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
    public static void addReplacement (@Nonnull String stage, @Nonnull Block original, int originalMeta, @Nonnull Block replacement, int replacementMeta) {

        addReplacement(stage, original.getStateFromMeta(originalMeta), replacement.getStateFromMeta(replacementMeta));
    }

    /**
     * Adds a replacement for a block state.
     *
     * @param stage The stage to add the replacement to.
     * @param original The original block.
     * @param replacement The block to replace it with.
     */
    public static void addReplacement (@Nonnull String stage, @Nonnull Block original, @Nonnull Block replacement) {

        addReplacement(stage, original.getDefaultState(), replacement.getDefaultState());
    }

    /**
     * Adds a replacement for a block state.
     *
     * @param stage The stage to add the replacement to.
     * @param original The original block state.
     * @param replacement The state to replace it with.
     */
    public static void addReplacement (@Nonnull String stage, @Nonnull IBlockState original, @Nonnull IBlockState replacement) {

        if (hasReplacement(original)) {
            OreTiers.log.info(String.format("Attempted to register duplicate replacement for %s on stage %s. It will be replaced.", stage, original.toString()));
        }

        STATE_MAP.put(original, new Tuple<>(stage, replacement));
    }

    /**
     * Removes a replacement state.
     *
     * @param state The state to remove.
     */
    public static void removeReplacement (IBlockState state) {

        STATE_MAP.remove(state);
    }

    /**
     * Checks if a state has a replacement for it.
     *
     * @param stage The stage to check.
     * @param state The state to check for.
     * @return Whether or not the state has a replacement.
     */
    public static boolean hasReplacement (@Nonnull IBlockState state) {

        return STATE_MAP.containsKey(state);
    }

    /**
     * Blacklists a block from the mod.
     *
     * @param block The block to blacklist.
     * @param meta The meta of the block.
     */
    public static void blacklist (@Nonnull Block block, int meta) {

        blacklist(block.getStateFromMeta(meta));
    }

    /**
     * Blacklists a block from the mod.
     *
     * @param block The block to blacklist.
     */
    public static void blacklist (@Nonnull Block block) {

        blacklist(block.getDefaultState());
    }

    /**
     * Blacklists a state from the blacklist.
     *
     * @param state The block state to blacklist.
     */
    public static void blacklist (@Nonnull IBlockState state) {

        BLACKLIST.add(state);
    }

    /**
     * Removes a state from the blacklist.
     *
     * @param state The block state to remove from the blacklist.
     */
    public static void unBlacklist (@Nonnull IBlockState state) {

        BLACKLIST.remove(state);
    }

    /**
     * Checks if a state has been blacklisted.
     *
     * @param state The state to check.
     * @return Whether or not the state was blacklisted.
     */
    public static boolean isBlacklisted (@Nonnull IBlockState state) {

        return BLACKLIST.contains(state);
    }

    /**
     * Unlocks a stage for a player.
     *
     * @param player The player to unlock the stage for.
     * @param stage The stage to unlock.
     */
    public static void unlockStage (@Nonnull EntityPlayer player, @Nonnull String stage) {

        PlayerDataHandler.getHandler(player).unlockStage(stage);
    }

    /**
     * Checks if a player has a stage.
     *
     * @param player The player to check.
     * @param stage The stage to check for.
     * @return Whether or not the player has the stage.
     */
    public static boolean hasStage (@Nonnull EntityPlayer player, @Nonnull String stage) {

        return PlayerDataHandler.getHandler(player).hasUnlockedStage(stage);
    }
}
