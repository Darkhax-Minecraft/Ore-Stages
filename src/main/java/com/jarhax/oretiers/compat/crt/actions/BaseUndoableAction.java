package com.jarhax.oretiers.compat.crt.actions;

import minetweaker.IUndoableAction;

public abstract class BaseUndoableAction implements IUndoableAction {

    @Override
    public boolean canUndo () {

        return true;
    }

    @Override
    public Object getOverrideKey () {

        return null;
    }
}
