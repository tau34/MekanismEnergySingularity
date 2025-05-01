package io.github.tau34.mes.common.attribute;

import mekanism.api.chemical.attribute.ChemicalAttribute;
import mekanism.api.math.FloatingLongSupplier;
import mekanism.api.providers.IGasProvider;

import java.util.function.DoubleSupplier;

public class Plasma extends ChemicalAttribute {
    private final DoubleSupplier heat;

    public Plasma(DoubleSupplier heat) {
        this.heat = heat;
    }

    public DoubleSupplier getHeat() {
        return heat;
    }
}
