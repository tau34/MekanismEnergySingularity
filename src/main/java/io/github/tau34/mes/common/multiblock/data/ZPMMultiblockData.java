package io.github.tau34.mes.common.multiblock.data;

import io.github.tau34.mes.common.register.MESGases;
import io.github.tau34.mes.common.tile.zpm.TileEntityZPMBlock;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.math.FloatingLong;
import mekanism.common.Mekanism;
import mekanism.common.capabilities.chemical.multiblock.MultiblockChemicalTankBuilder;
import mekanism.common.capabilities.energy.VariableCapacityEnergyContainer;
import mekanism.common.inventory.container.sync.dynamic.ContainerSync;
import mekanism.common.lib.multiblock.MultiblockData;
import mekanism.common.network.to_client.PacketLightningRender;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class ZPMMultiblockData extends MultiblockData {
    public IEnergyContainer energyContainer;
    @ContainerSync
    public IGasTank stabilizerTank;
    private boolean isActive = false;
    @ContainerSync(getter = "getAir")
    private int clientAir = 400;
    private int air = 400;
    @ContainerSync(getter = "getEnergy")
    private final FloatingLong clientEnergy = FloatingLong.ZERO;
    @ContainerSync(getter = "getMaxEnergy")
    private final FloatingLong clientMaxEnergy = FloatingLong.ZERO;
    private AABB deathZone;

    public ZPMMultiblockData(TileEntityZPMBlock tile) {
        super(tile);
        this.energyContainers.add(this.energyContainer = VariableCapacityEnergyContainer.input(FloatingLong.createConst(50_000_000_000L), this));
        this.gasTanks.add(stabilizerTank = MultiblockChemicalTankBuilder.GAS.input(this, () -> 10000L, gas -> gas == MESGases.QUANTUM_STABILIZER.get(), this));
    }

    public void setForceActive(boolean active) {
        isActive = active;
        if (active) {
            air = 0;
        } else {
            air = 400;
        }
    }

    public void setActive(boolean active) {
        if (active) {
            if (air <= 0) isActive = true;
        } else isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getAir() {
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
        boolean np = super.tick(world);
        np |= air != clientAir;
        if (!isActive && air == 0) this.isActive = true;
        if (isActive) {
            energyContainer.insert(FloatingLong.createConst(1_500_000_000L), Action.EXECUTE, AutomationType.INTERNAL);
            stabilizerTank.extract(10L, Action.EXECUTE, AutomationType.INTERNAL);
            Vec3 max = this.getMaxPos().getCenter();
            Vec3 min = this.getMinPos().getCenter();
            double x = (max.x + min.x) / 2D;
            double z = (max.z + min.z) / 2D;
            Vec3 up = new Vec3(x, max.y - 0.5D, z);
            Vec3 down = new Vec3(x, min.y + 0.5D, z);
            Mekanism.packetHandler().sendToAllTracking(new PacketLightningRender(PacketLightningRender.LightningPreset.MAGNETIC_ATTRACTION, Objects.hash(this.renderLocation), up, down, 35), this.getWorld(), this.renderLocation);
            if (stabilizerTank.isEmpty()) {
                world.explode(null, x, (max.y + min.y) / 2, z, 15F, Level.ExplosionInteraction.TNT);
            }
            if (world.getRandom().nextInt() % 20 == 0) {
                for (Entity entity : this.getWorld().getEntitiesOfClass(Entity.class, this.deathZone)) {
                    entity.hurt(entity.damageSources().magic(), 10000.0F);
                }
            }
        }
        return np;
    }

    @Override
    public void onCreated(Level world) {
        super.onCreated(world);
        deathZone = new AABB(this.getMinPos().offset(1, 1, 1),
                this.getMaxPos().offset(-1, -1, -1));
    }

    public FloatingLong getEnergy() {
        return isRemote() ? clientEnergy : this.energyContainer.getEnergy();
    }

    public FloatingLong getMaxEnergy() {
        return isRemote() ? clientMaxEnergy : this.energyContainer.getMaxEnergy();
    }
}
