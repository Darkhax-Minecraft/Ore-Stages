package net.darkhax.orestages.compat.theoneprobe;

import com.google.common.base.Function;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.apiimpl.providers.DefaultProbeInfoProvider;
import mcjty.theoneprobe.config.Config;
import net.darkhax.bookshelf.util.PlayerUtils;
import net.darkhax.gamestages.GameStageHelper;
import net.darkhax.orestages.api.OreTiersAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class TOPCompatibility implements Function<ITheOneProbe, Void> {

    private static class ProbeHitDataWrapper implements IProbeHitData {
        private final IProbeHitData data;
        private final ItemStack pickBlock;

        public ProbeHitDataWrapper(IProbeHitData data, IBlockState state, World world, EntityPlayer player) {
            this.data = data;
            pickBlock = state.getBlock().getPickBlock(state,
                    new RayTraceResult(data.getHitVec(), data.getSideHit(), data.getPos()),
                    world,
                    data.getPos(),
                    player
            );
        }

        @Override
        public BlockPos getPos() {
            return data.getPos();
        }

        @Override
        public Vec3d getHitVec() {
            return data.getHitVec();
        }

        @Override
        public EnumFacing getSideHit() {
            return data.getSideHit();
        }

        @Override
        public ItemStack getPickBlock() {
            return pickBlock;
        }
    }

    public static void load() {
        FMLInterModComms.sendFunctionMessage("theoneprobe", "getTheOneProbe", TOPCompatibility.class.getName());
    }

    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        theOneProbe.registerBlockDisplayOverride((mode, probeInfo, player, world, blockState, data) -> {
            Tuple<String, IBlockState> stageInfo = OreTiersAPI.getStageInfo(blockState);
            if (stageInfo != null && !GameStageHelper.clientHasStage(PlayerUtils.getClientPlayer(), stageInfo.getFirst())) {
                IBlockState stageBlockState = stageInfo.getSecond();
                DefaultProbeInfoProvider.showStandardBlockInfo(
                        Config.getRealConfig(),
                        mode,
                        probeInfo,
                        stageBlockState,
                        stageBlockState.getBlock(),
                        world,
                        data.getPos(),
                        player,
                        new ProbeHitDataWrapper(data, stageBlockState, world, player)
                );
                return true;
            }
            return false;
        });
        return null;
    }
}
