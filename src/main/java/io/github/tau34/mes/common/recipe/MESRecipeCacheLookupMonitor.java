package io.github.tau34.mes.common.recipe;

import io.github.tau34.mes.common.recipe.lookup.IMESRecipeLookupHandler;
import mekanism.api.IContentsListener;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.ICachedRecipeHolder;
import mekanism.api.recipes.cache.ItemStackConstantChemicalToItemStackCachedRecipe;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.recipe.lookup.IRecipeLookupHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MESRecipeCacheLookupMonitor<R extends MekanismRecipe> implements ICachedRecipeHolder<R>, IContentsListener {
    private final IMESRecipeLookupHandler<R> handler;
    protected final int cacheIndex;
    protected CachedRecipe<R> cachedRecipe;
    protected boolean hasNoRecipe;

    public MESRecipeCacheLookupMonitor(IMESRecipeLookupHandler<R> handler) {
        this(handler, 0);
    }

    public MESRecipeCacheLookupMonitor(IMESRecipeLookupHandler<R> handler, int cacheIndex) {
        this.handler = handler;
        this.cacheIndex = cacheIndex;
    }

    protected boolean cachedIndexMatches(int cacheIndex) {
        return this.cacheIndex == cacheIndex;
    }

    @Override
    public final void onContentsChanged() {
        this.handler.onContentsChanged();
        this.onChange();
    }

    public void onChange() {
        this.hasNoRecipe = false;
    }

    public FloatingLong updateAndProcess(IEnergyContainer energyContainer) {
        FloatingLong prev = energyContainer.getEnergy().copy();
        return this.updateAndProcess() ? prev.minusEqual(energyContainer.getEnergy()) : FloatingLong.ZERO;
    }

    public boolean updateAndProcess() {
        CachedRecipe<R> oldCache = this.cachedRecipe;
        this.cachedRecipe = this.getUpdatedCache(this.cacheIndex);
        if (this.cachedRecipe != oldCache) {
            this.handler.onCachedRecipeChanged(this.cachedRecipe, this.cacheIndex);
        }

        if (this.cachedRecipe != null) {
            this.cachedRecipe.process();
            return true;
        } else {
            return false;
        }
    }

    public void loadSavedData(@NotNull CachedRecipe<R> cached, int cacheIndex) {
        if (this.cachedIndexMatches(cacheIndex)) {
            ICachedRecipeHolder.super.loadSavedData(cached, cacheIndex);
            if (cached instanceof ItemStackConstantChemicalToItemStackCachedRecipe<?, ?, ?, ?> c) {
                IMESRecipeLookupHandler<?> var5 = this.handler;
                if (var5 instanceof IRecipeLookupHandler.ConstantUsageRecipeLookupHandler handler) {
                    c.loadSavedUsageSoFar(handler.getSavedUsedSoFar(cacheIndex));
                }
            }
        }
    }

    public int getSavedOperatingTicks(int cacheIndex) {
        return this.cachedIndexMatches(cacheIndex) ? this.handler.getSavedOperatingTicks(cacheIndex) : ICachedRecipeHolder.super.getSavedOperatingTicks(cacheIndex);
    }

    @Override
    public @Nullable CachedRecipe<R> getCachedRecipe(int cacheIndex) {
        return this.cachedIndexMatches(cacheIndex) ? this.cachedRecipe : null;
    }

    @Override
    public @Nullable R getRecipe(int cacheIndex) {
        return this.cachedIndexMatches(cacheIndex) ? this.handler.getRecipe(cacheIndex) : null;
    }

    @Override
    public @Nullable CachedRecipe<R> createNewCachedRecipe(@NotNull R recipe, int cacheIndex) {
        return this.cachedIndexMatches(cacheIndex) ? this.handler.createNewCachedRecipe(recipe, cacheIndex) : null;
    }

    public boolean invalidateCache() {
        return CommonWorldTickHandler.flushTagAndRecipeCaches;
    }

    public void setHasNoRecipe(int cacheIndex) {
        if (this.cachedIndexMatches(cacheIndex)) {
            this.hasNoRecipe = true;
        }

    }

    public boolean hasNoRecipe(int cacheIndex) {
        return this.cachedIndexMatches(cacheIndex) ? this.hasNoRecipe : ICachedRecipeHolder.super.hasNoRecipe(cacheIndex);
    }
}
