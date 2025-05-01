package io.github.tau34.mes.client.gui;

import io.github.tau34.mes.common.multiblock.data.AdvancedFusionMultiblockData;
import io.github.tau34.mes.common.tile.fusion.TileEntityAdvancedFusionBlock;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.bar.GuiHorizontalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.inventory.warning.WarningTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiAdvancedFusion extends GuiMekanismTile<TileEntityAdvancedFusionBlock, MekanismTileContainer<TileEntityAdvancedFusionBlock>> {
    public GuiAdvancedFusion(MekanismTileContainer<TileEntityAdvancedFusionBlock> container, Inventory inv, Component title) {
        super(container, inv, title);
        this.inventoryLabelY += 2;
        this.titleLabelX = 5;
        this.titleLabelY = 5;
        this.dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        AdvancedFusionMultiblockData multiblock = this.tile.getMultiblock();
        this.addRenderableWidget(new GuiHorizontalPowerBar(this, multiblock.energyContainer, 115, 75).warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY, multiblock.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY))
                .warning(WarningTracker.WarningType.NOT_ENOUGH_ENERGY_REDUCED_RATE, multiblock.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE)));
        this.addRenderableWidget(new GuiGasGauge(() -> multiblock.leftTank, () -> multiblock.getGasTanks(null), GaugeType.STANDARD, this, 25, 13).warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, multiblock.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_LEFT_INPUT)));
        this.addRenderableWidget(new GuiGasGauge(() -> multiblock.rightTank, () -> multiblock.getGasTanks(null), GaugeType.STANDARD, this, 133, 13).warning(WarningTracker.WarningType.NO_MATCHING_RECIPE, multiblock.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_RIGHT_INPUT)));
        this.addRenderableWidget(new GuiGasGauge(() -> multiblock.outputTank, () -> multiblock.getGasTanks(null), GaugeType.STANDARD, this, 79, 13).warning(WarningTracker.WarningType.NO_SPACE_IN_OUTPUT, multiblock.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE)));
        this.addRenderableWidget(new GuiProgress(multiblock::isActive, ProgressType.SMALL_RIGHT, this, 47, 39).jeiCategory(this.tile).warning(WarningTracker.WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, multiblock.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT)));
        this.addRenderableWidget(new GuiProgress(multiblock::isActive, ProgressType.SMALL_LEFT, this, 101, 39).jeiCategory(this.tile).warning(WarningTracker.WarningType.INPUT_DOESNT_PRODUCE_OUTPUT, multiblock.getWarningCheck(CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT)));
    }

    @Override
    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        this.drawString(guiGraphics, this.title, this.titleLabelX, this.titleLabelY, this.titleTextColor());
        this.drawString(guiGraphics, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, this.titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }
}
