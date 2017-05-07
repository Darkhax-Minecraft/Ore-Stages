package com.jarhax.oretiers.compat.crt.actions;

import com.jarhax.oretiers.api.OreTiersAPI;

public class ActionAddStage extends BaseUndoableAction {

    private final String stageName;

    public ActionAddStage (String stageName) {

        this.stageName = stageName;
    }

    @Override
    public void apply () {

        OreTiersAPI.createStage(this.stageName);
    }

    @Override
    public String describe () {

        return String.format("Creating ore stage: %s", this.stageName);
    }

    @Override
    public void undo () {

        OreTiersAPI.removeStage(this.stageName);
    }

    @Override
    public String describeUndo () {

        return String.format("Removing ore stage: %s", this.stageName);
    }
}