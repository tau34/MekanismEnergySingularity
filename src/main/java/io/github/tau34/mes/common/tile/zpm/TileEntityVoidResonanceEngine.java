package io.github.tau34.mes.common.tile.zpm;

import io.github.tau34.mes.common.multiblock.data.ZPMMultiblockData;
import io.github.tau34.mes.common.register.MESBlocks;
import mekanism.common.util.EnumUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TileEntityVoidResonanceEngine extends TileEntityZPMBlock {
    public TileEntityVoidResonanceEngine(BlockPos pos, BlockState state) {
        super(MESBlocks.VOID_RESONANCE_ENGINE, pos, state);
    }

    @Override
    protected boolean onUpdateServer(ZPMMultiblockData multiblock) {
        for (Direction d : EnumUtils.DIRECTIONS) {
            BlockEntity adj = WorldUtils.getTileEntity(this.getLevel(), this.getBlockPos().relative(d));
            if (adj instanceof TileEntityAirExtractor extractor) {
                if (extractor.getActive()) {
                    multiblock.removeAir();
                }
            }
        }
        return super.onUpdateServer(multiblock);
    }
}
