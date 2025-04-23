package io.github.tau34.mes.client.gui;

import com.jerry.generator_extras.common.network.to_server.ExtraPacketGeneratorsGuiInteract;
import io.github.tau34.mes.common.network.MESPacketGuiInteract;
import io.github.tau34.mes.common.tile.zpm.TileEntityZPMLogicAdapter;
import mekanism.api.text.EnumColor;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.element.GuiElementHolder;
import mekanism.client.gui.element.button.ToggleButton;
import mekanism.client.gui.element.scroll.GuiScrollBar;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.container.tile.EmptyTileContainer;
import mekanism.common.network.to_server.PacketGuiInteract;
import mekanism.common.util.text.BooleanStateDisplay;
import mekanism.generators.client.gui.element.button.ReactorLogicButton;
import mekanism.generators.common.GeneratorsLang;
import mekanism.generators.common.MekanismGenerators;
import mekanism.generators.common.base.IReactorLogic;
import mekanism.generators.common.tile.fission.TileEntityFissionReactorLogicAdapter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.IntSupplier;

public class GuiZPMLogicAdapter extends GuiMekanismTile<TileEntityZPMLogicAdapter, EmptyTileContainer<TileEntityZPMLogicAdapter>> {
    private static final int DISPLAY_COUNT = 4;
    private GuiScrollBar scrollBar;
    
    public GuiZPMLogicAdapter(EmptyTileContainer<TileEntityZPMLogicAdapter> container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    protected void addGuiElements() {
        super.addGuiElements();
        this.addRenderableWidget(new GuiElementHolder(this, 16, 31, 130, 90));
        TileEntityZPMLogicAdapter var10007 = Objects.requireNonNull(this.tile);
        this.scrollBar = this.addRenderableWidget(new GuiScrollBar(this, 146, 31, 90, () -> var10007.getModes().length, () -> 4));

        for (int i = 0; i < 4; ++i) {
            int typeShift = 22 * i;
            int var10005 = 32 + typeShift;
            GuiScrollBar var10008 = this.scrollBar;
            Objects.requireNonNull(var10008);
            IntSupplier var4 = var10008::getCurrentSelection;
            TileEntityZPMLogicAdapter var10009 = this.tile;
            Objects.requireNonNull(var10009);
            this.addRenderableWidget(new ReactorLogicButton<>(this, 17, var10005, i, this.tile, var4, var10009::getModes, (type) -> {
                if (type != null) {
                    Mekanism.packetHandler().sendToServer(new MESPacketGuiInteract(MESPacketGuiInteract.MESGuiInteraction.LOGIC_TYPE, this.tile, type.ordinal()));
                }
            }));
        }

    }

    protected void drawForegroundText(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        this.renderTitleText(guiGraphics);
        this.drawTextScaledBound(guiGraphics, GeneratorsLang.REACTOR_LOGIC_REDSTONE_MODE.translate(this.tile.logic.getColor(), this.tile.logic), 16.0F, 123.0F, this.titleTextColor(), 144.0F);
        this.drawCenteredText(guiGraphics, MekanismLang.STATUS.translate(EnumColor.RED, this.tile.getStatus()), 0.0F, (float)this.imageWidth, 136.0F, this.titleTextColor());
        super.drawForegroundText(guiGraphics, mouseX, mouseY);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return super.mouseScrolled(mouseX, mouseY, delta) || this.scrollBar.adjustScroll(delta);
    }
}
