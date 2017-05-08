package com.jarhax.oretiers.packet;

import com.jarhax.oretiers.api.OreTiersAPI;

import net.darkhax.bookshelf.network.SerializableMessage;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketStage extends SerializableMessage {

    private static final long serialVersionUID = 333083516838356520L;

    public String stageName;

    public boolean unlock;

    public PacketStage () {

    }

    public PacketStage (String stageName, boolean unlock) {

        this.stageName = stageName;
        this.unlock = unlock;
    }

    @Override
    public IMessage handleMessage (MessageContext context) {

        if (this.unlock) {

            OreTiersAPI.unlockStage(PlayerUtils.getClientPlayer(), this.stageName);
        }

        else {

            OreTiersAPI.lockStage(PlayerUtils.getClientPlayer(), this.stageName);
        }

        OreTiersAPI.requireReload = true;
        return null;
    }
}
