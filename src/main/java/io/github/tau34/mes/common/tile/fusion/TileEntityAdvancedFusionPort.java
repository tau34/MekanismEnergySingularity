package io.github.tau34.mes.common.tile.fusion;

import com.mojang.logging.LogUtils;
import io.github.tau34.mes.MESLang;
import io.github.tau34.mes.common.attribute.AttributeStateAdvancedFusionPortMode;
import io.github.tau34.mes.common.multiblock.data.AdvancedFusionMultiblockData;
import io.github.tau34.mes.common.register.MESBlocks;
import mekanism.api.IContentsListener;
import mekanism.api.IIncrementalEnum;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.MathUtils;
import mekanism.api.text.EnumColor;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.lib.multiblock.IMultiblockEjector;
import mekanism.common.tile.base.SubstanceType;
import mekanism.common.util.CableUtils;
import mekanism.common.util.ChemicalUtil;
import mekanism.common.util.text.BooleanStateDisplay;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

public class TileEntityAdvancedFusionPort extends TileEntityAdvancedFusionBlock implements IMultiblockEjector {
    private Set<Direction> outputDirections = Collections.emptySet();

    public TileEntityAdvancedFusionPort(BlockPos pos, BlockState state) {
        super(MESBlocks.ADVANCED_FUSION_PORT, pos, state);
        this.delaySupplier = NO_DELAY;
    }

    @Override
    protected @Nullable IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        return side -> this.getMultiblock().getEnergyContainers(side);
    }

    @Override
    public boolean insertGasCheck(int tank, @Nullable Direction side) {
        return this.getMode() != AttributeStateAdvancedFusionPortMode.AdvancedFusionPortMode.OUTPUT && super.insertGasCheck(tank, side);
    }

    @Override
    public @Nullable IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        return  side -> switch (this.getMode()) {
            case INPUT_LEFT -> this.getMultiblock().leftTanks;
            case INPUT_RIGHT -> this.getMultiblock().rightTanks;
            case OUTPUT -> this.getMultiblock().outputTanks;
        };
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
    protected boolean onUpdateServer(AdvancedFusionMultiblockData multiblock) {
        if (this.getMode() == AttributeStateAdvancedFusionPortMode.AdvancedFusionPortMode.OUTPUT && multiblock.isFormed()) {
            ChemicalUtil.emit(this.outputDirections, multiblock.outputTank, this);
        }
        return super.onUpdateServer(multiblock);
    }

    @ComputerMethod
    AttributeStateAdvancedFusionPortMode.AdvancedFusionPortMode getMode() {
        return this.getBlockState().getValue(AttributeStateAdvancedFusionPortMode.modeProperty);
    }

    @ComputerMethod
    void setMode(AttributeStateAdvancedFusionPortMode.AdvancedFusionPortMode mode) {
        if (mode != this.getMode()) {
            this.level.setBlockAndUpdate(this.worldPosition, this.getBlockState().setValue(AttributeStateAdvancedFusionPortMode.modeProperty, mode));
        }
    }

    @Override
    public InteractionResult onSneakRightClick(Player player) {
        if (!this.isRemote()) {
            AttributeStateAdvancedFusionPortMode.AdvancedFusionPortMode mode = this.getMode().getNext();
            this.setMode(mode);
            player.displayClientMessage(MESLang.ZPM_PORT_EJECT.translateColored(EnumColor.GRAY, mode), true);
        }

        return InteractionResult.SUCCESS;
    }
}
