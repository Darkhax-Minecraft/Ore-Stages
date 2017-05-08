package com.jarhax.oretiers.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerDataHandler {

    @CapabilityInject(IOreData.class)
    public static final Capability<IOreData> CAPABILITY = null;

    public static void initialize () {

        CapabilityManager.INSTANCE.register(IOreData.class, new Storage(), DefaultOreData.class);
        MinecraftForge.EVENT_BUS.register(new PlayerDataHandler());
    }

    public static IOreData getHandler (EntityPlayer entity) {

        return entity.hasCapability(CAPABILITY, EnumFacing.DOWN) ? entity.getCapability(CAPABILITY, EnumFacing.DOWN) : null;
    }

    public static void read (IOreData data, NBTTagCompound tag) {

        final NBTTagList tagList = tag.getTagList("UnlockedStages", NBT.TAG_STRING);

        for (int index = 0; index < tagList.tagCount(); index++) {
            data.unlockStage(tagList.getStringTagAt(index));
        }
    }

    public static void write (IOreData data, NBTTagCompound tag) {

        final NBTTagList tagList = new NBTTagList();

        for (final String string : data.getUnlockedStages()) {
            tagList.appendTag(new NBTTagString(string));
        }

        tag.setTag("UnlockedStages", tagList);
    }

    public static void clone (IOreData original, IOreData clone) {

        for (final String stage : original.getUnlockedStages()) {
            clone.unlockStage(stage);
        }
    }

    @SubscribeEvent
    public void attachCapabilities (AttachCapabilitiesEvent<Entity> event) {

        if (event.getObject() instanceof EntityPlayer) {
            event.addCapability(new ResourceLocation("oretiers", "playerdata"), new Provider());
        }
    }

    @SubscribeEvent
    public void clonePlayer (PlayerEvent.Clone event) {

        final IOreData original = getHandler(event.getOriginal());
        final IOreData clone = getHandler((EntityPlayer) event.getEntity());
        clone(original, clone);
    }

    public interface IOreData {

        Collection<String> getUnlockedStages ();

        boolean hasUnlockedStage (String stage);

        void unlockStage (String stage);

        void lockStage (String stage);
    }

    public static class DefaultOreData implements IOreData {

        private final List<String> unlockedStages = new ArrayList<>();

        @Override
        public Collection<String> getUnlockedStages () {

            return this.unlockedStages;
        }

        @Override
        public boolean hasUnlockedStage (String stage) {

            return this.unlockedStages.contains(stage);
        }

        @Override
        public void unlockStage (String stage) {

            if (!this.unlockedStages.contains(stage)) {
                this.unlockedStages.add(stage);
            }
        }

        @Override
        public void lockStage (String stage) {

            this.unlockedStages.remove(stage);
        }
    }

    public static class Storage implements Capability.IStorage<IOreData> {

        @Override
        public NBTBase writeNBT (Capability<IOreData> capability, IOreData instance, EnumFacing side) {

            final NBTTagCompound tag = new NBTTagCompound();
            PlayerDataHandler.write(instance, tag);
            return tag;
        }

        @Override
        public void readNBT (Capability<IOreData> capability, IOreData instance, EnumFacing side, NBTBase nbt) {

            final NBTTagCompound tag = (NBTTagCompound) nbt;
            PlayerDataHandler.read(instance, tag);
        }
    }

    public static class Provider implements ICapabilitySerializable<NBTTagCompound> {

        IOreData instance = CAPABILITY.getDefaultInstance();

        @Override
        public boolean hasCapability (Capability<?> capability, EnumFacing facing) {

            return capability == CAPABILITY;
        }

        @Override
        public <T> T getCapability (Capability<T> capability, EnumFacing facing) {

            return this.hasCapability(capability, facing) ? CAPABILITY.<T> cast(this.instance) : null;
        }

        @Override
        public NBTTagCompound serializeNBT () {

            return (NBTTagCompound) CAPABILITY.getStorage().writeNBT(CAPABILITY, this.instance, null);
        }

        @Override
        public void deserializeNBT (NBTTagCompound nbt) {

            CAPABILITY.getStorage().readNBT(CAPABILITY, this.instance, null, nbt);
        }
    }
}