package com.jarhax.sevtechores.api.ores;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class Ore {

	private final ResourceLocation ore;

	private final ItemStack drop;

	private final float hardness;

	private final int harvestLevel;

	private final IBlockState oreState;

	public Ore (ResourceLocation ore, ItemStack drop, float hardness, int harvestLevel, IBlockState oreState) {
		this.ore = ore;
		this.drop = drop;
		this.hardness = hardness;
		this.harvestLevel = harvestLevel;
		this.oreState = oreState;
	}

	public ResourceLocation getOre () {

		return this.ore;
	}

	public ItemStack getDrop () {

		return this.drop;
	}

	public float getHardness () {

		return this.hardness;
	}

	public int getHarvestLevel () {

		return this.harvestLevel;
	}

	public IBlockState getOreState () {

		return this.oreState;
	}
}
