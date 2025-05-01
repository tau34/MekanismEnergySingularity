package io.github.tau34.mes.common.tile;

import io.github.tau34.mes.common.attribute.Plasma;
import io.github.tau34.mes.common.recipe.IMESRecipeTypeProvider;
import io.github.tau34.mes.common.recipe.MESRecipeType;
import io.github.tau34.mes.common.recipe.cache.MESInputRecipeCache;
import io.github.tau34.mes.common.recipe.lookup.IMESSingleRecipeLookupHandler;
import io.github.tau34.mes.common.register.MESBlocks;
import io.github.tau34.mes.common.tile.base.MESTileEntityRecipeMachine;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.Upgrade;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.heat.HeatAPI;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.OneInputCachedRecipe;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.api.recipes.outputs.OutputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.heat.BasicHeatCapacitor;
import mekanism.common.capabilities.heat.CachedAmbientTemperature;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.heat.HeatCapacitorHelper;
import mekanism.common.capabilities.holder.heat.IHeatCapacitorHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.integration.computer.SpecialComputerMethodWrapper;
import mekanism.common.integration.computer.annotation.ComputerMethod;
import mekanism.common.integration.computer.annotation.WrappingComputerMethod;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.slot.ContainerSlotType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.container.sync.SyncableDouble;
import mekanism.common.inventory.container.sync.SyncableFloatingLong;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.chemical.GasInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.base.SubstanceType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class TileEntityPlasmaCooler extends MESTileEntityRecipeMachine<GasToGasRecipe> implements IMESSingleRecipeLookupHandler.ChemicalRecipeLookupHandler<Gas, GasStack, GasToGasRecipe> {
    private static final List<CachedRecipe.OperationTracker.RecipeError> TRACKED_ERROR_TYPES = List.of(CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_ENERGY_REDUCED_RATE, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE, CachedRecipe.OperationTracker.RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);
    public static final int MAX_GAS = 10000;
    @WrappingComputerMethod(
            wrapper = SpecialComputerMethodWrapper.ComputerChemicalTankWrapper.class,
            methodNames = {"getInput", "getInputCapacity", "getInputNeeded", "getInputFilledPercentage"},
            docPlaceholder = "input tank"
    )
    public IGasTank inputTank;
    @WrappingComputerMethod(
            wrapper = SpecialComputerMethodWrapper.ComputerChemicalTankWrapper.class,
            methodNames = {"getOutput", "getOutputCapacity", "getOutputNeeded", "getOutputFilledPercentage"},
            docPlaceholder = "output tank"
    )
    public IGasTank outputTank;
    private int baselineMaxOperations;
    private final IOutputHandler<@NotNull GasStack> outputHandler;
    private final IInputHandler<@NotNull GasStack> inputHandler;
    @WrappingComputerMethod(
            wrapper = SpecialComputerMethodWrapper.ComputerHeatCapacitorWrapper.class,
            methodNames = {"getTemperature"},
            docPlaceholder = "heater"
    )
    BasicHeatCapacitor heatCapacitor;
    @WrappingComputerMethod(
            wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class,
            methodNames = {"getInputItem"},
            docPlaceholder = "input slot"
    )
    GasInventorySlot inputSlot;
    @WrappingComputerMethod(
            wrapper = SpecialComputerMethodWrapper.ComputerIInventorySlotWrapper.class,
            methodNames = {"getOutputItem"},
            docPlaceholder = "output slot"
    )
    GasInventorySlot outputSlot;
    private double lastEnvironmentLoss;
    private double lastTransferLoss;

    public TileEntityPlasmaCooler(BlockPos pos, BlockState state) {
        super(MESBlocks.PLASMA_COOLER, pos, state, TRACKED_ERROR_TYPES);
        this.baselineMaxOperations = 1;
        this.configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.GAS);
        this.configComponent.setupIOConfig(TransmissionType.GAS, this.inputTank, this.outputTank, RelativeSide.FRONT, false, true).setEjecting(true);
        this.configComponent.addDisabledSides(RelativeSide.TOP);
        this.ejectorComponent = new TileComponentEjector(this);
        this.ejectorComponent.setOutputData(this.configComponent, TransmissionType.ITEM, TransmissionType.GAS).setCanTankEject(tank -> tank != this.inputTank);
        this.inputHandler = InputHelper.getInputHandler(this.inputTank, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_INPUT);
        this.outputHandler = OutputHelper.getOutputHandler(this.outputTank, CachedRecipe.OperationTracker.RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
    }

    public @NotNull IChemicalTankHolder<Gas, GasStack, IGasTank> getInitialGasTanks(IContentsListener listener, IContentsListener recipeCacheListener) {
        ChemicalTankHelper<Gas, GasStack, IGasTank> builder = ChemicalTankHelper.forSideGasWithConfig(this::getDirection, this::getConfig);
        builder.addTank(this.inputTank = ChemicalTankBuilder.GAS.create(10000L, ChemicalTankHelper.radioactiveInputTankPredicate(() -> this.outputTank), ChemicalTankBuilder.GAS.alwaysTrueBi, this::containsRecipe, ChemicalAttributeValidator.ALWAYS_ALLOW, recipeCacheListener));
        builder.addTank(this.outputTank = ChemicalTankBuilder.GAS.output(10000L, listener));
        return builder.build();
    }

    protected @NotNull IInventorySlotHolder getInitialInventory(IContentsListener listener, IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(this.inputSlot = GasInventorySlot.fill(this.inputTank, listener, 5, 56));
        builder.addSlot(this.outputSlot = GasInventorySlot.drain(this.outputTank, listener, 155, 56));
        this.inputSlot.setSlotType(ContainerSlotType.INPUT);
        this.inputSlot.setSlotOverlay(SlotOverlay.MINUS);
        this.outputSlot.setSlotType(ContainerSlotType.OUTPUT);
        this.outputSlot.setSlotOverlay(SlotOverlay.PLUS);
        return builder.build();
    }

    @Override
    protected @Nullable IHeatCapacitorHolder getInitialHeatCapacitors(IContentsListener listener, IContentsListener recipeCacheListener, CachedAmbientTemperature ambientTemperature) {
        HeatCapacitorHelper builder = HeatCapacitorHelper.forSide(this::getDirection);
        builder.addCapacitor(this.heatCapacitor = BasicHeatCapacitor.create(100.0D, 5.0D, 100.0D, ambientTemperature, listener));
        return builder.build();
    }

    protected void onUpdateServer() {
        super.onUpdateServer();
        this.inputSlot.fillTank();
        this.outputSlot.drainTank();
        this.recipeCacheLookupMonitor.updateAndProcess();
        if (this.getActive()) {
            Plasma plasmaProperty = this.inputTank.getType().get(Plasma.class);
            if (plasmaProperty != null) {
                heatCapacitor.handleHeat(plasmaProperty.getHeat().getAsDouble() * this.baselineMaxOperations);
            }
        }
        HeatAPI.HeatTransfer transfer = this.simulate();
        this.lastEnvironmentLoss = transfer.environmentTransfer();
        this.lastTransferLoss = transfer.adjacentTransfer();
    }

    public @NotNull IMESRecipeTypeProvider<GasToGasRecipe, MESInputRecipeCache.SingleChemical<Gas, GasStack, GasToGasRecipe>> getRecipeType() {
        return MESRecipeType.PLASMA_COOLING;
    }

    public @Nullable GasToGasRecipe getRecipe(int cacheIndex) {
        return this.findFirstRecipe(this.inputHandler);
    }

    public @NotNull CachedRecipe<GasToGasRecipe> createNewCachedRecipe(@NotNull GasToGasRecipe recipe, int cacheIndex) {
        CachedRecipe<GasToGasRecipe> var10000 = OneInputCachedRecipe.chemicalToChemical(recipe, this.recheckAllRecipeErrors, this.inputHandler, this.outputHandler).setErrorsChanged(this::onErrorsChanged).setCanHolderFunction(() -> MekanismUtils.canFunction(this)).setActive(this::setActive).setOnFinish(this::markForSave);
        return var10000.setBaselineMaxOperations(() -> this.baselineMaxOperations);
    }

    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == Upgrade.SPEED) {
            this.baselineMaxOperations = (int)Math.pow(2.0D, this.upgradeComponent.getUpgrades(Upgrade.SPEED));
        }

    }

    public int getRedstoneLevel() {
        return MekanismUtils.redstoneLevelFromContents(this.inputTank.getStored(), this.inputTank.getCapacity());
    }

    protected boolean makesComparatorDirty(@Nullable SubstanceType type) {
        return type == SubstanceType.GAS;
    }

    @ComputerMethod(
            nameOverride = "getTransferLoss"
    )
    public double getLastTransferLoss() {
        return this.lastTransferLoss;
    }

    @ComputerMethod(
            nameOverride = "getEnvironmentalLoss"
    )
    public double getLastEnvironmentLoss() {
        return this.lastEnvironmentLoss;
    }

    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableDouble.create(this::getLastTransferLoss, (value) -> this.lastTransferLoss = value));
        container.track(SyncableDouble.create(this::getLastEnvironmentLoss, (value) -> this.lastEnvironmentLoss = value));
    }
}
