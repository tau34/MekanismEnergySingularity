package io.github.tau34.mes.client.gui;

import com.mojang.logging.LogUtils;
import io.github.tau34.mes.client.element.UniverseOreInfo;
import io.github.tau34.mes.common.network.MESPacketGuiButtonPress;
import io.github.tau34.mes.common.network.MESPacketGuiInteract;
import io.github.tau34.mes.common.tile.TileEntityUniverseGenerator;
import mekanism.client.gui.GuiConfigurableTile;
import mekanism.client.gui.element.button.MekanismImageButton;
import mekanism.client.gui.element.scroll.GuiScrollBar;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class GuiUniverseGeneratorManager extends GuiConfigurableTile<TileEntityUniverseGenerator, MekanismTileContainer<TileEntityUniverseGenerator>> {
    private GuiScrollBar scrollBar;

    public GuiUniverseGeneratorManager(MekanismTileContainer<TileEntityUniverseGenerator> container, Inventory inv, Component title) {
        super(container, inv, title);
        this.dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        scrollBar = this.addRenderableWidget(new GuiScrollBar(this, 156, 4, 76, () -> 13, () -> 4));
        this.addRenderableWidget(new UniverseOreInfo(this, 4, 4, getOreFromIndex(0), getOres(0), getOnClick(0), () -> this.tile.canStartMining(scrollBar.getCurrentSelection())));
        this.addRenderableWidget(new UniverseOreInfo(this, 4, 24, getOreFromIndex(1), getOres(1), getOnClick(1), () -> this.tile.canStartMining(scrollBar.getCurrentSelection() + 1)));
        this.addRenderableWidget(new UniverseOreInfo(this, 4, 44, getOreFromIndex(2), getOres(2), getOnClick(2), () -> this.tile.canStartMining(scrollBar.getCurrentSelection() + 2)));
        this.addRenderableWidget(new UniverseOreInfo(this, 4, 64, getOreFromIndex(3), getOres(3), getOnClick(3), () -> this.tile.canStartMining(scrollBar.getCurrentSelection() + 3)));
        this.addRenderableWidget(new MekanismImageButton(this, 4, -14, 11, 14, this.getButtonLocation("back"), () -> Mekanism.packetHandler().sendToServer(new MESPacketGuiButtonPress(MESPacketGuiButtonPress.ClickedTileButton.BACK_BUTTON, this.tile)), this.getOnHover(MekanismLang.BACK)));
    }

    private Supplier<Item> getOreFromIndex(int index) {
        return () -> TileEntityUniverseGenerator.ORE_ITEMS[scrollBar.getCurrentSelection() + index];
    }

    private IntSupplier getOres(int index) {
        return () -> this.tile.getOre(scrollBar.getCurrentSelection() + index);
    }

    private Runnable getOnClick(int index) {
        return () -> Mekanism.packetHandler().sendToServer(new MESPacketGuiInteract(MESPacketGuiInteract.MESGuiInteraction.UNIVERSE_MINE, this.tile, scrollBar.getCurrentSelection() + index));
    }
}
