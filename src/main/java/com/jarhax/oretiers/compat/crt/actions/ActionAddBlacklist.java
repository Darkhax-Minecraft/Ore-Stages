package com.jarhax.oretiers.compat.crt.actions;

import com.jarhax.oretiers.api.OreTiersAPI;

import net.minecraft.block.state.IBlockState;

public class ActionAddBlacklist extends BaseUndoableAction {

    private final IBlockState state;

    public ActionAddBlacklist (IBlockState state) {

        this.state = state;
    }

    @Override
    public void apply () {

        OreTiersAPI.blacklist(this.state);
    }

    @Override
    public String describe () {

        return String.format("Added %s to the blacklist.", this.state.toString());
    }

    @Override
    public void undo () {

        OreTiersAPI.unBlacklist(this.state);
    }

    @Override
    public String describeUndo () {

        return String.format("Removed %s from the blacklist.", this.state.toString());
    }
}