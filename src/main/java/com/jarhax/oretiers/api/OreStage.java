package com.jarhax.oretiers.api;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.state.IBlockState;

public class OreStage {

    /**
     * A map of state replacements.
     */
    private final Map<IBlockState, IBlockState> stateReplacement;

    /**
     * The name of the stage.
     */
    private String name;

    /**
     * Constructs a stage.
     *
     * @param tier The tier of the stage.
     * @param name The name of the stage.
     */
    public OreStage (String name) {

        this.name = name;
        this.stateReplacement = new HashMap<>();
    }

    /**
     * Gets the name of the stage.
     *
     * @return The name of the stage.
     */
    public String getName () {

        return this.name;
    }

    /**
     * Sets the name of the stage.
     *
     * @param name The new name for the stage.
     */
    public void setName (String name) {

        this.name = name;
    }

    /**
     * Adds a replacement to the stage.
     *
     * @param original The original state.
     * @param replacement The state to replace it with.
     */
    public void addReplacement (IBlockState original, IBlockState replacement) {

        this.stateReplacement.put(original, replacement);
    }

    /**
     * Checks if a state has a replacement.
     *
     * @param state The state to check for.
     * @return Whether or not there is a replacement.
     */
    public boolean hasReplacement (IBlockState state) {

        return this.stateReplacement.containsKey(state);
    }

    /**
     * Gets the replacement for a state.
     *
     * @param state The state to get the replacement of.
     * @return The replacement state.
     */
    public IBlockState getReplacement (IBlockState state) {

        return this.stateReplacement.get(state);
    }
}
