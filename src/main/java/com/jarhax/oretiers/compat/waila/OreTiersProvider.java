package com.jarhax.oretiers.compat.waila;

import java.util.List;

import com.jarhax.oretiers.api.OreTiersAPI;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.bookshelf.util.StackUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OreTiersProvider implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack (IWailaDataAccessor accessor, IWailaConfigHandler config) {

        final Tuple<String, IBlockState> stageInfo = OreTiersAPI.getStageInfo(accessor.getBlockState());

        if (stageInfo != null && !OreTiersAPI.hasStage(PlayerUtils.getClientPlayer(), stageInfo.getFirst())) {

            return StackUtils.getStackFromState(stageInfo.getSecond(), 1);
        }

        return accessor.getStack();
    }

    @Override
    public List<String> getWailaHead (ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

        return currenttip;
    }

    @Override
    public List<String> getWailaBody (ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

        return currenttip;
    }

    @Override
    public List<String> getWailaTail (ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData (EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos) {

        return tag;
    }

    public static void register (IWailaRegistrar register) {

        final OreTiersProvider provider = new OreTiersProvider();
        register.registerStackProvider(provider, Block.class);
    }
}