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

        return "Creating ore stage: " + this.stageName;
    }
}