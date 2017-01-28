package com.jarhax.sevtechores.blocks;

import com.jarhax.sevtechores.tileentities.TileEntityOre;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;

import javax.annotation.Nullable;
import java.util.*;

public class BlockOre extends Block implements ITileEntityProvider {
	
	public BlockOre() {
		super(Material.ROCK);
		
		setHarvestLevel("pickaxe", 0);
	}
	
	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
		return ((TileEntityOre) worldIn.getTileEntity(pos)).getStoredOre().getHardness();
	}
	
	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		if(player.isCreative()){
			return true;
		}
		ItemStack activeStack = player.getActiveItemStack();
		if(activeStack != null && activeStack.getItem().getToolClasses(activeStack).contains("pickaxe")) {
			if(activeStack.getItem().getHarvestLevel(activeStack, "pickaxe", player, state) >= ((TileEntityOre) world.getTileEntity(pos)).getStoredOre().getHarvestLevel()) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		super.breakBlock(worldIn, pos, state);
	}
	
	@Nullable
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return super.getDrops(world, pos, state, fortune);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileEntityOre();
	}
}
