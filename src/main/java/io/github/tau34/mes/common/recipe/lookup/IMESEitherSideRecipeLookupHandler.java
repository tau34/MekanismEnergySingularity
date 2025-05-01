package io.github.tau34.mes.common.recipe.lookup;

import io.github.tau34.mes.common.recipe.cache.MESEitherSideInputRecipeCache;
import io.github.tau34.mes.common.recipe.cache.MESInputRecipeCache;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.chemical.ChemicalChemicalToChemicalRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.common.util.ChemicalUtil;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiPredicate;

public interface IMESEitherSideRecipeLookupHandler<I, R extends MekanismRecipe & BiPredicate<I, I>, IC extends MESEitherSideInputRecipeCache<I, ?, R, ?>> extends IMESRecipeLookupHandler.IMESRecipeTypedLookupHandler<R, IC> {
    default boolean containsRecipe(I input) {
        return this.getRecipeType().getInputCache().containsInput(this.getHandlerWorld(), input);
    }

    default boolean containsRecipe(I inputA, I inputB) {
        return this.getRecipeType().getInputCache().containsInput(this.getHandlerWorld(), inputA, inputB);
    }

    default @Nullable R findFirstRecipe(I inputA, I inputB) {
        return this.getRecipeType().getInputCache().findFirstRecipe(this.getHandlerWorld(), inputA, inputB);
    }

    default @Nullable R findFirstRecipe(IInputHandler<I> inputAHandler, IInputHandler<I> inputBHandler) {
        return this.findFirstRecipe(inputAHandler.getInput(), inputBHandler.getInput());
    }

    public interface MESEitherSideChemicalRecipeLookupHandler<C extends Chemical<C>, S extends ChemicalStack<C>, R extends ChemicalChemicalToChemicalRecipe<C, S, ? extends ChemicalStackIngredient<C, S>>> extends IMESEitherSideRecipeLookupHandler<S, R, MESInputRecipeCache.EitherSideChemical<C, S, R>> {
        default boolean containsRecipe(C input) {
            return this.containsRecipe(ChemicalUtil.withAmount(input, 1L));
        }

        default boolean containsRecipe(C inputA, S inputB) {
            return this.containsRecipe(ChemicalUtil.withAmount(inputA, 1L), inputB);
        }
    }
}
