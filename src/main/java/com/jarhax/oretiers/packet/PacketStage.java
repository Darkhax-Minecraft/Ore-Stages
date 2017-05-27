package com.jarhax.oretiers.packet;

import com.jarhax.oretiers.api.OreTiersAPI;

import net.darkhax.bookshelf.network.SerializableMessage;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * A packet for syncing a stage add/remove action with the client. It uses
 * {@code net.darkhax.bookshelf.network.SerializableMessage} to handle the IO automagically.
 */
public class PacketStage extends SerializableMessage {

    /**
     * The serial ID. Please update this when fields are added/removed, or other big changes
     * happen. Not that important though.
     */
    private static final long serialVersionUID = 333083516838356520L;

    /**
     * The name of the stage to modify.
     */
    public String stageName;

    /**
     * Whether or not the stage is being added or removed.
     */
    public boolean unlock;

    /**
     * Empty constructor, required by forge's system.
     */
    public PacketStage () {

    }

    /**
     * Constructor for the packet.
     *
     * @param stageName The name of the stage to modify.
     * @param unlock Whether or not the stage is being added or removed.
     */
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
