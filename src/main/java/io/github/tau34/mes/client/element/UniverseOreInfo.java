package io.github.tau34.mes.client.element;

import io.github.tau34.mes.MESLang;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiElement;
import mekanism.client.gui.element.button.MekanismButton;
import mekanism.client.gui.element.button.TranslationButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class UniverseOreInfo extends GuiElement {
    private final Supplier<Item> item;
    private final IntSupplier count;
    private final MekanismButton button;
    private final BooleanSupplier validity;

    public UniverseOreInfo(IGuiWrapper gui, int x, int y, Supplier<Item> item, IntSupplier count, Runnable onClick, BooleanSupplier validity) {
        super(gui, x, y, 150, 16);
        this.item = item;
        this.count = count;
        this.validity = validity;
        this.button = this.addChild(new TranslationButton(gui, this.relativeX + 110, this.relativeY, 40, 16, MESLang.BUTTON_MINE, onClick));
    }

    @Override
    public void drawBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.gui().renderItem(guiGraphics, item.get().getDefaultInstance(), this.relativeX + 1, this.relativeY);
    }

    @Override
    public void renderForeground(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderForeground(guiGraphics, mouseX, mouseY);
        this.drawScaledCenteredText(guiGraphics, MESLang.AVAILABLE.translate(formatCount(count.getAsInt())), this.relativeX + 64, this.relativeY + 4, 0x101010, 1F);
    }

    @Override
    public void renderToolTip(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderToolTip(guiGraphics, mouseX, mouseY);
        int relativeMouseX = mouseX - this.getX();
        if (0 < relativeMouseX && relativeMouseX < 18) {
            int relativeMouseY = mouseY - this.getY();
            if (-1 < relativeMouseY && relativeMouseY < 17) {
                this.displayTooltips(guiGraphics, mouseX, mouseY, item.get().getDefaultInstance().getHoverName());
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.button.active = validity.getAsBoolean();
    }

    private static String formatCount(int i) {
        DecimalFormat FORMAT_1 = new DecimalFormat("#.##");
        DecimalFormat FORMAT_2 = new DecimalFormat("##.#");
        DecimalFormat FORMAT_3 = new DecimalFormat("###");
        if (i < 1_000_000) {
            return String.valueOf(i);
        } else if (i < 10_000_000) {
            return FORMAT_1.format(((double) i) / 1E6D) + "M";
        } else if (i < 100_000_000) {
            return FORMAT_2.format(((double) i) / 1E6D) + "M";
        } else if (i < 1_000_000_000) {
            return FORMAT_3.format(((double) i) / 1E6D) + "M";
        } else {
            return FORMAT_1.format(((double) i) / 1E9D) + "B";
        }
    }
}
