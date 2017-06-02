package com.jarhax.oretiers.packet;

import net.darkhax.bookshelf.network.SerializableMessage;
import net.darkhax.bookshelf.util.RenderUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketRequestClientRefresh extends SerializableMessage {

    public PacketRequestClientRefresh () {

    }

    @Override
    public IMessage handleMessage (MessageContext context) {

        RenderUtils.markRenderersForReload(true);
        return null;
    }
}
