package com.jarhax.oretiers.api;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class OreWrapper {

	private final IBlockState targetState;
	private final IBlockState replacementState;

	public OreWrapper(ItemStack stack) {

		this(Block.getBlockFromItem(stack.getItem()), stack.getMetadata());
	}

	public OreWrapper(Block block, int meta) {

		this(block.getStateFromMeta(meta));
	}

	public OreWrapper(Block block) {

		this(block.getDefaultState());
	}

	public OreWrapper(IBlockState targetState) {

		this(targetState, Blocks.STONE.getDefaultState());
	}

	public OreWrapper(IBlockState targetState, IBlockState replacementState) {

		this.targetState = targetState;
		this.replacementState = replacementState;
	}

	public IBlockState getReplacementState() {
		return replacementState;
	}

	public IBlockState getTargetState() {
		return targetState;
	}
}
