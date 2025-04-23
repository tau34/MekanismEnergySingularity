package io.github.tau34.mes.common.capability;

import io.github.tau34.mes.common.tile.TileEntityUniverseGenerator;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.math.FloatingLong;
import mekanism.common.block.attribute.AttributeEnergy;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.util.NBTUtils;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class UniverseGeneratorEnergyContainer extends MachineEnergyContainer<TileEntityUniverseGenerator> {
    public static UniverseGeneratorEnergyContainer input(TileEntityUniverseGenerator tile, @Nullable IContentsListener listener) {
        AttributeEnergy electricBlock = validateBlock(tile);
        return new UniverseGeneratorEnergyContainer(electricBlock.getStorage(), electricBlock.getUsage(), notExternal, alwaysTrue, tile, listener);
    }

    private UniverseGeneratorEnergyContainer(FloatingLong maxEnergy, FloatingLong energyPerTick, Predicate<@NotNull AutomationType> canExtract, Predicate<@NotNull AutomationType> canInsert, TileEntityUniverseGenerator tile, @Nullable IContentsListener listener) {
        super(maxEnergy, energyPerTick, canExtract, canInsert, tile, listener);
    }

    public boolean adjustableRates() {
        return true;
    }

    public void updateMaxEnergy(FloatingLong maxEnergy) {
        this.setMaxEnergy(maxEnergy);
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = super.serializeNBT();
        nbt.putString("maxEnergy", this.getMaxEnergy().toString());
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        NBTUtils.setFloatingLongIfPresent(nbt, "maxEnergy", this::updateMaxEnergy);
    }
}