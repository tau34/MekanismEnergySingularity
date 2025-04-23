package io.github.tau34.mes.client.gui;

import io.github.tau34.mes.MESLang;
import io.github.tau34.mes.common.network.MESPacketGuiButtonPress;
import io.github.tau34.mes.common.network.MESPacketGuiInteract;
import io.github.tau34.mes.common.tile.TileEntityUniverseGenerator;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.button.MekanismButton;
import mekanism.client.gui.element.button.TranslationButton;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class GuiUniverseGenerator extends GuiConfigurableTile<TileEntityUniverseGenerator, MekanismTileContainer<TileEntityUniverseGenerator>> {
    private MekanismButton configButton;
    private MekanismButton generateButton;
    private MekanismButton manageButton;
    private MekanismButton disconnectButton;

    public GuiUniverseGenerator(MekanismTileContainer<TileEntityUniverseGenerator> container, Inventory inv, Component title) {
        super(container, inv, title);
        this.inventoryLabelY += 2;
        this.titleLabelY = 4;
        this.dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        this.addRenderableWidget(configButton = new TranslationButton(this, 87, 26, 61, 18, MekanismLang.BUTTON_CONFIG, () -> Mekanism.packetHandler().sendToServer(new MESPacketGuiButtonPress(MESPacketGuiButtonPress.ClickedTileButton.TAB_UNIVERSE_CONFIG, this.tile))));
        this.addRenderableWidget(new TranslationButton(this, 87, 46, 61, 18, MESLang.BUTTON_OUTPUT, () -> Mekanism.packetHandler().sendToServer(new MESPacketGuiButtonPress(MESPacketGuiButtonPress.ClickedTileButton.TAB_UNIVERSE_GENERATOR_OUTPUT, this.tile))));
        this.addRenderableWidget(generateButton = new TranslationButton(this, 16, 18, 61, 18, MESLang.BUTTON_GENERATE, () -> Mekanism.packetHandler().sendToServer(new MESPacketGuiInteract(MESPacketGuiInteract.MESGuiInteraction.UNIVERSE_GENERATE, this.tile))));
        this.addRenderableWidget(manageButton = new TranslationButton(this, 16, 36, 61, 18, MESLang.BUTTON_MANAGE, () -> Mekanism.packetHandler().sendToServer(new MESPacketGuiButtonPress(MESPacketGuiButtonPress.ClickedTileButton.TAB_UNIVERSE_GENERATOR_MANAGER, this.tile))));
        this.addRenderableWidget(disconnectButton = new TranslationButton(this, 16, 54, 61, 18, MESLang.BUTTON_DISCONNECT, () -> Mekanism.packetHandler().sendToServer(new MESPacketGuiInteract(MESPacketGuiInteract.MESGuiInteraction.UNIVERSE_DISCONNECT, this.tile))));
        this.addRenderableWidget(new GuiVerticalPowerBar(this, this.tile.getEnergyContainer(), 164, 15));
        updateButtons();
    }

    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        this.renderTitleText(guiGraphics);
        this.drawString(guiGraphics, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, this.titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

    @Override
    public void containerTick() {
        super.containerTick();
        updateButtons();
    }

    private void updateButtons() {
        configButton.active = this.tile.isUninitialized();
        generateButton.active = this.tile.isReady() && this.tile.isUninitialized();
        manageButton.active = this.tile.isUniversePresent();
        disconnectButton.active = this.tile.isUniversePresent();
    }
}
