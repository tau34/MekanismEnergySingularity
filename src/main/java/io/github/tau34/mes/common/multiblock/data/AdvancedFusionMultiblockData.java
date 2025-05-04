package io.github.tau34.mes.common.multiblock.data;

import com.mojang.logging.LogUtils;
import io.github.tau34.mes.common.recipe.IMESRecipeTypeProvider;
import io.github.tau34.mes.common.recipe.MESRecipeCacheLookupMonitor;
import io.github.tau34.mes.common.recipe.MESRecipeType;
import io.github.tau34.mes.common.recipe.cache.MESInputRecipeCache;
import io.github.tau34.mes.common.recipe.lookup.IMESEitherSideRecipeLookupHandler;
import io.github.tau34.mes.common.tile.fusion.TileEntityAdvancedFusionBlock;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.ChemicalInfuserRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.ChemicalChemicalToChemicalCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.chemical.multiblock.MultiblockChemicalTankBuilder;
import mekanism.common.capabilities.energy.VariableCapacityEnergyContainer;
import mekanism.common.inventory.container.sync.dynamic.ContainerSync;
import mekanism.common.lib.multiblock.IValveHandler;
import mekanism.common.lib.multiblock.MultiblockData;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.generators.common.registries.GeneratorsGases;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BooleanSupplier;

public class AdvancedFusionMultiblockData extends MultiblockData implements IValveHandler, IMESEitherSideRecipeLookupHandler.MESEitherSideChemicalRecipeLookupHandler<Gas, GasStack, ChemicalInfuserRecipe> {
    private static final List<CachedRecipe.OperationTracker.@NotNull RecipeError> TRACKED_ERROR_TYPES = List.of(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_LEFT_INPUT, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_RIGHT_INPUT, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE, CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    @ContainerSync
    public IEnergyContainer energyContainer;
    @ContainerSync
    public IGasTank leftTank;
    @ContainerSync
    public IGasTank rightTank;
    @ContainerSync
    public IGasTank outputTank;
    public List<IGasTank> leftTanks = new ArrayList<>();
    public List<IGasTank> rightTanks = new ArrayList<>();
    public List<IGasTank> outputTanks = new ArrayList<>();
    private final IOutputHandler<@NotNull GasStack> outputHandler;
    private final IInputHandler<@NotNull GasStack> leftInputHandler;
    private final IInputHandler<@NotNull GasStack> rightInputHandler;
    private final BooleanSupplier recheckAllRecipeErrors;
    @ContainerSync
    private final boolean[] trackedErrors;
    @ContainerSync
    private boolean isActive;
    private final MESRecipeCacheLookupMonitor<ChemicalInfuserRecipe> recipeCacheLookupMonitor;

    public AdvancedFusionMultiblockData(TileEntityAdvancedFusionBlock tile) {
        super(tile);
        this.recipeCacheLookupMonitor = new MESRecipeCacheLookupMonitor<>(this);
        this.energyContainers.add(energyContainer = VariableCapacityEnergyContainer.input(FloatingLong.createConst(5_000_000_000L), this));
        this.gasTanks.add(leftTank = MultiblockChemicalTankBuilder.GAS.input(this, () -> 10000L, this::containsRecipe, recipeCacheLookupMonitor));
        this.gasTanks.add(rightTank = MultiblockChemicalTankBuilder.GAS.input(this, () -> 10000L, this::containsRecipe, recipeCacheLookupMonitor));
        this.gasTanks.add(outputTank = MultiblockChemicalTankBuilder.GAS.output(this, () -> 10000L, gas -> true, this));
        this.leftTanks.add(leftTank);
        this.rightTanks.add(rightTank);
        this.outputTanks.add(outputTank);
        this.leftInputHandler = InputHelper.getInputHandler(this.leftTank, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_LEFT_INPUT);
        this.rightInputHandler = InputHelper.getInputHandler(this.rightTank, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_RIGHT_INPUT);
        this.outputHandler = OutputHelper.getOutputHandler(this.outputTank, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
        this.recheckAllRecipeErrors = TileEntityRecipeMachine.shouldRecheckAllErrors(tile);
        this.trackedErrors = new boolean[TRACKED_ERROR_TYPES.size()];
    }

    @Override
    public @NotNull IMESRecipeTypeProvider<ChemicalInfuserRecipe, MESInputRecipeCache.EitherSideChemical<Gas, GasStack, ChemicalInfuserRecipe>> getRecipeType() {
        return MESRecipeType.ADVANCED_FUSION;
    }

    @Override
    public @Nullable ChemicalInfuserRecipe getRecipe(int cacheIndex) {
        return this.findFirstRecipe(this.leftInputHandler, this.rightInputHandler);
    }

    @Override
    public @NotNull CachedRecipe<ChemicalInfuserRecipe> createNewCachedRecipe(@NotNull ChemicalInfuserRecipe recipe, int cacheIndex) {
        CachedRecipe<ChemicalInfuserRecipe> cr = new ChemicalChemicalToChemicalCachedRecipe<>(recipe, this.recheckAllRecipeErrors, this.leftInputHandler, this.rightInputHandler, this.outputHandler);
        LogUtils.getLogger().info(String.valueOf(recipe));
        return cr.setActive(b -> isActive = b).setErrorsChanged(recipeErrors -> {
            for (int i = 0; i < this.trackedErrors.length; i ++) {
                this.trackedErrors[i] = recipeErrors.contains(TRACKED_ERROR_TYPES.get(i));
            }
        }).setEnergyRequirements(() -> FloatingLong.create(250_000_000), this.energyContainer);
    }

    @Override
    public void clearRecipeErrors(int cacheIndex) {
        Arrays.fill(this.trackedErrors, false);
    }

    @Override
    public boolean tick(Level world) {
        boolean np = super.tick(world);
        recipeCacheLookupMonitor.updateAndProcess();
        return np;
    }

    public BooleanSupplier getWarningCheck(CachedRecipe.OperationTracker.RecipeError error) {
        int errorIndex = TRACKED_ERROR_TYPES.indexOf(error);
        return errorIndex == -1 ? () -> false : () -> this.trackedErrors[errorIndex];
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public Level getHandlerWorld() {
        return this.getWorld();
    }
}
