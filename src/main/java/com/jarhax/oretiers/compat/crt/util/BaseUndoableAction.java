package com.jarhax.oretiers.compat.crt.util;

import minetweaker.IUndoableAction;

public abstract class BaseUndoableAction implements IUndoableAction {

    @Override
    public boolean canUndo () {

        return true;
    }

    @Override
    public void undo () {

    }

    @Override
    public String describeUndo () {

        return "";
    }

    @Override
    public Object getOverrideKey () {

        return null;
    }
}
