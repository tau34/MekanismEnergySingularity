package io.github.tau34.mes.client.element;

import com.mojang.logging.LogUtils;
import io.github.tau34.mes.MESMod;
import mekanism.api.text.IHasTextComponent;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiDigitalSwitch;
import mekanism.client.gui.element.GuiTexturedElement;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.util.MekanismUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.IntSupplier;

public class EnumSwitch<E extends Enum<E> & IHasTextComponent> extends GuiTexturedElement {
    private E[] values;
    private final int width;
    private final IntSupplier stateSupplier;
    private final Component tooltip;
    private final Consumer<Integer> onChange;

    public EnumSwitch(IGuiWrapper gui, int x, int y, int width, int height, Enum<E> e, IntSupplier stateSupplier, Component tooltip, Consumer<Integer> onToggle) {
        super(MekanismUtils.getResource(MekanismUtils.ResourceType.GUI, "switch/enum.png"), gui, x, y, width * e.getDeclaringClass().getEnumConstants().length, height);
        this.stateSupplier = stateSupplier;
        this.tooltip = tooltip;
        this.onChange = onToggle;
        this.width = width;
        values = e.getDeclaringClass().getEnumConstants();
    }

    public void renderToolTip(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderToolTip(guiGraphics, mouseX, mouseY);
        this.displayTooltips(guiGraphics, mouseX, mouseY, this.tooltip);
    }

    public void drawBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(guiGraphics, mouseX, mouseY, partialTicks);
        int state = this.stateSupplier.getAsInt();
        for (E e : values) {
            int ord = e.ordinal();
            guiGraphics.blit(this.getResource(), this.relativeX + this.width * ord, this.relativeY, 0F, state == ord ? 0F : 8F, 30, 8, 30, 16);
        }
    }

    public void renderForeground(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderForeground(guiGraphics, mouseX, mouseY);
        for (E e : values) {
            this.drawScaledCenteredText(guiGraphics, e.getTextComponent(), this.relativeX + this.width * e.ordinal() + 15, this.relativeY, 0x101010, 0.5F);
        }
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        onChange.accept((int)Math.round((mouseX - (double)this.getX()) / (double)this.width - 0.5F));
    }
}
