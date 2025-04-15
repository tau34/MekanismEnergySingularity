package io.github.tau34.mes.common.registration;

import io.github.tau34.mes.common.recipe.IMESRecipeTypeProvider;
import io.github.tau34.mes.common.recipe.MESRecipeType;
import io.github.tau34.mes.common.registration.impl.MESRecipeTypeRegistryObject;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.registration.WrappedDeferredRegister;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

public class MESRecipeTypeDeferredRegister extends WrappedDeferredRegister<RecipeType<?>> {
    private final List<IMESRecipeTypeProvider<?, ?>> recipeTypes = new ArrayList<>();

    public MESRecipeTypeDeferredRegister(String mod_id) {
        super(mod_id, ForgeRegistries.RECIPE_TYPES);
    }

    public <R extends MekanismRecipe, IC extends IInputRecipeCache> MESRecipeTypeRegistryObject<R, IC> register(String name, Supplier<? extends MESRecipeType<R, IC>> sup) {
        MESRecipeTypeRegistryObject<R, IC> registeredRecipeType = this.register(name, sup, MESRecipeTypeRegistryObject::new);
        this.recipeTypes.add(registeredRecipeType);
        return registeredRecipeType;
    }

    public List<IMESRecipeTypeProvider<?, ?>> getAllRecipeTypes() {
        return Collections.unmodifiableList(recipeTypes);
    }
}
