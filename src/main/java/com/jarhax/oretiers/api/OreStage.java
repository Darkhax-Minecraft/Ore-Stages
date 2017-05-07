package com.jarhax.oretiers.api;

public class OreStage {

    /**
     * The name of the stage.
     */
    private String name;

    /**
     * Constructs a stage.
     *
     * @param name The name of the stage.
     */
    public OreStage (String name) {

        this.name = name;
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
}
