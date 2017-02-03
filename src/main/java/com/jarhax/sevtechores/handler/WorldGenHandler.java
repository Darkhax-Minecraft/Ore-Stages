package com.jarhax.sevtechores.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jarhax.sevtechores.world.gen.feature.WorldGenSevOre;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldGenHandler {

	/**
	 * A list of all the known minable block states. Every time an ore generates it will be
	 * added to this list. This list is used purely for information gathering and has no
	 * functional value.
	 */
	public static final List<IBlockState> KNOWN_STATES = new ArrayList<IBlockState>();

	/**
	 * A list of all the known minable block states, which do not have an ore tier specified.
	 * Every time an unhandled minable ore is generated it will be added here. This list is
	 * used purely for information gathering and has no functional value.
	 */
	public static final List<IBlockState> UNKNOWN_STATES = new ArrayList<IBlockState>();

	/**
	 * A map which caches all of the custom generator objects. This cache should only be
	 * modified through {@link #getGenerator(IBlockState, WorldGenMinable)}. Used to keep
	 * object creation to a minimum during the ore generation process.
	 */
	public static final Map<IBlockState, WorldGenSevOre> GENERATORS = new HashMap<IBlockState, WorldGenSevOre>();

	@SubscribeEvent
	public void onGenerateMinable (OreGenEvent.GenerateMinable event) {

		final WorldGenerator gen = event.getGenerator();

		if (gen instanceof WorldGenMinable) {

			final IBlockState state = ((WorldGenMinable) gen).oreBlock;

			if (!KNOWN_STATES.contains(state)) {
				KNOWN_STATES.add(state);
			}

			// TOOD check if there is a tier handling the state.
			if (true) {

				getGenerator(state, (WorldGenMinable) gen).generate(event.getWorld(), event.getRand(), event.getPos());
				event.setResult(Result.DENY);
			}

			else if (UNKNOWN_STATES.contains(state)) {
				UNKNOWN_STATES.add(state);
			}

			System.out.println(String.format("Attempted to spawn %s at %s", state.getBlock().getLocalizedName(), event.getPos().toString()));
		}
	}

	/**
	 * Gets the modified sevtech generator for a minable block. If one does not exist, it will
	 * be created.
	 *
	 * @param state The state to make a generator for.
	 * @param gen The generator to copy.
	 * @return The sevtech generator instance.
	 */
	private static WorldGenSevOre getGenerator (IBlockState state, WorldGenMinable gen) {

		if (GENERATORS.containsKey(state))
			return GENERATORS.get(state);

		final WorldGenSevOre sevGen = new WorldGenSevOre(gen);
		GENERATORS.put(state, sevGen);

		return sevGen;
	}
}
