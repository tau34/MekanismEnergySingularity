package io.github.tau34.mes.common.recipe.cache;

import io.github.tau34.mes.common.recipe.MESRecipeType;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.common.recipe.lookup.cache.type.ChemicalInputCache;

import java.util.function.Function;
import java.util.function.Predicate;

public class MESInputRecipeCache {
    public static class SingleChemical<C extends Chemical<C>, S extends ChemicalStack<C>, R extends MekanismRecipe & Predicate<S>> extends MESSingleInputRecipeCache<S, ChemicalStackIngredient<C, S>, R, ChemicalInputCache<C, S, R>> {
        public SingleChemical(MESRecipeType<R, ?> recipeType, Function<R, ChemicalStackIngredient<C, S>> inputExtractor) {
            super(recipeType, inputExtractor, new ChemicalInputCache<>());
        }
    }
}
