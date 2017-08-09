package com.jarhax.oretiers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Utilities {

    public static float getModifiedBreakSpeed (float desiredBreakSpeed, float currentHardness, boolean canHarvest) {

        float speed = desiredBreakSpeed;
        speed *= currentHardness;
        speed *= canHarvest ? 30f : 100f;
        return speed;
    }

    public static boolean getCanHarvestSafely (IBlockState state, EntityPlayer player) {

        final Block block = state.getBlock();

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

    public static float getBlockStrengthSafely (float currentSpeed, IBlockState state, EntityPlayer player, World world, BlockPos pos) {

        final float hardness = state.getBlockHardness(world, pos);

        if (hardness < 0.0F) {

            return 0.0F;
        }

        return currentSpeed / hardness / (!getCanHarvestSafely(state, player) ? 100f : 30f);
    }
}
