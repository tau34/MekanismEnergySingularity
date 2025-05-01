package io.github.tau34.mes.common.tile.fusion;

import io.github.tau34.mes.MESMod;
import io.github.tau34.mes.common.multiblock.data.AdvancedFusionMultiblockData;
import io.github.tau34.mes.common.register.MESBlocks;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityAdvancedFusionBlock extends TileEntityMultiblock<AdvancedFusionMultiblockData> {
    public TileEntityAdvancedFusionBlock(BlockPos pos, BlockState state) {
        super(MESBlocks.ADVANCED_FUSION_FRAME, pos, state);
    }

    public TileEntityAdvancedFusionBlock(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    public AdvancedFusionMultiblockData createMultiblock() {
        return new AdvancedFusionMultiblockData(this);
    }

    @Override
    public MultiblockManager<AdvancedFusionMultiblockData> getManager() {
        return MESMod.advancedFusionManager;
    }
}
