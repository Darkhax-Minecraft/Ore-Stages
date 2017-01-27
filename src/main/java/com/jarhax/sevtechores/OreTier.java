package com.jarhax.sevtechores;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.ResourceLocation;

public class OreTier {
	
	public static final Map<ResourceLocation, OreTier> TIERS = new HashMap<ResourceLocation, OreTier>();
	
	private final ResourceLocation tierId;
	
	public OreTier (ResourceLocation id) {
		
		this.tierId = id;
	}
	
	public ResourceLocation getTierId () {
		
		return this.tierId;
	}
}
