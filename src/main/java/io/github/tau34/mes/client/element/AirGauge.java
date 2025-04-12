package io.github.tau34.mes.client.element;

import io.github.tau34.mes.MESMod;
import io.github.tau34.mes.client.gui.GuiZPM;
import mekanism.api.text.EnumColor;
import mekanism.api.text.TextComponentUtil;
import mekanism.client.gui.GuiMekanismTile;
import mekanism.client.gui.GuiUtils;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.GuiTexturedElement;
import mekanism.client.gui.element.gauge.GaugeInfo;
import mekanism.client.gui.element.gauge.GaugeOverlay;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.Mekanism;
import mekanism.common.MekanismLang;
import mekanism.common.inventory.warning.ISupportsWarning;
import mekanism.common.inventory.warning.WarningTracker;
import mekanism.common.item.ItemConfigurator;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.interfaces.ISideConfiguration;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;

public class AirGauge extends GuiTexturedElement implements ISupportsWarning<AirGauge> {
    private final GuiZPM zpm;
    private @Nullable BooleanSupplier warningSupplier;

    public AirGauge(GuiZPM gui, int x, int y) {
        super(GaugeOverlay.MEDIUM.getBarOverlay(), gui, x, y, 34, 56);
        zpm = gui;
    }

    @Override
    public AirGauge warning(@NotNull WarningTracker.@NotNull WarningType type, @NotNull BooleanSupplier warningSupplier) {
        this.warningSupplier = ISupportsWarning.compound(this.warningSupplier, this.gui().trackWarning(type, warningSupplier));
        return this;
    }

    public int getScaledLevel() {
        return (this.height - 2) * zpm.getAir() / 400;
    }

    public @Nullable TextureAtlasSprite getIcon() {
        return MekanismRenderer.getSprite(Mekanism.rl("liquid/liquid"));
    }

    public Component getLabel() {
        return null;
    }

    public List<Component> getTooltipText() {
        return Collections.singletonList(TextComponentUtil.translate("label.air_percent", String.valueOf((zpm.getAir() * 2.5) / 10D)));
    }

    public @Nullable TransmissionType getTransmission() {
        return null;
    }

    public GaugeOverlay getGaugeOverlay() {
        return GaugeOverlay.MEDIUM;
    }

    protected GaugeInfo getGaugeColor() {
        return GaugeInfo.STANDARD;
    }

    protected void applyRenderColor(GuiGraphics guiGraphics) {
        guiGraphics.setColor(135F / 255F, 206F / 255F, 235F / 255F, 1F);
    }

    public void drawBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(guiGraphics, mouseX, mouseY, partialTicks);
        GaugeInfo color = this.getGaugeColor();
        this.renderExtendedTexture(guiGraphics, color.getResourceLocation(), color.getSideWidth(), color.getSideHeight());
        this.renderContents(guiGraphics);
    }

    public void renderContents(GuiGraphics guiGraphics) {
        boolean warning = this.warningSupplier != null && this.warningSupplier.getAsBoolean();
        if (warning) {
            guiGraphics.blit(GuiSlot.WARNING_BACKGROUND_TEXTURE, this.relativeX + 1, this.relativeY + 1, 0.0F, 0.0F, this.width - 2, this.height - 2, 256, 256);
        }

        int scale = this.getScaledLevel();
        TextureAtlasSprite icon = this.getIcon();
        if (scale > 0 && icon != null) {
            this.applyRenderColor(guiGraphics);
            this.drawTiledSprite(guiGraphics, this.relativeX + 1, this.relativeY + 1, this.height - 2, this.width - 2, scale, icon, GuiUtils.TilingDirection.UP_RIGHT);
            MekanismRenderer.resetColor(guiGraphics);
            if (warning && (double)scale / (double)(this.height - 2) > 0.98) {
                int halfWidth = (this.width - 2) / 2;
                guiGraphics.blit(WARNING_TEXTURE, this.relativeX + 1 + halfWidth, this.relativeY + 1, (float)halfWidth, 0.0F, halfWidth, this.height - 2, 256, 256);
            }
        }

        this.drawBarOverlay(guiGraphics);
    }

    public void drawBarOverlay(GuiGraphics guiGraphics) {
        GaugeOverlay gaugeOverlay = this.getGaugeOverlay();
        guiGraphics.blit(this.getResource(), this.relativeX + 1, this.relativeY + 1, this.getWidth() - 2, this.getHeight() - 2, 0.0F, 0.0F, gaugeOverlay.getWidth(), gaugeOverlay.getHeight(), gaugeOverlay.getWidth(), gaugeOverlay.getHeight());
    }

    public void renderToolTip(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderToolTip(guiGraphics, mouseX, mouseY);
        ItemStack stack = this.gui().getCarriedItem();
        EnumColor color = this.getGaugeColor().getColor();
        if (!stack.isEmpty() && stack.getItem() instanceof ItemConfigurator && color != null) {
            IGuiWrapper gui1 = this.gui();
            if (gui1 instanceof GuiMekanismTile<?, ?> gui) {
                TileEntityMekanism var14 = gui.getTileEntity();
                if (var14 instanceof ISideConfiguration sideConfig) {
                    if (this.getTransmission() != null) {
                        DataType dataType = null;
                        ConfigInfo config = sideConfig.getConfig().getConfig(this.getTransmission());
                        if (config != null) {
                            for(DataType type : config.getSupportedDataTypes()) {
                                if (type.getColor() == color) {
                                    dataType = type;
                                    break;
                                }
                            }
                        }

                        if (dataType == null) {
                            this.displayTooltips(guiGraphics, mouseX, mouseY, MekanismLang.GENERIC_PARENTHESIS.translateColored(color, new Object[]{color.getName()}));
                        } else {
                            this.displayTooltips(guiGraphics, mouseX, mouseY, MekanismLang.GENERIC_WITH_PARENTHESIS.translateColored(color, new Object[]{dataType, color.getName()}));
                        }
                    }
                }
            }
        } else {
            List<Component> list = new ArrayList<>();
            if (this.getLabel() != null) {
                list.add(this.getLabel());
            }

            list.addAll(this.getTooltipText());
            this.displayTooltips(guiGraphics, mouseX, mouseY, list);
        }

    }
}
