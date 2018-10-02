package com.jarhax.sevtechores.api;

import java.util.HashMap;
import java.util.Map;

import com.jarhax.sevtechores.api.ores.OreTier;

public class OreRegistry {

	public static final Map<String, OreTier> TIERS = new HashMap<>();

	public static void registerTier (OreTier tier) {

		TIERS.put(tier.getTierId(), tier);
	}
	
	public static OreTier removeTier(String name){
		return TIERS.remove(name);
	}

	public static OreTier getTier (String location) {

		return TIERS.get(location);
	}
}