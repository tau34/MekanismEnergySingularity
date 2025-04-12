package io.github.tau34.mes.common.multiblock;

import io.github.tau34.mes.common.register.MESBlocks;
import mekanism.common.command.builders.StructureBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class MESBuilders {
    public static class ZPMBuilder extends StructureBuilder {
        public ZPMBuilder() {
            super(5, 10, 5);
        }

        @Override
        protected void build(Level world, BlockPos start, boolean empty) {
            this.buildFrame(world, start);
            this.buildWalls(world, start);
            this.buildInteriorLayers(world, start, 1, 8, Blocks.AIR);
        }

        @Override
        protected Block getCasing() {
            return MESBlocks.ZPM_FRAME.getBlock();
        }
    }
}
