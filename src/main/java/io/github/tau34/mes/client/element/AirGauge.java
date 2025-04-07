package io.github.tau34.mes.client.element;

import io.github.tau34.mes.MESMod;
import io.github.tau34.mes.client.gui.GuiZPM;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasBuilder;
import mekanism.api.text.TextComponentUtil;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.gauge.GaugeOverlay;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.render.MekanismRenderer;
import mekanism.common.lib.transmitter.TransmissionType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class AirGauge extends GuiGauge<Void> {
    private final GuiZPM zpm;

    public AirGauge(GuiZPM gui, int x, int y) {
        super(GaugeType.MEDIUM, gui, x, y, 34, 56);
        zpm = gui;
    }

    @Override
    public int getScaledLevel() {
        return dummy ? this.height - 2 : (this.height - 2) * zpm.getAir() / 400;
    }

    @Override
    public @Nullable TextureAtlasSprite getIcon() {
        return MekanismRenderer.getSprite(MESMod.rl("textures/chemical/air"));
    }

    @Override
    public Component getLabel() {
        return null;
    }

    @Override
    public List<Component> getTooltipText() {
        return dummy ? Collections.singletonList(TextComponentUtil.build(this.dummyType)) : Collections.singletonList(TextComponentUtil.translate("label.air_percent", String.valueOf((zpm.getAir() * 2.5) / 10D)));
    }

    @Override
    public @Nullable TransmissionType getTransmission() {
        return null;
    }

    @Override
    public GaugeOverlay getGaugeOverlay() {
        return super.getGaugeOverlay();
    }
}
