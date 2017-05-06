package com.jarhax.oretiers.packet;

import com.jarhax.oretiers.api.OreTiersAPI;

import net.darkhax.bookshelf.network.SerializableMessage;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUnlockStage extends SerializableMessage {

    private static final long serialVersionUID = 333083516838356520L;

    public String stageName;

    public PacketUnlockStage () {

    }

    public PacketUnlockStage (String stageName) {

        this.stageName = stageName;
    }

    @Override
    public IMessage handleMessage (MessageContext context) {

        OreTiersAPI.unlockStage(PlayerUtils.getClientPlayer(), this.stageName);
        Minecraft.getMinecraft().renderGlobal.loadRenderers();
        return null;
    }
}
