package io.github.tau34.mes.common.multiblock.validator;

import io.github.tau34.mes.common.multiblock.data.ZPMMultiblockData;
import io.github.tau34.mes.common.register.MESBlockTypes;
import io.github.tau34.mes.common.register.MESBlocks;
import mekanism.common.content.blocktype.BlockType;
import mekanism.common.lib.math.voxel.VoxelCuboid;
import mekanism.common.lib.multiblock.CuboidStructureValidator;
import mekanism.common.lib.multiblock.FormationProtocol;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ZPMValidator extends CuboidStructureValidator<ZPMMultiblockData> {
    private static final VoxelCuboid BOUNDS = new VoxelCuboid(5, 10, 5);

    public ZPMValidator() {
        super(BOUNDS, BOUNDS);
    }

    @Override
    protected FormationProtocol.CasingType getCasingType(BlockState state) {
        Block block = state.getBlock();
        return BlockType.is(block, MESBlockTypes.ZPM_FRAME) ? FormationProtocol.CasingType.FRAME :
                BlockType.is(block, MESBlockTypes.ZPM_PORT) ? FormationProtocol.CasingType.VALVE : FormationProtocol.CasingType.INVALID;
    }
}
