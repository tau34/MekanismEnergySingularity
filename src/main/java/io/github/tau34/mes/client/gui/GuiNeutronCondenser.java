package io.github.tau34.mes.client.gui;

import io.github.tau34.mes.common.tile.TileEntityNeutronCondenser;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiHorizontalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.tab.GuiEnergyTab;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GuiNeutronCondenser extends GuiConfigurableTile<TileEntityNeutronCondenser, MekanismTileContainer<TileEntityNeutronCondenser>> {
    public GuiNeutronCondenser(MekanismTileContainer<TileEntityNeutronCondenser> container, Inventory inv, Component title) {
        super(container, inv, title);
        this.inventoryLabelY += 2;
        this.titleLabelY = 4;
        this.dynamicSlots = true;
    }

    protected void addGuiElements() {
        super.addGuiElements();
        this.addRenderableWidget(new GuiHorizontalPowerBar(this, this.tile.getEnergyContainer(), 115, 75)).warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY)).warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY_REDUCED_RATE, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE));
        MachineEnergyContainer<TileEntityNeutronCondenser> var10004 = this.tile.getEnergyContainer();
        TileEntityNeutronCondenser var10005 = this.tile;
        Objects.requireNonNull(var10005);
        this.addRenderableWidget(new GuiEnergyTab(this, var10004, var10005::getEnergyUsed));
        this.addRenderableWidget(new GuiGasGauge(() -> this.tile.inputTank, () -> this.tile.getGasTanks(null), GaugeType.STANDARD, this, 25, 13)).warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT));
        this.addRenderableWidget(new GuiGasGauge(() -> this.tile.outputTank, () -> this.tile.getGasTanks(null), GaugeType.STANDARD, this, 133, 13)).warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE));
        TileEntityNeutronCondenser var10003 = this.tile;
        Objects.requireNonNull(var10003);
        this.addRenderableWidget((new GuiProgress(var10003::getActive, ProgressType.LARGE_RIGHT, this, 64, 39)).jeiCategory(this.tile)).warning(WarningTracker.WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, this.tile.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT));
    }

    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        this.renderTitleText(guiGraphics);
        this.drawString(guiGraphics, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, this.titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
