package io.github.tau34.mes.common.multiblock.data;

import com.mojang.logging.LogUtils;
import io.github.tau34.mes.common.tile.TileEntityZPMBlock;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.math.FloatingLong;
import mekanism.common.capabilities.energy.VariableCapacityEnergyContainer;
import mekanism.common.inventory.container.sync.dynamic.ContainerSync;
import mekanism.common.lib.multiblock.MultiblockData;
import net.minecraft.world.level.Level;

public class ZPMMultiblockData extends MultiblockData {
    public IEnergyContainer energyContainer;
    private boolean isActive = false;
    @ContainerSync(getter = "getAir", setter = "setAir")
    private final int clientAir = 400;
    private int air = 400;
    @ContainerSync(getter = "getEnergy")
    private final FloatingLong clientEnergy = FloatingLong.ZERO;
    @ContainerSync(getter = "getMaxEnergy")
    private final FloatingLong clientMaxEnergy = FloatingLong.ZERO;

    public ZPMMultiblockData(TileEntityZPMBlock tile) {
        super(tile);
        this.energyContainers.add(this.energyContainer = VariableCapacityEnergyContainer.input(FloatingLong.createConst(50_000_000_000L), this));
    }

    public void setActive(boolean active) {
        isActive = active;
        if (active) {
            air = 0;
        }
    }

    public boolean isActive() {
        return isActive;
    }

    public int getAir() {
        LogUtils.getLogger().info(String.valueOf(isRemote()));
        return isRemote() ? clientAir : air;
    }

    public void removeAir() {
        if (air > 0) air--;
    }

    public void setAir(int air) {
        this.air = air;
    }

    @Override
    public boolean tick(Level world) {
        if (!isActive && air == 0) setActive(true);
        if (isActive) {
            energyContainer.insert(FloatingLong.createConst(1_500_000_000L), Action.EXECUTE, AutomationType.INTERNAL);
        }
        return super.tick(world);
    }

    public FloatingLong getEnergy() {
        return isRemote() ? clientEnergy : this.energyContainer.getEnergy();
    }

    public FloatingLong getMaxEnergy() {
        return isRemote() ? clientMaxEnergy : this.energyContainer.getMaxEnergy();
    }
}
