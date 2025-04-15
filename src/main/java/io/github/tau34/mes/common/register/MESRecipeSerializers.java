package io.github.tau34.mes.common.register;

import io.github.tau34.mes.common.recipe.impl.NeutronCondenserIRecipe;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.common.recipe.serializer.GasToGasRecipeSerializer;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerRegistryObject;

public class MESRecipeSerializers {
    public static final RecipeSerializerDeferredRegister REGISTER = new RecipeSerializerDeferredRegister("mes");

    public static final RecipeSerializerRegistryObject<GasToGasRecipe> NEUTRON_CONDENSING = REGISTER.register("neutron_condensing", () -> new GasToGasRecipeSerializer<>(NeutronCondenserIRecipe::new));
}
