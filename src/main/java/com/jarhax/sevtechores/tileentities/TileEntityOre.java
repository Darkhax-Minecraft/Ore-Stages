package com.jarhax.sevtechores.tileentities;

import com.jarhax.sevtechores.api.ores.*;
import net.minecraft.tileentity.TileEntity;

public class TileEntityOre extends TileEntity {
	
	private Ore storedOre;
	
	private OreTier tier;
	
	public Ore getStoredOre() {
		return storedOre;
	}
	
	public void setStoredOre(Ore storedOre) {
		this.storedOre = storedOre;
	}
	
	public OreTier getTier() {
		return tier;
	}
	
	public void setTier(OreTier tier) {
		this.tier = tier;
	}
}
