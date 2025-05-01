package io.github.tau34.mes.client.jei;

import io.github.tau34.mes.common.register.MESBlocks;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.ChemicalInfuserRecipe;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiChemicalGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.jei.MekanismJEI;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.client.jei.machine.ChemicalChemicalToChemicalRecipeCategory;
import mezz.jei.api.helpers.IGuiHelper;

public class AdvancedFusionRecipeCategory extends ChemicalChemicalToChemicalRecipeCategory<Gas, GasStack, ChemicalInfuserRecipe> {
    public AdvancedFusionRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<ChemicalInfuserRecipe> recipeType) {
        super(helper, recipeType, MESBlocks.ADVANCED_FUSION_FRAME, MekanismJEI.TYPE_GAS, 3, 3, 170, 80);
    }

    @Override
    protected GuiChemicalGauge<Gas, GasStack, ?> getGauge(GaugeType type, int x, int y) {
        return GuiGasGauge.getDummy(type, this, x, y);
    }
}
