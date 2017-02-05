package com.jarhax.sevtechores.handler;

import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class WorldGenHandler {

	/**
	 * WARNING: This method is specified through a hard coded string in
	 * {@link com.jarhax.sevtechores.asm.ClassTransformerSevTechOres}. If this method is
	 * renamed, or has it's signature changed, the ASM for this method will break. This
	 * includes things like changing package names, the descriptor, and so on. So don't touch
	 * it!
	 *
	 * Called at the start of {@link World#setBlockState(BlockPos, IBlockState, int)}. Allows
	 * for the block being placed to be changed, or modified.
	 *
	 * @param world The world the block is being placed in.
	 * @param pos The position of the block being placed.
	 * @param newState The state being placed.
	 * @param flags The placement flags.
	 * @return Whether or not the vanilla behavior should be ignored. If true is returned,
	 *         {@link World#setBlockState(BlockPos, IBlockState, int)} will return false.
	 */
	public static boolean preBlockSet (World world, BlockPos pos, IBlockState newState, int flags) {

		// TODO replace with the actual tile code. This is a test placeholder.
		if (isOre(newState, true)) {

			world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState(), flags);
			System.out.println(newState + " placed at " + pos);
			return true;
		}

		return false;
	}

	/**
	 * Checks if a block is an ore or not. An ore is considered any block which extends
	 * {@link net.minecraft.block.BlockOre}, has an ore dict entry which starts with ore.
	 * Optionally, the display name of the block can also be checked.
	 *
	 * @param state The state/block to check.
	 * @param checkName Whether or not the display name should be checked.
	 * @return Whether or not the block was an ore.
	 */
	public static boolean isOre (IBlockState state, boolean checkName) {

		if (state == null || state.getBlock() == null)
			return false;

		if (state.getBlock() instanceof BlockOre)
			return true;

		final ItemStack stack = new ItemStack(Item.getItemFromBlock(state.getBlock()), 1, state.getBlock().getMetaFromState(state));

		if (stack != null && stack.getItem() != null) {
			for (final int oreID : OreDictionary.getOreIDs(stack))
				if (OreDictionary.getOreName(oreID).startsWith("ore"))
					return true;

			if (checkName && stack.getItem().getItemStackDisplayName(stack).matches(".*(^|\\s)([oO]re)($|\\s)."))
				return true;
		}

		return false;
	}
}
