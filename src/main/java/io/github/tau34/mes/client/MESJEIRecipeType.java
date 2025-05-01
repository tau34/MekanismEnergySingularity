package io.github.tau34.mes.client;

import io.github.tau34.mes.common.register.MESBlocks;
import mekanism.api.recipes.ChemicalInfuserRecipe;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.client.jei.MekanismJEIRecipeType;

public class MESJEIRecipeType {
    public static final MekanismJEIRecipeType<GasToGasRecipe> NEUTRON_CONDENSING = new MekanismJEIRecipeType<>(MESBlocks.NEUTRON_CONDENSER, GasToGasRecipe.class);
    public static final MekanismJEIRecipeType<ChemicalInfuserRecipe> ADVANCED_FUSION = new MekanismJEIRecipeType<>(MESBlocks.ADVANCED_FUSION_FRAME, ChemicalInfuserRecipe.class);
    public static final MekanismJEIRecipeType<GasToGasRecipe> PLASMA_COOLING = new MekanismJEIRecipeType<>(MESBlocks.PLASMA_COOLER, GasToGasRecipe.class);
}
