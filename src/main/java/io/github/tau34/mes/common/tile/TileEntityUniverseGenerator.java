package io.github.tau34.mes.common.tile;

import com.jerry.mekanism_extras.common.registry.ExtraBlock;
import com.jerry.mekanism_extras.common.resource.ore.ExtraOreType;
import com.mojang.logging.LogUtils;
import io.github.tau34.mes.MESLang;
import io.github.tau34.mes.common.capability.UniverseGeneratorEnergyContainer;
import io.github.tau34.mes.common.register.MESBlocks;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.math.FloatingLong;
import mekanism.api.text.IHasTextComponent;
import mekanism.api.text.ILangEntry;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableBoolean;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.resource.ore.OreType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.interfaces.ISustainedData;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.util.NBTUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiPredicate;

public class TileEntityUniverseGenerator extends TileEntityConfigurableMachine implements ISustainedData {
    private static final int[] MIN = {3012, 271853, 230409, 54225, 9037, 143138, 76864, 243737, 140034, 1278, 132366, 267289, 132549};
    private static final int[] MAX = {3615, 326224, 276490, 65069, 10845, 171766, 92236, 292484, 168041, 1534, 158840, 320746, 159059};
    private static final double LOG1 = Math.log(400000D);
    private static final double LOG2 = LOG1 - Math.log(1500);
    private static final long MAX_PROGRESS = 120_888_000_000_000_000L;
    public static final Item[] ORE_ITEMS = {Items.ANCIENT_DEBRIS, Items.COAL_ORE, Items.COPPER_ORE, Items.DIAMOND_ORE, Items.EMERALD_ORE, getOre(OreType.FLUORITE), Items.GOLD_ORE, Items.IRON_ORE, getOre(OreType.LEAD), ExtraBlock.ORES.get(ExtraOreType.NAQUADAH).stone().asItem(), getOre(OreType.OSMIUM), getOre(OreType.TIN), getOre(OreType.URANIUM)};
    public static final String[] ORE_NAMES = {"ancient_debris", "coal", "copper", "diamond", "emerald", "fluorite", "gold", "iron", "lead", "naquadah", "osmium", "tin", "uranium"};

    private UniverseGeneratorEnergyContainer energyContainer;
    private OreDensity density = OreDensity.SPARSE;
    private RareOre rare = RareOre.SPARSE;
    private Radius radius = Radius.COMPACT;
    private Gravity gravity = Gravity.LOW;
    private CrustType crust = CrustType.STANDARD;
    private boolean isUniversePresent = false;
    private List<IInventorySlot> mainSlots;
    private int[] ores = new int[13];
    private int mining = -1;
    private long progress = -1;

    public TileEntityUniverseGenerator(BlockPos pos, BlockState state) {
        super(MESBlocks.UNIVERSE_GENERATOR, pos, state);
        this.configComponent = new TileComponentConfig(this, TransmissionType.ENERGY, TransmissionType.ITEM);
        this.configComponent.setupInputConfig(TransmissionType.ENERGY, this.energyContainer).setCanEject(false);
        ConfigInfo config = this.configComponent.getConfig(TransmissionType.ITEM);
        if (config != null) {
            config.addSlotInfo(DataType.OUTPUT, TileComponentConfig.createInfo(TransmissionType.ITEM, false, true, mainSlots));
            config.setDataType(DataType.OUTPUT);
            config.setEjecting(true);
        }
        this.ejectorComponent = new TileComponentEjector(this);
        this.ejectorComponent.setOutputData(this.configComponent, TransmissionType.ITEM);
    }

    private static Item getOre(OreType type) {
        return MekanismBlocks.ORES.get(type).stone().asItem();
    }

    @Override
    protected @Nullable IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSide(this::getDirection);
        builder.addContainer(energyContainer = UniverseGeneratorEnergyContainer.input(this, listener));
        return builder.build();
    }

    protected @NotNull IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        this.mainSlots = new ArrayList<>();
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        BiPredicate<ItemStack, AutomationType> canInsert = (stack, automationType) -> automationType != AutomationType.EXTERNAL;

        for(int slotY = 0; slotY < 3; ++slotY) {
            for(int slotX = 0; slotX < 9; ++slotX) {
                BasicInventorySlot slot = BasicInventorySlot.at(BasicInventorySlot.alwaysTrueBi, canInsert, listener, 8 + slotX * 18, 18 + slotY * 18);
                builder.addSlot(slot);
                this.mainSlots.add(slot);
            }
        }

        return builder.build();
    }

    public int getDensityOrdinal() {
        return density.ordinal();
    }

    public void setDensity(int i) {
        OreDensity[] values = OreDensity.values();
        density = values[Mth.clamp(i, 0, values.length - 1)];
        this.markForSave();
        this.recalculateEnergy();
    }

    public int getRareOrdinal() {
        return rare.ordinal();
    }

    public void setRare(int i) {
        RareOre[] values = RareOre.values();
        rare = values[Mth.clamp(i, 0, values.length - 1)];
        this.markForSave();
        this.recalculateEnergy();
    }

    public int getRadiusOrdinal() {
        return radius.ordinal();
    }

    public void setRadius(int i) {
        Radius[] values = Radius.values();
        radius = values[Mth.clamp(i, 0, values.length - 1)];
        this.markForSave();
        this.recalculateEnergy();
    }

    public int getGravityOrdinal() {
        return gravity.ordinal();
    }

    public void setGravity(int i) {
        Gravity[] values = Gravity.values();
        gravity = values[Mth.clamp(i, 0, values.length - 1)];
        this.markForSave();
        this.recalculateEnergy();
    }

    public int getCrustOrdinal() {
        return crust.ordinal();
    }

    public void setCrust(int i) {
        CrustType[] values =CrustType.values();
        crust = values[Mth.clamp(i, 0, values.length - 1)];
        this.markForSave();
        this.recalculateEnergy();
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(this::getDensityOrdinal, this::setDensity));
        container.track(SyncableInt.create(this::getRareOrdinal, this::setRare));
        container.track(SyncableInt.create(this::getRadiusOrdinal, this::setRadius));
        container.track(SyncableInt.create(this::getGravityOrdinal, this::setGravity));
        container.track(SyncableInt.create(this::getCrustOrdinal, this::setCrust));
        container.track(SyncableBoolean.create(this::isUniversePresent, b -> this.isUniversePresent = b));
        for (int i = 0; i < 13; i ++) {
            int finalI = i;
            container.track(SyncableInt.create(() -> ores[finalI], j -> this.ores[finalI] = j));
        }
        container.track(SyncableInt.create(() -> this.mining, this::setMining));
        container.track(SyncableLong.create(() -> this.progress, l -> this.progress = l));
    }

    private void recalculateEnergy() {
        this.energyContainer.setMaxEnergy(FloatingLong.create(2.5E14D)
                .multiply(density.getEnergyMult()).multiply(rare.getEnergyMult()).multiply(radius.getEnergyMult())
                .multiply(gravity.getEnergyMult()).multiply(crust.getEnergyMult()));
    }

    public UniverseGeneratorEnergyContainer getEnergyContainer() {
        return this.energyContainer;
    }

    @Override
    public void writeSustainedData(CompoundTag dataMap) {
        dataMap.putInt("density", getDensityOrdinal());
        dataMap.putInt("rare", getRareOrdinal());
        dataMap.putInt("radius", getRadiusOrdinal());
        dataMap.putInt("gravity", getGravityOrdinal());
        dataMap.putInt("crust", getCrustOrdinal());
        dataMap.putBoolean("isPresent", this.isUniversePresent);;
        for (int i = 0; i < 13; i ++) {
            dataMap.putInt(ORE_NAMES[i], this.ores[i]);
        }
        dataMap.putInt("mining", this.mining);
        dataMap.putLong("progress", this.progress);
    }

    @Override
    public void readSustainedData(CompoundTag dataMap) {
        NBTUtils.setIntIfPresent(dataMap, "density", this::setDensity);
        NBTUtils.setIntIfPresent(dataMap, "rare", this::setRare);
        NBTUtils.setIntIfPresent(dataMap, "radius", this::setRadius);
        NBTUtils.setIntIfPresent(dataMap, "gravity", this::setGravity);
        NBTUtils.setIntIfPresent(dataMap, "crust", this::setCrust);
        NBTUtils.setBooleanIfPresent(dataMap, "isPresent", b -> this.isUniversePresent = b);
        for (int i = 0; i < 13; i ++) {
            int finalI = i;
            NBTUtils.setIntIfPresent(dataMap, ORE_NAMES[i], j -> this.ores[finalI] = j);
        }
        NBTUtils.setIntIfPresent(dataMap, "mining", this::setMining);
        NBTUtils.setLongIfPresent(dataMap, "progress", l -> this.progress = l);
    }

    @Override
    public Map<String, String> getTileDataRemap() {
        Map<String, String> remap = new Object2ObjectOpenHashMap<>();
        remap.put("density", "density");
        remap.put("rare", "rare");
        remap.put("radius", "radius");
        remap.put("gravity", "gravity");
        remap.put("crust", "crust");
        remap.put("isPresent", "isPresent");
        for (int i = 0; i < 13; i++) {
            remap.put(ORE_NAMES[i], ORE_NAMES[i]);
        }
        remap.put("mining", "mining");
        remap.put("progress", "progress");
        return remap;
    }

    public boolean isReady() {
        return this.energyContainer.getEnergy().equals(this.energyContainer.getMaxEnergy());
    }

    public boolean isUniversePresent() {
        return this.isUniversePresent;
    }

    public boolean isUninitialized() {
        return this.progress == -1;
    }

    public void generate() {
        this.energyContainer.setEmpty();
        this.progress = MAX_PROGRESS;
    }

    private void genOre() {
        LogUtils.getLogger().info("generating ore");
        for (int i = 0; i < 13; i ++) {
            double d = Math.random();
            double o = MIN[i] * d + MAX[i] * (1 - d);
            o *= switch (density) {
                case SPARSE -> 1D;
                case NORMAL -> 2D;
                case RICH -> 5D;
                case ULTRA_RICH -> 8D;
            };
            o *= switch (rare) {
                case SPARSE -> 0D;
                case NORMAL -> 0.5D;
                case RICH -> 1.3D;
                case ABUNDANT -> 2.4D;
            } * (LOG1 - Math.log(MAX[i])) / LOG2 + 1D;
            o *= switch (radius) {
                case COMPACT -> 1.0D;
                case MEDIUM -> 2.0D;
                case WIDE -> 3.5D;
                case VAST -> 6.0D;
            };
            o *= switch (crust) {
                case STANDARD -> 1.0D;
                case MAGMATIC -> i == 0 || i == 6 || i == 12 ? 2.0D : 1.0D;
                case RADIOGENIC -> i == 8 || i == 10 || i == 12 ? 3.0D : 1.0D;
                case CRYSTALLINE -> i == 3 || i == 4 || i == 5 ? 3.0D : 1.0D;
            };
            this.ores[i] = (int) o;
        }
        this.isUniversePresent = true;
    }

    public void disconnect() {
        this.isUniversePresent = false;
        this.ores = new int[14];
        this.mining = -1;
        this.progress = -1;
    }

    public int getOre(int index) {
        return ores[index];
    }

    public boolean canStartMining(int index) {
        return this.mining != index && ores[index] != 0;
    }

    public void setMining(int index) {
        this.mining = index;
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        if (progress > 0) {
            progress -= 10_000_000_000_000_000L;
            if (progress < 0) {
                progress = 0;
                genOre();
            }
        }
        if (mining != -1) {
            if (ores[mining] > 0) {
                int toMine = Math.min(ores[mining], switch (gravity) {
                    case LOW -> 64;
                    case NORMAL -> 96;
                    case HIGH -> 128;
                    case EXTREME -> 160;
                });
                int overflow = toMine;
                for (IInventorySlot slot : mainSlots) {
                    int toInsert = Math.min(overflow, 64);
                    ItemStack stack = slot.insertItem(new ItemStack(ORE_ITEMS[mining], toInsert), Action.EXECUTE, AutomationType.INTERNAL);
                    if (stack.isEmpty()) {
                        if (overflow <= 64) {
                            overflow = 0;
                            break;
                        } else {
                            overflow -= 64;
                        }
                    } else {
                        overflow -= toInsert - stack.getCount();
                    }
                }
                ores[mining] -= toMine - overflow;
            }
        }
    }

    public static interface IUniverseConfig extends IHasTextComponent {
        float getEnergyMult();
    }

    public static enum OreDensity implements IUniverseConfig {
        SPARSE(1.0F, MESLang.UNIVERSE_DENSITY_SPARSE),
        NORMAL(2.0F, MESLang.UNIVERSE_DENSITY_NORMAL),
        RICH(4.0F, MESLang.UNIVERSE_DENSITY_RICH),
        ULTRA_RICH(6.0F, MESLang.UNIVERSE_DENSITY_ULTRA_RICH);

        private final float energyMult;
        private final ILangEntry lang;

        OreDensity(float energyMult, ILangEntry lang) {
            this.energyMult = energyMult;
            this.lang = lang;
        }

        @Override
        public float getEnergyMult() {
            return energyMult;
        }

        @Override
        public @NotNull Component getTextComponent() {
            return lang.translate();
        }
    }

    public static enum RareOre implements IUniverseConfig {
        SPARSE(1.0F, MESLang.UNIVERSE_RARE_SPARSE),
        NORMAL(1.5F, MESLang.UNIVERSE_RARE_NORMAL),
        RICH(2.5F, MESLang.UNIVERSE_RARE_RICH),
        ABUNDANT(4.0F, MESLang.UNIVERSE_RARE_ABUNDANT);

        private final float energyMult;
        private final ILangEntry lang;

        RareOre(float energyMult, ILangEntry lang) {
            this.energyMult = energyMult;
            this.lang = lang;
        }

        @Override
        public float getEnergyMult() {
            return energyMult;
        }

        @Override
        public @NotNull Component getTextComponent() {
            return lang.translate();
        }
    }

    public static enum Radius implements IUniverseConfig {
        COMPACT(1.0F, MESLang.UNIVERSE_RADIUS_COMPACT),
        MEDIUM(1.8F, MESLang.UNIVERSE_RADIUS_MEDIUM),
        WIDE(3.0F, MESLang.UNIVERSE_RADIUS_WIDE),
        VAST(5.0F, MESLang.UNIVERSE_RADIUS_VAST);

        private final float energyMult;
        private final ILangEntry lang;

        Radius(float energyMult, ILangEntry lang) {
            this.energyMult = energyMult;
            this.lang = lang;
        }

        @Override
        public float getEnergyMult() {
            return energyMult;
        }

        @Override
        public @NotNull Component getTextComponent() {
            return lang.translate();
        }
    }

    public static enum Gravity implements IUniverseConfig {
        LOW(1.0F, MESLang.UNIVERSE_GRAVITY_LOW),
        NORMAL(1.2F, MESLang.UNIVERSE_GRAVITY_NORMAL),
        HIGH(1.5F, MESLang.UNIVERSE_GRAVITY_HIGH),
        EXTREME(2.0F, MESLang.UNIVERSE_GRAVITY_EXTREME);

        private final float energyMult;
        private final ILangEntry lang;

        Gravity(float energyMult, ILangEntry lang) {
            this.energyMult = energyMult;
            this.lang = lang;
        }

        @Override
        public float getEnergyMult() {
            return energyMult;
        }

        @Override
        public @NotNull Component getTextComponent() {
            return lang.translate();
        }
    }

    public static enum CrustType implements IUniverseConfig {
        STANDARD(1.0F, MESLang.UNIVERSE_CRUST_STANDARD),
        MAGMATIC(1.5F, MESLang.UNIVERSE_CRUST_MAGMATIC),
        RADIOGENIC(2.0F, MESLang.UNIVERSE_CRUST_RADIOGENIC),
        CRYSTALLINE(2.0F, MESLang.UNIVERSE_CRUST_CRYSTALLINE);

        private final float energyMult;
        private final ILangEntry lang;

        CrustType(float energyMult, ILangEntry lang) {
            this.energyMult = energyMult;
            this.lang = lang;
        }

        @Override
        public float getEnergyMult() {
            return energyMult;
        }

        @Override
        public @NotNull Component getTextComponent() {
            return lang.translate();
        }
    }
}
