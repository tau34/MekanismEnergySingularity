package io.github.tau34.mes.common.recipe.lookup;

import io.github.tau34.mes.common.recipe.IMESRecipeTypeProvider;
import mekanism.api.IContentsListener;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.IRecipeLookupHandler;
import mekanism.common.recipe.lookup.cache.IInputRecipeCache;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IMESRecipeLookupHandler<R extends MekanismRecipe> extends IContentsListener {
    @Nullable
    default Level getHandlerWorld() {
        if (this instanceof BlockEntity tile) {
            return tile.getLevel();
        } else if (this instanceof Entity entity) {
            return entity.level();
        }
        return null;
    }

    @NotNull IMESRecipeTypeProvider<R, ?> getRecipeType();

    default int getSavedOperatingTicks(int cacheIndex) {
        return 0;
    }

    @Nullable R getRecipe(int cacheIndex);

    @NotNull CachedRecipe<R> createNewCachedRecipe(@NotNull R recipe, int cacheIndex);

    default void onCachedRecipeChanged(@Nullable CachedRecipe<R> cachedRecipe, int cacheIndex) {
        this.clearRecipeErrors(cacheIndex);
    }

    default void clearRecipeErrors(int cacheIndex) {
    }

    public interface ConstantUsageRecipeLookupHandler {
        default long getSavedUsedSoFar(int cacheIndex) {
            return 0L;
        }
    }

    public interface IMESRecipeTypedLookupHandler<R extends MekanismRecipe, IC extends IInputRecipeCache> extends IMESRecipeLookupHandler<R> {
        @NotNull IMESRecipeTypeProvider<R, IC> getRecipeType();
    }
}
