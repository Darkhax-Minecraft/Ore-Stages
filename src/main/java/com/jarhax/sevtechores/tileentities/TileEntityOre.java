package com.jarhax.sevtechores.tileentities;

import com.jarhax.sevtechores.api.ores.Ore;
import com.jarhax.sevtechores.api.ores.OreTier;

import net.minecraft.tileentity.TileEntity;

public class TileEntityOre extends TileEntity {

	private Ore storedOre;

	private OreTier tier;

	public Ore getStoredOre () {

		return this.storedOre;
	}

	public void setStoredOre (Ore storedOre) {

		this.storedOre = storedOre;
	}

	public OreTier getTier () {

		return this.tier;
	}

	public void setTier (OreTier tier) {

		this.tier = tier;
	}
}
