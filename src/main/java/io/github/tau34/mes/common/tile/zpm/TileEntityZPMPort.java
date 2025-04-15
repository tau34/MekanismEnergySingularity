package io.github.tau34.mes.common.tile.zpm;

import io.github.tau34.mes.common.multiblock.data.ZPMMultiblockData;
import io.github.tau34.mes.common.register.MESBlocks;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.text.EnumColor;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.lib.multiblock.IMultiblockEjector;
import mekanism.common.tile.base.SubstanceType;
import mekanism.common.util.CableUtils;
import mekanism.common.util.text.BooleanStateDisplay;
import mekanism.generators.common.GeneratorsLang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

public class TileEntityZPMPort extends TileEntityZPMBlock implements IMultiblockEjector {
    private Set<Direction> outputDirections = Collections.emptySet();

    public TileEntityZPMPort(BlockPos pos, BlockState state) {
        super(MESBlocks.ZPM_PORT, pos, state);
    }

    @Override
    protected @Nullable IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        return side -> this.getMultiblock().getEnergyContainers(side);
    }

    @Override
    public @Nullable IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        return side -> this.getMultiblock().getGasTanks(side);
    }

    @Override
    public void setEjectSides(Set<Direction> sides) {
        outputDirections = sides;
    }

    @Override
    public boolean persists(SubstanceType type) {
        return type != SubstanceType.ENERGY && type != SubstanceType.GAS && super.persists(type);
    }

    @Override
    public InteractionResult onSneakRightClick(Player player) {
        if (!this.isRemote()) {
            boolean oldMode = this.getActive();
            this.setActive(!oldMode);
            player.displayClientMessage(GeneratorsLang.REACTOR_PORT_EJECT.translateColored(EnumColor.GRAY, BooleanStateDisplay.InputOutput.of(oldMode, true)), true);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    protected boolean onUpdateServer(ZPMMultiblockData multiblock) {
        if (this.getActive() && multiblock.isFormed()) {
            CableUtils.emit(this.outputDirections, multiblock.energyContainer, this);
        }
        return super.onUpdateServer(multiblock);
    }
}
