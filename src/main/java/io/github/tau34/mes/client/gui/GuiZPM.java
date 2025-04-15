package io.github.tau34.mes.client.gui;

import io.github.tau34.mes.client.element.AirGauge;
import io.github.tau34.mes.common.network.MESPacketGuiInteract;
import io.github.tau34.mes.common.tile.zpm.TileEntityZPMBlock;
import mekanism.api.math.FloatingLong;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.button.TranslationButton;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.generators.common.GeneratorsLang;
import mekanism.generators.common.MekanismGenerators;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

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
        this.addRenderableWidget(new GuiGasGauge(() -> this.tile.getMultiblock().stabilizerTank,
                () -> this.tile.getMultiblock().getGasTanks(null), GaugeType.STANDARD, this, 45, 16));
        this.addRenderableWidget(new TranslationButton(this, 76, 20, 50, 16, GeneratorsLang.FISSION_SCRAM, () -> MekanismGenerators.packetHandler().sendToServer(new MESPacketGuiInteract(MESPacketGuiInteract.MESGuiInteraction.ZPM_ACTIVATE, this.tile, 0D))));
        this.addRenderableWidget(new AirGauge(this, 133, 16));
    }

    public int getAir() {
        return this.tile.getMultiblock().getAir();
    }
}
