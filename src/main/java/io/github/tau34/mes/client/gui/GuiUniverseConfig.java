package io.github.tau34.mes.client.gui;

import io.github.tau34.mes.MESLang;
import io.github.tau34.mes.client.element.EnumSwitch;
import io.github.tau34.mes.common.network.MESPacketGuiButtonPress;
import io.github.tau34.mes.common.network.MESPacketGuiInteract;
import io.github.tau34.mes.common.tile.TileEntityUniverseGenerator;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.button.MekanismButton;
import mekanism.client.gui.element.button.MekanismImageButton;
import mekanism.client.gui.element.button.TranslationButton;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.network.to_server.PacketGuiButtonPress;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiUniverseConfig extends GuiMekanismTile<TileEntityUniverseGenerator, MekanismTileContainer<TileEntityUniverseGenerator>> {
    public GuiUniverseConfig(MekanismTileContainer<TileEntityUniverseGenerator> container, Inventory inv, Component title) {
        super(container, inv, title);
        this.dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        TileEntityUniverseGenerator tile = this.tile;
        this.addRenderableWidget(new EnumSwitch<>(this, 34, 5, 30, 8, TileEntityUniverseGenerator.OreDensity.SPARSE, tile::getDensityOrdinal, MESLang.UNIVERSE_DENSITY.translate(), (i) -> Mekanism.packetHandler().sendToServer(new MESPacketGuiInteract(MESPacketGuiInteract.MESGuiInteraction.UNIVERSE_DENSITY, tile, i))));
        this.addRenderableWidget(new EnumSwitch<>(this, 34, 20, 30, 8, TileEntityUniverseGenerator.RareOre.SPARSE, tile::getRareOrdinal, MESLang.UNIVERSE_RARE.translate(), (i) -> Mekanism.packetHandler().sendToServer(new MESPacketGuiInteract(MESPacketGuiInteract.MESGuiInteraction.UNIVERSE_RARE, tile, i))));
        this.addRenderableWidget(new EnumSwitch<>(this, 34, 35, 30, 8, TileEntityUniverseGenerator.Radius.COMPACT, tile::getRadiusOrdinal, MESLang.UNIVERSE_RADIUS.translate(), (i) -> Mekanism.packetHandler().sendToServer(new MESPacketGuiInteract(MESPacketGuiInteract.MESGuiInteraction.UNIVERSE_RADIUS, tile, i))));
        this.addRenderableWidget(new EnumSwitch<>(this, 34, 50, 30, 8, TileEntityUniverseGenerator.Gravity.LOW, tile::getGravityOrdinal, MESLang.UNIVERSE_GRAVITY.translate(), (i) -> Mekanism.packetHandler().sendToServer(new MESPacketGuiInteract(MESPacketGuiInteract.MESGuiInteraction.UNIVERSE_GRAVITY, tile, i))));
        this.addRenderableWidget(new EnumSwitch<>(this, 34, 65, 30, 8, TileEntityUniverseGenerator.CrustType.STANDARD, tile::getCrustOrdinal, MESLang.UNIVERSE_CRUST.translate(), (i) -> Mekanism.packetHandler().sendToServer(new MESPacketGuiInteract(MESPacketGuiInteract.MESGuiInteraction.UNIVERSE_CRUST, tile, i))));
        this.addRenderableWidget(new MekanismImageButton(this, 5, 5, 11, 14, this.getButtonLocation("back"), () -> Mekanism.packetHandler().sendToServer(new MESPacketGuiButtonPress(MESPacketGuiButtonPress.ClickedTileButton.BACK_BUTTON, this.tile)), this.getOnHover(MekanismLang.BACK)));
    }
}
