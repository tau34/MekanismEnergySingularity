package io.github.tau34.mes.common.recipe;

import mekanism.api.recipes.MekanismRecipe;
import mekanism.common.recipe.MekanismRecipeType;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public interface IMESRecipeTypeProvider<R extends MekanismRecipe, IC extends IInputRecipeCache> {
    default ResourceLocation getRegistryName() {
        return this.getRecipeType().getRegistryName();
    }

    MESRecipeType<R, IC> getRecipeType();

    default IC getInputCache() {
        return this.getRecipeType().getInputCache();
    }

    default @NotNull List<R> getRecipes(@Nullable Level world) {
        return this.getRecipeType().getRecipes(world);
    }

    default Stream<R> stream(@Nullable Level world) {
        return this.getRecipes(world).stream();
    }

    default @Nullable R findFirst(@Nullable Level world, Predicate<R> matchCriteria) {
        return this.stream(world).filter(matchCriteria).findFirst().orElse(null);
    }

    default boolean contains(@Nullable Level world, Predicate<R> matchCriteria) {
        return this.stream(world).anyMatch(matchCriteria);
    }
}
