package io.github.tau34.mes.client;

import io.github.tau34.mes.common.register.MESBlocks;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.client.jei.MekanismJEIRecipeType;

public class MESJEIRecipeType {
    public static final MekanismJEIRecipeType<GasToGasRecipe> NEUTRON_CONDENSING = new MekanismJEIRecipeType<>(MESBlocks.NEUTRON_CONDENSER, GasToGasRecipe.class);
}
