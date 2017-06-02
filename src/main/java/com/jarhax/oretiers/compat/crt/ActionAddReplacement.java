package com.jarhax.oretiers.compat.crt;

import com.jarhax.oretiers.api.OreTiersAPI;

import minetweaker.IUndoableAction;
import net.minecraft.block.state.IBlockState;

public class ActionAddReplacement implements IUndoableAction {

    private final String stage;
    private final IBlockState original;
    private final IBlockState replacement;

    public ActionAddReplacement (String stage, IBlockState original, IBlockState replacement) {

        this.stage = stage;
        this.original = original;
        this.replacement = replacement;
    }

    @Override
    public void apply () {

        OreTiersAPI.addReplacement(this.stage, this.original, this.replacement);
    }

    @Override
    public String describe () {

        return String.format("Adding a replacement to %s. %s will become %s", this.stage, this.original.toString(), this.replacement.toString());
    }

    @Override
    public void undo () {

        OreTiersAPI.removeReplacement(this.original);
    }

    @Override
    public String describeUndo () {

        return String.format("Removed replacement %s from stage %s", this.original.toString(), this.stage);
    }

    @Override
    public boolean canUndo () {

        return true;
    }

    @Override
    public Object getOverrideKey () {

        return null;
    }
}
