package net.darkhax.orestages.compat.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.WailaPlugin;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.bookshelf.util.StackUtils;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.orestages.api.OreTiersAPI;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@WailaPlugin
public class OreTiersProvider implements IWailaPlugin, IWailaDataProvider {
    
    @Override
    public ItemStack getWailaStack (IWailaDataAccessor accessor, IWailaConfigHandler config) {
        
        final Tuple<String, IBlockState> stageInfo = OreTiersAPI.getStageInfo(accessor.getBlockState());

        if (stageInfo != null && !GameStageHelper.clientHasStage(PlayerUtils.getClientPlayer(), stageInfo.getFirst())) {

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
    
    @Override
    public void register (IWailaRegistrar registrar) {
        
        registrar.registerStackProvider(this, Block.class);
    }
}