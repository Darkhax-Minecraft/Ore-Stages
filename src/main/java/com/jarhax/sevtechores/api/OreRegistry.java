package com.jarhax.sevtechores.api;

import com.jarhax.sevtechores.api.ores.OreTier;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class OreRegistry {
	public static final Map<String, OreTier> TIERS = new HashMap<String, OreTier>();
	
	public static void registerTier(OreTier tier){
		TIERS.put(tier.getTierId(), tier);
	}
	
	public static OreTier getTier(String location){
		return TIERS.get(location);
	}
}
