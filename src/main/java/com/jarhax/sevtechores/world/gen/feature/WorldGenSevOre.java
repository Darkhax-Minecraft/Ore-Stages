package com.jarhax.sevtechores.world.gen.feature;

import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;

public class WorldGenSevOre extends WorldGenerator {

	public final IBlockState oreBlock;

	private final int numberOfBlocks;

	private final Predicate<IBlockState> predicate;

	public WorldGenSevOre (WorldGenMinable gen) {

		this(gen.oreBlock, gen.numberOfBlocks, gen.predicate);
	}

	public WorldGenSevOre (IBlockState state, int blockCount, Predicate<IBlockState> predicate) {

		this.oreBlock = state;
		this.numberOfBlocks = blockCount;
		this.predicate = predicate;
	}

	@Override
	public boolean generate (World worldIn, Random rand, BlockPos position) {

		final float offset = rand.nextFloat() * (float) Math.PI;
		final double maxOffsetX = position.getX() + 8 + MathHelper.sin(offset) * this.numberOfBlocks / 8.0F;
		final double minOffsetX = position.getX() + 8 - MathHelper.sin(offset) * this.numberOfBlocks / 8.0F;
		final double maxOffsetZ = position.getZ() + 8 + MathHelper.cos(offset) * this.numberOfBlocks / 8.0F;
		final double minOffsetZ = position.getZ() + 8 - MathHelper.cos(offset) * this.numberOfBlocks / 8.0F;
		final double maxOffsetY = position.getY() + rand.nextInt(3) - 2;
		final double minOffsetY = position.getY() + rand.nextInt(3) - 2;

		for (int attemptNum = 0; attemptNum < this.numberOfBlocks; attemptNum++) {

			final float attemptOffset = (float) attemptNum / (float) this.numberOfBlocks;
			final double offsetX = maxOffsetX + (minOffsetX - maxOffsetX) * attemptOffset;
			final double offsetY = maxOffsetY + (minOffsetY - maxOffsetY) * attemptOffset;
			final double offsetZ = maxOffsetZ + (minOffsetZ - maxOffsetZ) * attemptOffset;
			final double anotherOffset = rand.nextDouble() * this.numberOfBlocks / 16.0D;
			final double horizontalOffset = (MathHelper.sin((float) Math.PI * attemptOffset) + 1.0F) * anotherOffset + 1.0D;
			final double verticalOffset = (MathHelper.sin((float) Math.PI * attemptOffset) + 1.0F) * anotherOffset + 1.0D;
			final int startX = MathHelper.floor_double(offsetX - horizontalOffset / 2.0D);
			final int startY = MathHelper.floor_double(offsetY - verticalOffset / 2.0D);
			final int startZ = MathHelper.floor_double(offsetZ - horizontalOffset / 2.0D);
			final int maxX = MathHelper.floor_double(offsetX + horizontalOffset / 2.0D);
			final int maxY = MathHelper.floor_double(offsetY + verticalOffset / 2.0D);
			final int maxZ = MathHelper.floor_double(offsetZ + horizontalOffset / 2.0D);

			for (int posX = startX; posX <= maxX; posX++) {

				final double xBounds = (posX + 0.5D - offsetX) / (horizontalOffset / 2.0D);

				if (xBounds * xBounds < 1.0D) {

					for (int posY = startY; posY <= maxY; posY++) {

						final double yBounds = (posY + 0.5D - offsetY) / (verticalOffset / 2.0D);

						if (xBounds * xBounds + yBounds * yBounds < 1.0D) {

							for (int posZ = startZ; posZ <= maxZ; posZ++) {

								final double zBounds = (posZ + 0.5D - offsetZ) / (horizontalOffset / 2.0D);

								if (xBounds * xBounds + yBounds * yBounds + zBounds * zBounds < 1.0D) {

									final BlockPos blockpos = new BlockPos(posX, posY, posZ);
									final IBlockState state = worldIn.getBlockState(blockpos);

									// TODO This is never true?
									if (state.getBlock().isReplaceableOreGen(state, worldIn, blockpos, this.predicate)) {
										worldIn.setBlockState(blockpos, Blocks.OBSIDIAN.getDefaultState(), 2);
										System.out.println("Genned at " + blockpos.toString());
									}
								}
							}
						}
					}
				}
			}
		}

		return true;
	}
}