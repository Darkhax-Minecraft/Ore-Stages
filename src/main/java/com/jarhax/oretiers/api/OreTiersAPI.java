package com.jarhax.oretiers.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.jarhax.oretiers.OreTiers;
import com.jarhax.oretiers.packet.PacketStage;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Tuple;

public final class OreTiersAPI {

    // TOOD move to bookshelf
    public static boolean requireReload = false;

    /**
     * A map which links every stage, by name to an OreStage object.
     */
    private static final Map<String, OreStage> STAGE_MAP = new HashMap<>();

    /**
     * A list of all relevant blockstates.
     */
    private static final List<IBlockState> RELEVANT_STATES = new ArrayList<>();

    /**
     * A map which links block states to their stage key.
     */
    private static final Map<IBlockState, Tuple<String, IBlockState>> STATE_MAP = new HashMap<>();

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

        addRelevantState(original);
        addRelevantState(replacement);
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
     * Unlocks a stage for a player.
     *
     * @param player The player to unlock the stage for.
     * @param stage The stage to unlock.
     */
    public static void unlockStage (@Nonnull EntityPlayer player, @Nonnull String stage) {

        PlayerDataHandler.getHandler(player).unlockStage(stage);

        if (player instanceof EntityPlayerMP) {

            OreTiers.network.sendTo(new PacketStage(stage, true), (EntityPlayerMP) player);
        }
    }

    /**
     * Locks a stage for a player.
     *
     * @param player The player to lock the stage for.
     * @param stage The stage to lcok.
     */
    public static void lockStage (@Nonnull EntityPlayer player, @Nonnull String stage) {

        PlayerDataHandler.getHandler(player).lockStage(stage);

        if (player instanceof EntityPlayerMP) {

            OreTiers.network.sendTo(new PacketStage(stage, false), (EntityPlayerMP) player);
        }
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

    /**
     * Gets a set of all states to be replaced/wrapped.
     *
     * @return A set of all the states to replace/wrap.
     */
    public static Set<IBlockState> getStatesToReplace () {

        return STATE_MAP.keySet();
    }

    /**
     * Gets a list of all the relevant blockstates. This is used internally for getting the
     * list of models to wrap. See
     * {@link com.jarhax.oretiers.OreTiersEventHandler#onModelBake(net.minecraftforge.client.event.ModelBakeEvent)}.
     *
     * @return A List of all the relevant states.
     */
    public static List<IBlockState> getRelevantStates () {

        return RELEVANT_STATES;
    }

    /**
     * Gets stage info from a blockstate.
     *
     * @param state The blockstate to get stage info for.
     * @return The stage info for the passed state.
     */
    @Nullable
    public static Tuple<String, IBlockState> getStageInfo (@Nonnull IBlockState state) {

        return STATE_MAP.get(state);
    }

    /**
     * Used internally add a relevant blockstate stage. Just a wrapper to prevent duplicate
     * entries.
     *
     * @param state The blockstate to add.
     */
    private static void addRelevantState (@Nonnull IBlockState state) {

        if (!RELEVANT_STATES.contains(state)) {

            RELEVANT_STATES.add(state);
        }
    }
}
