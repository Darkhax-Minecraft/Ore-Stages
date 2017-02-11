package com.jarhax.sevtechores.api.ores;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class OreTier {

	private final String tierId;

	private final List<ItemStack> ores;

	public OreTier (String id) {

		this.tierId = id;
		this.ores = new ArrayList<>();
	}

	public OreTier (String tierId, List<ItemStack> ores) {
		this.tierId = tierId;
		this.ores = ores;
	}

	public String getTierId () {

		return this.tierId;
	}
	
	public List<ItemStack> getOres() {
		return ores;
	}
}
