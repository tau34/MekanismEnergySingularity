package io.github.tau34.mes.common.tile;

import io.github.tau34.mes.common.register.MESBlocks;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityAirExtractor extends TileEntityMekanism {
    public TileEntityAirExtractor(BlockPos pos, BlockState state) {
        super(MESBlocks.AIR_EXTRACTOR, pos, state);
    }
}
