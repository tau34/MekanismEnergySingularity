package io.github.tau34.mes.common.tile;

import io.github.tau34.mes.MESMod;
import io.github.tau34.mes.common.multiblock.data.ZPMMultiblockData;
import io.github.tau34.mes.common.register.MESBlocks;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.tile.prefab.TileEntityMultiblock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityZPMBlock extends TileEntityMultiblock<ZPMMultiblockData> {
    public TileEntityZPMBlock(BlockPos pos, BlockState state) {
        super(MESBlocks.ZPM_FRAME, pos, state);
    }

    public TileEntityZPMBlock(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    public ZPMMultiblockData createMultiblock() {
        return new ZPMMultiblockData(this);
    }

    @Override
    public MultiblockManager<ZPMMultiblockData> getManager() {
        return MESMod.zpmManager;
    }

    public void setZPMActive(boolean b) {
        if (this.getMultiblock().isFormed()) {
            this.getMultiblock().setActive(b);
        }
    }
}
