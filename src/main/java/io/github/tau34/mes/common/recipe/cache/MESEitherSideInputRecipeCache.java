package io.github.tau34.mes.common.recipe.cache;

import com.mojang.logging.LogUtils;
import io.github.tau34.mes.common.recipe.MESRecipeType;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.common.recipe.lookup.cache.type.IInputCache;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class MESEitherSideInputRecipeCache<I, IG extends InputIngredient<I>, R extends MekanismRecipe & BiPredicate<I, I>, C extends IInputCache<I, IG, R>> extends MESAbstractInputRecipeCache<R> {
    private final Set<R> complexRecipes = new HashSet<>();
    private final Function<R, IG> inputAExtractor;
    private final Function<R, IG> inputBExtractor;
    private final C cache;

    protected MESEitherSideInputRecipeCache(MESRecipeType<R, ?> recipeType, Function<R, IG> inputAExtractor, Function<R, IG> inputBExtractor, C cache) {
        super(recipeType);
        this.inputAExtractor = inputAExtractor;
        this.inputBExtractor = inputBExtractor;
        this.cache = cache;
    }

    public void clear() {
        super.clear();
        this.cache.clear();
        this.complexRecipes.clear();
    }

    public boolean containsInput(@Nullable Level world, I input) {
        if (this.cache.isEmpty(input)) {
            return false;
        } else {
            this.initCacheIfNeeded(world);
            return this.cache.contains(input) || this.complexRecipes.stream().anyMatch((recipe) -> this.inputAExtractor.apply(recipe).testType(input) || this.inputBExtractor.apply(recipe).testType(input));
        }
    }

    public boolean containsInput(@Nullable Level world, I inputA, I inputB) {
        if (this.cache.isEmpty(inputA)) {
            return this.containsInput(world, inputB);
        } else if (this.cache.isEmpty(inputB)) {
            return true;
        } else {
            this.initCacheIfNeeded(world);
            return this.cache.contains(inputA, (recipe) -> {
                IG ingredientA = this.inputAExtractor.apply(recipe);
                IG ingredientB = this.inputBExtractor.apply(recipe);
                return ingredientB.testType(inputB) && ingredientA.testType(inputA) || ingredientA.testType(inputB) && ingredientB.testType(inputA);
            }) || this.complexRecipes.stream().anyMatch((recipe) -> {
                IG ingredientA = this.inputAExtractor.apply(recipe);
                IG ingredientB = this.inputBExtractor.apply(recipe);
                return ingredientA.testType(inputA) && ingredientB.testType(inputB) || ingredientB.testType(inputA) && ingredientA.testType(inputB);
            });
        }
    }

    public @Nullable R findFirstRecipe(@Nullable Level world, I inputA, I inputB) {
        if (!this.cache.isEmpty(inputA) && !this.cache.isEmpty(inputB)) {
            this.initCacheIfNeeded(world);
            Predicate<R> matchPredicate = (r) -> r.test(inputA, inputB);
            R recipe = this.cache.findFirstRecipe(inputA, matchPredicate);
            return (R)(recipe == null ? this.findFirstRecipe(this.complexRecipes, matchPredicate) : recipe);
        } else {
            return null;
        }
    }

    protected void initCache(List<R> recipes) {
        LogUtils.getLogger().info("either side recipes: {}", recipes);
        for (R recipe : recipes) {
            LogUtils.getLogger().info("either side recipe: {}", recipe);
            boolean complexA = this.cache.mapInputs(recipe, this.inputAExtractor.apply(recipe));
            boolean complexB = this.cache.mapInputs(recipe, this.inputBExtractor.apply(recipe));
            if (complexA || complexB) {
                this.complexRecipes.add(recipe);
            }
        }

    }
}
