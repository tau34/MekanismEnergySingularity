package io.github.tau34.mes.common.tile.base;

import io.github.tau34.mes.common.recipe.MESRecipeCacheLookupMonitor;
import io.github.tau34.mes.common.recipe.lookup.IMESRecipeLookupHandler;
import mekanism.api.IContentsListener;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.ISlurryTank;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BooleanSupplier;

public abstract class MESTileEntityRecipeMachine<RECIPE extends MekanismRecipe> extends TileEntityConfigurableMachine implements IMESRecipeLookupHandler<RECIPE> {
    public static final int RECIPE_CHECK_FREQUENCY = 100;
    protected final BooleanSupplier recheckAllRecipeErrors;
    private final List<CachedRecipe.OperationTracker.RecipeError> errorTypes;
    private final boolean[] trackedErrors;
    protected MESRecipeCacheLookupMonitor<RECIPE> recipeCacheLookupMonitor;
    private @Nullable IContentsListener recipeCacheSaveOnlyListener;

    public MESTileEntityRecipeMachine(IBlockProvider blockProvider, BlockPos pos, BlockState state, List<CachedRecipe.OperationTracker.RecipeError> errorTypes) {
        super(blockProvider, pos, state);
        this.errorTypes = List.copyOf(errorTypes);
        this.recheckAllRecipeErrors = shouldRecheckAllErrors(this);
        this.trackedErrors = new boolean[this.errorTypes.size()];
        this.recipeCacheSaveOnlyListener = null;
    }

    protected void presetVariables() {
        super.presetVariables();
        this.recipeCacheLookupMonitor = this.createNewCacheMonitor();
    }

    protected MESRecipeCacheLookupMonitor<RECIPE> createNewCacheMonitor() {
        return new MESRecipeCacheLookupMonitor<>(this);
    }

    protected IContentsListener getRecipeCacheSaveOnlyListener() {
        if (this.supportsComparator()) {
            if (this.recipeCacheSaveOnlyListener == null) {
                this.recipeCacheSaveOnlyListener = () -> {
                    this.markForSave();
                    this.recipeCacheLookupMonitor.onChange();
                };
            }

            return this.recipeCacheSaveOnlyListener;
        } else {
            return this.recipeCacheLookupMonitor;
        }
    }

    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.trackArray(this.trackedErrors);
    }

    public void clearRecipeErrors(int cacheIndex) {
        Arrays.fill(this.trackedErrors, false);
    }

    protected void onErrorsChanged(Set<CachedRecipe.OperationTracker.RecipeError> errors) {
        for(int i = 0; i < this.trackedErrors.length; ++i) {
            this.trackedErrors[i] = errors.contains(this.errorTypes.get(i));
        }

    }

    public BooleanSupplier getWarningCheck(CachedRecipe.OperationTracker.RecipeError error) {
        int errorIndex = this.errorTypes.indexOf(error);
        return errorIndex == -1 ? () -> false : () -> this.trackedErrors[errorIndex];
    }

    public static BooleanSupplier shouldRecheckAllErrors(TileEntityMekanism tile) {
        int checkOffset = ThreadLocalRandom.current().nextInt(100);
        return () -> !tile.playersUsing.isEmpty() && tile.hasLevel() && tile.getLevel().getGameTime() % 100L == (long)checkOffset;
    }

    @Override
    public final @Nullable IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener) {
        return this.getInitialGasTanks(listener, listener == this ? this.recipeCacheLookupMonitor : this.getRecipeCacheSaveOnlyListener());
    }

    protected @Nullable IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        return null;
    }

    @Override
    public final @Nullable IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(IContentsListener listener) {
        return this.getInitialInfusionTanks(listener, listener == this ? this.recipeCacheLookupMonitor : this.getRecipeCacheSaveOnlyListener());
    }

    protected @Nullable IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        return null;
    }

    @Override
    public final @Nullable IChemicalTankHolder<Pigment, PigmentStack, IPigmentTank> getInitialPigmentTanks(IContentsListener listener) {
        return this.getInitialPigmentTanks(listener, listener == this ? this.recipeCacheLookupMonitor : this.getRecipeCacheSaveOnlyListener());
    }

    protected @Nullable IChemicalTankHolder<Pigment, PigmentStack, IPigmentTank> getInitialPigmentTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        return null;
    }

    @Override
    public final @Nullable IChemicalTankHolder<Slurry, SlurryStack, ISlurryTank> getInitialSlurryTanks(IContentsListener listener) {
        return this.getInitialSlurryTanks(listener, listener == this ? this.recipeCacheLookupMonitor : this.getRecipeCacheSaveOnlyListener());
    }

    protected @Nullable IChemicalTankHolder<Slurry, SlurryStack, ISlurryTank> getInitialSlurryTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        return null;
    }

    @Override
    protected final @Nullable IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        return this.getInitialFluidTanks(listener, listener == this ? this.recipeCacheLookupMonitor : this.getRecipeCacheSaveOnlyListener());
    }

    protected @Nullable IFluidTankHolder getInitialFluidTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        return null;
    }

    @Override
    protected final @Nullable IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        return this.getInitialEnergyContainers(listener, listener == this ? this.recipeCacheLookupMonitor : this.getRecipeCacheSaveOnlyListener());
    }

    protected @Nullable IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener, IContentsListener recipeCacheListener) {
        return null;
    }

    @Override
    protected final @Nullable IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        return this.getInitialInventory(listener, listener == this ? this.recipeCacheLookupMonitor : this.getRecipeCacheSaveOnlyListener());
    }

    protected @Nullable IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener) {
        return null;
    }

    @Override
    protected final @Nullable IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener, CachedAmbientTemperature ambientTemperature) {
        return this.getInitialHeatCapacitors(listener, listener == this ? this.recipeCacheLookupMonitor : this.getRecipeCacheSaveOnlyListener(), ambientTemperature);
    }

    protected @Nullable IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener, IContentsListener recipeCacheListener, CachedAmbientTemperature ambientTemperature) {
        return null;
    }
}
