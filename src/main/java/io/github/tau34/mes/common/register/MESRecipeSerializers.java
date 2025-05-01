package io.github.tau34.mes.common.register;

import io.github.tau34.mes.common.recipe.impl.AdvancedFusionIRecipe;
import io.github.tau34.mes.common.recipe.impl.NeutronCondenserIRecipe;
import io.github.tau34.mes.common.recipe.impl.PlasmaCoolerIRecipe;
import mekanism.api.recipes.ChemicalInfuserRecipe;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.common.recipe.serializer.ChemicalInfuserRecipeSerializer;
import mekanism.common.recipe.serializer.GasToGasRecipeSerializer;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerRegistryObject;

public class MESRecipeSerializers {
    public static final RecipeSerializerDeferredRegister REGISTER = new RecipeSerializerDeferredRegister("mes");

    public static final RecipeSerializerRegistryObject<GasToGasRecipe> NEUTRON_CONDENSING = REGISTER.register("neutron_condensing", () -> new GasToGasRecipeSerializer<>(NeutronCondenserIRecipe::new));
    public static final RecipeSerializerRegistryObject<ChemicalInfuserRecipe> ADVANCED_FUSION = REGISTER.register("advanced_fusion", () -> new ChemicalInfuserRecipeSerializer<>(AdvancedFusionIRecipe::new));
    public static final RecipeSerializerRegistryObject<GasToGasRecipe> PLASMA_COOLING = REGISTER.register("plasma_cooling", () -> new GasToGasRecipeSerializer<>(PlasmaCoolerIRecipe::new));
}
