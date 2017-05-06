package com.jarhax.oretiers;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.jarhax.oretiers.api.OreWrapper;

import net.darkhax.bookshelf.client.render.block.RenderBlockEvent;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = "oretiers", name = "Ore Tiers", version = "@VERSION@") // ,
																	// dependencies
																	// =
																	// "required-after:bookshelf@[@VERSION_BOOKSHELF@,);required-after:minetweaker3@[@VERSION_MINETWEAKER@,)")
public class STO {

	public static final Multimap<Integer, OreWrapper> REGISTRY = HashMultimap.create();
	public static final Map<IBlockState, OreWrapper> REGISTRY_MAP = new HashMap();

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent ev) {

		MinecraftForge.EVENT_BUS.register(this);
		register(Blocks.GOLD_ORE);
		register(Blocks.IRON_ORE);
		register(Blocks.DIAMOND_ORE);
		register(Blocks.EMERALD_ORE);
		register(Blocks.QUARTZ_ORE, Blocks.NETHERRACK);
	}

	public void register(Block block) {

		register(block.getDefaultState());
	}

	public void register(IBlockState state) {

		register(state, new OreWrapper(state));
	}

	public void register(Block block, Block replace) {

		register(block.getDefaultState(), replace.getDefaultState());
	}

	public void register(IBlockState state, IBlockState replace) {
		
		register(state, new OreWrapper(state, replace));
	}

	public void register(IBlockState state, OreWrapper wrapper) {

		REGISTRY.put(1, wrapper);
		REGISTRY_MAP.put(state, wrapper);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent ev) {

	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent ev) {

	}

	@SubscribeEvent
	public void onBlockRender(RenderBlockEvent event) {

		if (Minecraft.getMinecraft().player.getHeldItemMainhand().getItem() != Items.STICK) {

			OreWrapper wrapper = REGISTRY_MAP.get(event.getState());
			if (wrapper != null) {

				event.setState(wrapper.getReplacementState());
				event.setModel(Minecraft.getMinecraft().getBlockRendererDispatcher()
						.getModelForState(wrapper.getReplacementState()));
			}
		}
	}
}
