package com.jarhax.oretiers.packet;

import com.jarhax.oretiers.client.renderer.block.model.BakedModelTiered;

import net.darkhax.bookshelf.network.SerializableMessage;
import net.darkhax.bookshelf.util.RenderUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketReloadModels extends SerializableMessage {

    String stage;
    IBlockState original;
    IBlockState replacement;

    public PacketReloadModels () {

    }

    public PacketReloadModels (String stage, IBlockState original, IBlockState replacement) {

        this.stage = stage;
        this.original = original;
        this.replacement = replacement;
    }

    @Override
    public IMessage handleMessage (MessageContext context) {

        System.out.println("Client event recieved for " + this.original.toString());
        RenderUtils.setModelForState(this.original, new BakedModelTiered(this.stage, this.original, this.replacement));
        return null;
    }
}
