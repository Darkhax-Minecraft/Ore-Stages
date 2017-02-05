package com.jarhax.sevtechores.asm;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.SortingIndex(1001)
@IFMLLoadingPlugin.TransformerExclusions("com.jarhax.sevtechores.asm")
@IFMLLoadingPlugin.MCVersion("1.10.2")
public class LoadingPluginSevTechOres implements IFMLLoadingPlugin {

	public static boolean loaded = false;

	public LoadingPluginSevTechOres () {

	}

	@Override
	public String[] getASMTransformerClass () {

		return new String[] { ClassTransformerSevTechOres.class.getName() };
	}

	@Override
	public String getModContainerClass () {

		return null;
	}

	@Override
	public String getSetupClass () {

		return null;
	}

	@Override
	public void injectData (Map<String, Object> data) {

		ASMUtils.isSrg = (Boolean) data.get("runtimeDeobfuscationEnabled");
	}

	@Override
	public String getAccessTransformerClass () {

		return null;
	}
}