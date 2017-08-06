package com.jarhax.oretiers;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.Logger;

import com.jarhax.oretiers.api.OreTiersAPI;
import com.jarhax.oretiers.api.StageDataHandler;
import com.jarhax.oretiers.client.renderer.block.model.BakedModelTiered;

import net.darkhax.bookshelf.util.RenderUtils;
import net.darkhax.gamestages.capabilities.PlayerDataHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = "oretiers", name = "Ore Tiers", version = "@VERSION@", dependencies = "required-after:bookshelf@[2.0.0.380,);required-after:crafttweaker@[3.0.25.,)")
public class OreTiers {

    public static Logger log;

    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent ev) {

        log = ev.getModLog();
        MinecraftForge.EVENT_BUS.register(new OreTiersEventHandler());
        PlayerDataHandler.registerDataHandler("oretiers", new StageDataHandler());

        if (Loader.isModLoaded("waila")) {

            FMLInterModComms.sendMessage("waila", "register", "com.jarhax.oretiers.compat.waila.OreTiersProvider.register");
        }
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void postInit (FMLPostInitializationEvent ev) {

        log.info("Loaded {} block replacements!", OreTiersAPI.STATE_MAP.size());
        log.info("Starting model wrapping for replacements.");

        final Map<IBlockState, Tuple<String, IBlockState>> differences = OreTiersAPI.STATE_MAP;

        if (!OreTiersAPI.STATE_MAP.isEmpty()) {

            for (final Entry<IBlockState, Tuple<String, IBlockState>> entry : differences.entrySet()) {

                log.debug("Adding a wrapper model for {}", entry.getKey().toString());
                RenderUtils.setModelForState(entry.getKey(), new BakedModelTiered(entry.getValue().getFirst(), entry.getKey(), entry.getValue().getSecond()));
            }
        }

        else {

            log.info("There are no replacements. Have you added them in a CrT script?");
        }

        log.info("Model wrapping finished!");
    }
}
