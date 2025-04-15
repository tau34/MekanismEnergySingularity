package io.github.tau34.mes.common.recipe.cache;

import io.github.tau34.mes.common.recipe.MESRecipeType;
import mekanism.api.functions.ConstantPredicates;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.common.recipe.lookup.cache.type.IInputCache;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class MESSingleInputRecipeCache<I, IG extends InputIngredient<I>, R extends MekanismRecipe & Predicate<I>, C extends IInputCache<I, IG, R>> extends MESAbstractInputRecipeCache<R> {
    private final Set<R> complexRecipes = new HashSet<>();
    private final Function<R, IG> inputExtractor;
    private final C cache;

    protected MESSingleInputRecipeCache(MESRecipeType<R, ?> recipeType, Function<R, IG> inputExtractor, C cache) {
        super(recipeType);
        this.inputExtractor = inputExtractor;
        this.cache = cache;
    }

    @Override
    public void clear() {
        super.clear();
        cache.clear();
        complexRecipes.clear();
    }

    public boolean containsInput(@Nullable Level world, I input) {
        return containsInput(world, input, inputExtractor, cache, complexRecipes);
    }

    @Nullable
    public R findFirstRecipe(@Nullable Level world, I input) {
        if (cache.isEmpty(input)) {
            return null;
        }
        initCacheIfNeeded(world);
        Predicate<R> matchPredicate = recipe -> recipe.test(input);
        R recipe = cache.findFirstRecipe(input, matchPredicate);
        return recipe == null ? findFirstRecipe(complexRecipes, matchPredicate) : recipe;
    }

    @Nullable
    public R findTypeBasedRecipe(@Nullable Level world, I input) {
        return findTypeBasedRecipe(world, input, ConstantPredicates.alwaysTrue());
    }

    @Nullable
    public R findTypeBasedRecipe(@Nullable Level world, I input, Predicate<R> matchCriteria) {
        if (cache.isEmpty(input)) {
            return null;
        }
        initCacheIfNeeded(world);
        R recipe = cache.findFirstRecipe(input, matchCriteria);
        return recipe == null ? findFirstRecipe(complexRecipes, r -> inputExtractor.apply(r).testType(input) && matchCriteria.test(r)) : recipe;
    }

    @Override
    protected void initCache(List<R> recipes) {
        for(R recipe : recipes) {
            if (this.cache.mapInputs(recipe, this.inputExtractor.apply(recipe))) {
                this.complexRecipes.add(recipe);
            }
        }
    }
}
