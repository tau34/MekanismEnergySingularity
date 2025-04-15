package io.github.tau34.mes.common.registration.impl;

import io.github.tau34.mes.common.recipe.IMESRecipeTypeProvider;
import io.github.tau34.mes.common.recipe.MESRecipeType;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.registration.WrappedRegistryObject;
import net.minecraftforge.registries.RegistryObject;

public class MESRecipeTypeRegistryObject<R extends MekanismRecipe, IC extends IInputRecipeCache> extends WrappedRegistryObject<MESRecipeType<R, IC>> implements IMESRecipeTypeProvider<R, IC> {
    public MESRecipeTypeRegistryObject(RegistryObject<MESRecipeType<R, IC>> registryObject) {
        super(registryObject);
    }

    @Override
    public MESRecipeType<R, IC> getRecipeType() {
        return this.get();
    }
}
