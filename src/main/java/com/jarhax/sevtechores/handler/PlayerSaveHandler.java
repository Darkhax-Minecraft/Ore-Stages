package com.jarhax.sevtechores.handler;

import java.util.ArrayList;
import java.util.List;

import com.jarhax.sevtechores.OreTier;
import com.jarhax.sevtechores.STO;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerSaveHandler {
	
	@CapabilityInject(ICustomData.class)
	public static final Capability<ICustomData> CUSTOM_DATA = null;
	
	public static void init () {
		
		CapabilityManager.INSTANCE.register(ICustomData.class, new Storage(), Default.class);
		MinecraftForge.EVENT_BUS.register(new PlayerSaveHandler());
	}
	
	@SubscribeEvent
	public void attachCapabilities (AttachCapabilitiesEvent<Entity> event) {
		
		if (event.getObject() instanceof EntityPlayer)
			event.addCapability(new ResourceLocation(STO.MODID, "PlayerData"), new Provider());
	}
	
	@SubscribeEvent
	public void onPlayerClonning (PlayerEvent.Clone event) {
		
		final ICustomData newData = getCustomData(event.getEntityPlayer());
		
		for (final OreTier tier : getUnlockedTiers(event.getOriginal()))
			newData.unlockTier(tier);
	}
	
	public static boolean unlockTier (EntityPlayer player, OreTier tier) {
		
		final ICustomData data = getCustomData(player);
		return data != null ? data.unlockTier(tier) : false;
	}
	
	public static List<OreTier> getUnlockedTiers (EntityPlayer player) {
		
		final ICustomData data = getCustomData(player);
		return data != null ? data.getUnlockedTiers() : new ArrayList<OreTier>();
	}
	
	public static void forgetTiers (EntityPlayer player) {
		
		final ICustomData data = getCustomData(player);
		
		if (data != null)
			data.forgetTiers();
	}
	
	public static ICustomData getCustomData (EntityPlayer player) {
		
		return player.hasCapability(CUSTOM_DATA, null) ? player.getCapability(CUSTOM_DATA, null) : null;
	}
	
	public static interface ICustomData {
		
		List<OreTier> getUnlockedTiers ();
		
		boolean unlockTier (OreTier tier);
		
		void forgetTiers ();
	}
	
	public static class Default implements ICustomData {
		
		private List<OreTier> unlockedTiers;
		
		public List<OreTier> getUnlockedTiers () {
			
			return unlockedTiers;
		}
		
		public boolean unlockTier (OreTier tier) {
			
			if (tier == null)
				return false;
			
			return unlockedTiers.add(tier);
		}
		
		public void forgetTiers () {
			
			unlockedTiers.clear();
		}
	}
	
	public static class Storage implements Capability.IStorage<ICustomData> {
		
		@Override
		public NBTBase writeNBT (Capability<ICustomData> capability, ICustomData instance, EnumFacing side) {
			
			final NBTTagCompound tag = new NBTTagCompound();
			final NBTTagList tiers = new NBTTagList();
			
			for (OreTier tier : instance.getUnlockedTiers())
				tiers.appendTag(new NBTTagString(tier.getTierId().toString()));
			
			tag.setTag("Tiers", tiers);
			return tag;
		}
		
		@Override
		public void readNBT (Capability<ICustomData> capability, ICustomData instance, EnumFacing side, NBTBase nbt) {
			
			final NBTTagCompound tag = (NBTTagCompound) nbt;
			final NBTTagList list = tag.getTagList("Tiers", 8);
			
			for (int index = 0; index < list.tagCount(); index++)
				instance.unlockTier(OreTier.TIERS.get(new ResourceLocation(list.getStringTagAt(index))));
		}
	}
	
	public static class Provider implements ICapabilitySerializable<NBTTagCompound> {
		
		ICustomData instance = CUSTOM_DATA.getDefaultInstance();
		
		@Override
		public boolean hasCapability (Capability<?> capability, EnumFacing facing) {
			
			return capability == CUSTOM_DATA;
		}
		
		@Override
		public <T> T getCapability (Capability<T> capability, EnumFacing facing) {
			
			return this.hasCapability(capability, facing) ? CUSTOM_DATA.<T> cast(this.instance) : null;
		}
		
		@Override
		public NBTTagCompound serializeNBT () {
			
			return (NBTTagCompound) CUSTOM_DATA.getStorage().writeNBT(CUSTOM_DATA, this.instance, null);
		}
		
		@Override
		public void deserializeNBT (NBTTagCompound nbt) {
			
			CUSTOM_DATA.getStorage().readNBT(CUSTOM_DATA, this.instance, null, nbt);
		}
	}
}