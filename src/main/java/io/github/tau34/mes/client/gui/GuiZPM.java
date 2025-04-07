package io.github.tau34.mes.client.gui;

import com.mojang.logging.LogUtils;
import io.github.tau34.mes.client.element.AirGauge;
import io.github.tau34.mes.common.tile.TileEntityZPMBlock;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.FloatingLong;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiTankGauge;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;

public class GuiZPM extends GuiMekanismTile<TileEntityZPMBlock, MekanismTileContainer<TileEntityZPMBlock>> {
    public GuiZPM(MekanismTileContainer<TileEntityZPMBlock> container, Inventory inv, Component title) {
        super(container, inv, title);
        this.dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        this.addRenderableWidget(new GuiEnergyGauge(new GuiEnergyGauge.IEnergyInfoHandler() {
            public FloatingLong getEnergy() {
                return GuiZPM.this.tile.getMultiblock().getEnergy();
            }

            public FloatingLong getMaxEnergy() {
                return GuiZPM.this.tile.getMultiblock().getMaxEnergy();
            }
        }, GaugeType.MEDIUM, this, 7, 16, 34, 56));
        this.addRenderableWidget(new AirGauge(this, 107, 16));
    }

    public int getAir() {
        int air = this.tile.getMultiblock().getAir();
        LogUtils.getLogger().info(String.valueOf(air));
        return air;
    }
}
