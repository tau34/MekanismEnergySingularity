package io.github.tau34.mes.common.tile;

import io.github.tau34.mes.common.multiblock.data.ZPMMultiblockData;
import io.github.tau34.mes.common.register.MESBlocks;
import mekanism.api.IContentsListener;
import mekanism.api.text.EnumColor;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.lib.multiblock.IMultiblockEjector;
import mekanism.common.tile.base.SubstanceType;
import mekanism.common.util.CableUtils;
import mekanism.common.util.EnumUtils;
import mekanism.common.util.WorldUtils;
import mekanism.common.util.text.BooleanStateDisplay;
import mekanism.generators.common.GeneratorsLang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

public class TileEntityVoidResonanceEngine extends TileEntityZPMBlock {
    public TileEntityVoidResonanceEngine(BlockPos pos, BlockState state) {
        super(MESBlocks.VOID_RESONANCE_ENGINE, pos, state);
    }

    @Override
    protected boolean onUpdateServer(ZPMMultiblockData multiblock) {
        for (Direction d : EnumUtils.DIRECTIONS) {
            BlockEntity adj = WorldUtils.getTileEntity(this.getLevel(), this.getBlockPos().relative(d));
            if (adj instanceof TileEntityAirExtractor) {
                multiblock.removeAir();
            }
        }
        return super.onUpdateServer(multiblock);
    }
}
