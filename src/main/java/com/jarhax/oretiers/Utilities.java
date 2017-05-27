package com.jarhax.oretiers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Utilities {

    public static float getBlockStrengthSafely (IBlockState state, EntityPlayer player, World world, BlockPos pos) {

        final float hardness = state.getBlockHardness(world, pos);
        if (hardness < 0.0F) {
            return 0.0F;
        }

        if (!canHarvestBlockSafely(state, player, world, pos)) {
            return player.getDigSpeed(state, pos) / hardness / 100F;
        }
        else {
            return player.getDigSpeed(state, pos) / hardness / 30F;
        }
    }

    public static boolean canHarvestBlockSafely (IBlockState state, EntityPlayer player, World world, BlockPos pos) {

        final Block block = state.getBlock();
        state = state.getBlock().getActualState(state, world, pos);
        if (state.getMaterial().isToolNotRequired()) {
            return true;
        }

        final ItemStack stack = player.getHeldItemMainhand();
        final String tool = block.getHarvestTool(state);
        if (stack.isEmpty() || tool == null) {
            return player.canHarvestBlock(state);
        }

        final int toolLevel = stack.getItem().getHarvestLevel(stack, tool, player, state);
        if (toolLevel < 0) {
            return player.canHarvestBlock(state);
        }

        return toolLevel >= block.getHarvestLevel(state);
    }
}
