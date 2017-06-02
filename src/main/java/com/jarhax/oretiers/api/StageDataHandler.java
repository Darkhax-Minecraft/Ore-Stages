package com.jarhax.oretiers.api;

import net.darkhax.bookshelf.util.RenderUtils;
import net.darkhax.gamestages.capabilities.PlayerDataHandler.IAdditionalStageData;
import net.darkhax.gamestages.capabilities.PlayerDataHandler.IStageData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class StageDataHandler implements IAdditionalStageData {

    @Override
    public void writeNBT (EntityPlayer player, IStageData stageData, NBTTagCompound tag) {

        // No thanks
    }

    @Override
    public void readNBT (EntityPlayer player, IStageData stageData, NBTTagCompound tag) {

        // No Thanks
    }

    @Override
    public void onClientSync (EntityPlayer player, String stageName, boolean isUnlocking) {

        RenderUtils.markRenderersForReload(true);
    }
}
