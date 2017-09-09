package net.darkhax.orestages;

import java.util.Map;
import java.util.Map.Entry;

import net.darkhax.bookshelf.lib.LoggingHelper;
import net.darkhax.bookshelf.util.RenderUtils;
import net.darkhax.orestages.api.OreTiersAPI;
import net.darkhax.orestages.client.renderer.block.model.BakedModelTiered;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.Tuple;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = "orestages", name = "Ore Stages", version = "@VERSION@", dependencies = "required-after:bookshelf@[2.1.431,);required-after:gamestages@[1.0.52,);required-after:crafttweaker@[3.0.25.,)", certificateFingerprint = "@FINGERPRINT@")
public class OreStages {

    public static final LoggingHelper LOG = new LoggingHelper("Ore Stages");
    
    @Mod.EventHandler
    public void preInit (FMLPreInitializationEvent ev) {

        MinecraftForge.EVENT_BUS.register(new OreTiersEventHandler());

        if (Loader.isModLoaded("waila")) {

            FMLInterModComms.sendMessage("waila", "register", "com.jarhax.oretiers.compat.waila.OreTiersProvider.register");
        }
    }

    @Mod.EventHandler
    @SideOnly(Side.CLIENT)
    public void postInit (FMLPostInitializationEvent ev) {

        LOG.info("Loaded {} block replacements!", OreTiersAPI.STATE_MAP.size());
        LOG.info("Starting model wrapping for replacements.");

        final Map<IBlockState, Tuple<String, IBlockState>> differences = OreTiersAPI.STATE_MAP;

        if (!OreTiersAPI.STATE_MAP.isEmpty()) {

            for (final Entry<IBlockState, Tuple<String, IBlockState>> entry : differences.entrySet()) {

                LOG.debug("Adding a wrapper model for {}", entry.getKey().toString());
                RenderUtils.setModelForState(entry.getKey(), new BakedModelTiered(entry.getValue().getFirst(), entry.getKey(), entry.getValue().getSecond()));
            }
        }

        else {

            LOG.info("There are no replacements. Have you added them in a CrT script?");
        }

        LOG.info("Model wrapping finished!");
    }
    
    
    @EventHandler
    public void onFingerprintViolation(FMLFingerprintViolationEvent event) {
        
        LOG.warn("Invalid fingerprint detected! The file " + event.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
    }
}