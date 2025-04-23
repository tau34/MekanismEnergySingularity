package io.github.tau34.mes.client.gui;

import io.github.tau34.mes.common.network.MESPacketGuiButtonPress;
import io.github.tau34.mes.common.tile.TileEntityUniverseGenerator;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.button.MekanismImageButton;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiUniverseGeneratorOutput extends GuiConfigurableTile<TileEntityUniverseGenerator, MekanismTileContainer<TileEntityUniverseGenerator>> {
    public GuiUniverseGeneratorOutput(MekanismTileContainer<TileEntityUniverseGenerator> container, Inventory inv, Component title) {
        super(container, inv, title);
        this.dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        this.addRenderableWidget(new MekanismImageButton(this, 5, 5, 11, 14, this.getButtonLocation("back"), () -> Mekanism.packetHandler().sendToServer(new MESPacketGuiButtonPress(MESPacketGuiButtonPress.ClickedTileButton.BACK_BUTTON, this.tile)), this.getOnHover(MekanismLang.BACK)));
    }
}
