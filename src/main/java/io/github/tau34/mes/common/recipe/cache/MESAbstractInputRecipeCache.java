package io.github.tau34.mes.common.recipe.cache;

import com.mojang.logging.LogUtils;
import io.github.tau34.mes.common.recipe.MESRecipeType;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import mekanism.common.recipe.lookup.cache.type.IInputCache;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class MESAbstractInputRecipeCache<R extends MekanismRecipe> implements IInputRecipeCache {
    protected final MESRecipeType<R, ?> recipeType;
    protected boolean initialized;

    protected MESAbstractInputRecipeCache(MESRecipeType<R, ?> recipeType) {
        this.recipeType = recipeType;
    }

    @Override
    public void clear() {
        this.initialized = false;
    }

    protected void initCacheIfNeeded(@Nullable Level world) {
        if (!initialized) {
            initialized = true;
            initCache(recipeType.getRecipes(world));
        }
    }

    protected abstract void initCache(List<R> recipes);

    protected @Nullable R findFirstRecipe(@Nullable Collection<R> recipes, Predicate<R> matchCriteria) {
        return recipes == null ? null : recipes.stream().filter(matchCriteria).findFirst().orElse(null);
    }

    protected <I, IG extends InputIngredient<I>, C extends IInputCache<I, IG, R>> boolean containsInput(@Nullable Level world, I input, Function<R, IG> inputExtractor, C cache, Set<R> complexRecipes) {
        if (cache.isEmpty(input)) {
            return false;
        } else {
            this.initCacheIfNeeded(world);
            return cache.contains(input) || complexRecipes.stream().anyMatch(recipe -> inputExtractor.apply(recipe).testType(input));
        }
    }

    protected <I1, IG1 extends InputIngredient<I1>, C1 extends IInputCache<I1, IG1, R>, I2, IG2 extends InputIngredient<I2>, C2 extends IInputCache<I2, IG2, R>> boolean containsPairing(@Nullable Level world, I1 input1, Function<R, IG1> input1Extractor, C1 cache1, Set<R> complexIngredients1, I2 input2, Function<R, IG2> input2Extractor, C2 cache2, Set<R> complexIngredients2) {
        if (cache1.isEmpty(input1)) {
            return this.containsInput(world, input2, input2Extractor, cache2, complexIngredients2);
        } else if (cache2.isEmpty(input2)) {
            return true;
        } else {
            this.initCacheIfNeeded(world);
            return cache1.contains(input1, recipe -> input2Extractor.apply(recipe).testType(input2)) || complexIngredients1.stream().anyMatch(recipe -> input1Extractor.apply(recipe).testType(input1) && input2Extractor.apply(recipe).testType(input2));
        }
    }
}
