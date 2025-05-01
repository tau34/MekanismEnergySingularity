package io.github.tau34.mes.common.multiblock;

import com.mojang.logging.LogUtils;
import io.github.tau34.mes.common.multiblock.validator.AdvancedFusionValidator;
import io.github.tau34.mes.common.register.MESBlockTypes;
import io.github.tau34.mes.common.register.MESBlocks;
import mekanism.common.command.builders.StructureBuilder;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.lib.multiblock.FormationProtocol;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

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

    public static class AdvancedFusionBuilder extends StructureBuilder {
        public AdvancedFusionBuilder() {
            super(20, 5, 20);
        }

        @Override
        protected void build(Level world, BlockPos start, boolean empty) {
            int ySize = AdvancedFusionValidator.STRUCTURE.length;
            int xSize = AdvancedFusionValidator.STRUCTURE[0].length;
            int zSize = AdvancedFusionValidator.STRUCTURE[0][0].length;

            for (int x = 0; x < xSize; x++) {
                for (int z = 0; z < zSize; z++) {
                    for (int y = 0; y < ySize; y++) {
                        byte type = AdvancedFusionValidator.STRUCTURE[y][x][z];
                        if (type == 2) {
                            world.setBlockAndUpdate(start.offset(x, y, z), MESBlocks.ADVANCED_FUSION_FRAME.getBlock().defaultBlockState());
                        } else if (type == 1) {
                            world.setBlockAndUpdate(start.offset(x, y, z), Blocks.AIR.defaultBlockState());
                        }
                    }
                }
            }
        }

        @Override
        protected Block getCasing() {
            return MESBlocks.ADVANCED_FUSION_FRAME.getBlock();
        }
    }
}
