package io.github.tau34.mes.datagen;

import io.github.tau34.mes.common.register.MESBlocks;
import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import mekanism.api.NBTConstants;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.block.BlockCardboardBox;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.block.attribute.Attributes.AttributeInventory;
import mekanism.common.block.attribute.Attributes.AttributeRedstone;
import mekanism.common.block.attribute.Attributes.AttributeSecurity;
import mekanism.common.block.interfaces.IHasTileEntity;
import mekanism.common.lib.frequency.IFrequencyHandler;
import mekanism.common.resource.ore.OreBlockType;
import mekanism.common.tile.base.SubstanceType;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.ISideConfiguration;
import mekanism.common.tile.interfaces.ISustainedData;
import mekanism.common.util.EnumUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction.MergeStrategy;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MESBlockLootTables extends BlockLootSubProvider {
    private static final LootItemCondition.Builder HAS_SILK_TOUCH = MatchTool.toolMatches(ItemPredicate.Builder.item()
            .hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1))));

    private final Set<Block> knownBlocks = new ReferenceOpenHashSet<>();
    private final Set<Block> toSkip = new ReferenceArraySet<>();

    protected MESBlockLootTables() {
        super(Collections.emptySet(), FeatureFlags.VANILLA_SET);
    }

    @Override
    protected void add(@NotNull Block block, @NotNull LootTable.Builder table) {
        super.add(block, table);
        knownBlocks.add(block);
    }

    @NotNull
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return knownBlocks;
    }

    protected void skip(IBlockProvider... blockProviders) {
        for (IBlockProvider blockProvider : blockProviders) {
            toSkip.add(blockProvider.getBlock());
        }
    }

    protected boolean skipBlock(Block block) {
        return knownBlocks.contains(block) || toSkip.contains(block);
    }

    protected LootTable.Builder createOreDrop(Block block, ItemLike item) {
        return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(item.asItem())
                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
        ));
    }

    protected LootTable.Builder droppingWithFortuneOrRandomly(Block block, ItemLike item, UniformGenerator range) {
        return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(item.asItem())
                .apply(SetItemCountFunction.setCount(range))
                .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
        ));
    }

    protected void dropSelf(List<IBlockProvider> blockProviders) {
        for (IBlockProvider blockProvider : blockProviders) {
            Block block = blockProvider.getBlock();
            if (!skipBlock(block)) {
                dropSelf(block);
            }
        }
    }

    protected void add(Function<Block, Builder> factory, Collection<? extends IBlockProvider> blockProviders) {
        for (IBlockProvider blockProvider : blockProviders) {
            add(blockProvider.getBlock(), factory);
        }
    }

    protected void add(Function<Block, Builder> factory, IBlockProvider... blockProviders) {
        for (IBlockProvider blockProvider : blockProviders) {
            add(blockProvider.getBlock(), factory);
        }
    }

    protected void add(Function<Block, Builder> factory, OreBlockType... oreTypes) {
        for (OreBlockType oreType : oreTypes) {
            add(oreType.stoneBlock(), factory);
            add(oreType.deepslateBlock(), factory);
        }
    }

    protected void dropSelfWithContents(List<IBlockProvider> blockProviders) {
        for (IBlockProvider blockProvider : blockProviders) {
            Block block = blockProvider.getBlock();
            if (skipBlock(block)) {
                continue;
            }
            CopyNbtFunction.Builder nbtBuilder = CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY);
            boolean hasContents = false;
            LootItem.Builder<?> itemLootPool = LootItem.lootTableItem(block);
            DelayedLootItemBuilder delayedPool = new DelayedLootItemBuilder();
            @Nullable
            BlockEntity tile = null;
            boolean flag = false;
            if (block instanceof IHasTileEntity<?> hasTileEntity) {
                tile = hasTileEntity.createDummyBlockEntity();
            }
            if (tile instanceof IFrequencyHandler frequencyHandler && frequencyHandler.getFrequencyComponent().hasCustomFrequencies()) {
                flag = true;
                nbtBuilder.copy(NBTConstants.COMPONENT_FREQUENCY, NBTConstants.MEK_DATA + "." + NBTConstants.COMPONENT_FREQUENCY);
            }
            if (Attribute.has(block, AttributeSecurity.class)) {
                flag = true;
                nbtBuilder.copy(NBTConstants.COMPONENT_SECURITY + "." + NBTConstants.OWNER_UUID, NBTConstants.MEK_DATA + "." + NBTConstants.OWNER_UUID);
                nbtBuilder.copy(NBTConstants.COMPONENT_SECURITY + "." + NBTConstants.SECURITY_MODE, NBTConstants.MEK_DATA + "." + NBTConstants.SECURITY_MODE);
            }
            if (Attribute.has(block, AttributeUpgradeSupport.class)) {
                flag = true;
                nbtBuilder.copy(NBTConstants.COMPONENT_UPGRADE, NBTConstants.MEK_DATA + "." + NBTConstants.COMPONENT_UPGRADE);
            }
            if (tile instanceof ISideConfiguration) {
                flag = true;
                nbtBuilder.copy(NBTConstants.COMPONENT_CONFIG, NBTConstants.MEK_DATA + "." + NBTConstants.COMPONENT_CONFIG);
                nbtBuilder.copy(NBTConstants.COMPONENT_EJECTOR, NBTConstants.MEK_DATA + "." + NBTConstants.COMPONENT_EJECTOR);
            }
            if (tile instanceof ISustainedData sustainedData) {
                Set<Entry<String, String>> remapEntries = sustainedData.getTileDataRemap().entrySet();
                for (Entry<String, String> remapEntry : remapEntries) {
                    flag = true;
                    nbtBuilder.copy(remapEntry.getKey(), NBTConstants.MEK_DATA + "." + remapEntry.getValue());
                }
            }
            if (Attribute.has(block, AttributeRedstone.class)) {
                flag = true;
                nbtBuilder.copy(NBTConstants.CONTROL_TYPE, NBTConstants.MEK_DATA + "." + NBTConstants.CONTROL_TYPE);
            }
            if (tile instanceof TileEntityMekanism tileEntity) {
                if (tileEntity.isNameable()) {
                    itemLootPool.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY));
                }
                for (SubstanceType type : EnumUtils.SUBSTANCES) {
                    if (tileEntity.handles(type) && !type.getContainers(tileEntity).isEmpty()) {
                        flag = true;
                        nbtBuilder.copy(type.getContainerTag(), NBTConstants.MEK_DATA + "." + type.getContainerTag());
                        if (type != SubstanceType.ENERGY && type != SubstanceType.HEAT) {
                            hasContents = true;
                        }
                    }
                }
            }
            @SuppressWarnings("unchecked")
            AttributeInventory<DelayedLootItemBuilder> attributeInventory = Attribute.get(block, AttributeInventory.class);
            if (attributeInventory != null) {
                if (attributeInventory.hasCustomLoot()) {
                    hasContents = attributeInventory.applyLoot(delayedPool, nbtBuilder);
                }
                else if (!(tile instanceof IItemHandler handler) || handler.getSlots() > 0) {
                    if (!(tile instanceof TileEntityMekanism tileMek) || tileMek.persistInventory()) {
                        flag = true;
                        nbtBuilder.copy(NBTConstants.ITEMS, NBTConstants.MEK_DATA + "." + NBTConstants.ITEMS);
                        hasContents = true;
                    }
                }
            }
            if (block instanceof BlockCardboardBox) {
                flag = true;
                nbtBuilder.copy(NBTConstants.DATA, NBTConstants.MEK_DATA + "." + NBTConstants.DATA);
            }
            if (flag) {
                itemLootPool.apply(nbtBuilder);
            }
            for (LootItemFunction.Builder function : delayedPool.functions) {
                itemLootPool.apply(function);
            }
            for (LootItemCondition.Builder condition : delayedPool.conditions) {
                itemLootPool.when(condition);
            }
            add(block, LootTable.lootTable().withPool(applyExplosionCondition(hasContents, LootPool.lootPool()
                    .name("main")
                    .setRolls(ConstantValue.exactly(1))
                    .add(itemLootPool)
            )));
        }
    }

    private static <T extends ConditionUserBuilder<T>> T applyExplosionCondition(boolean explosionResistant, ConditionUserBuilder<T> condition) {
        return explosionResistant ? condition.unwrap() : condition.when(ExplosionCondition.survivesExplosion());
    }

    @NotNull
    @Override
    protected LootTable.Builder createSlabItemTable(@NotNull Block slab) {
        return LootTable.lootTable().withPool(LootPool.lootPool()
                .name("main")
                .setRolls(ConstantValue.exactly(1))
                .add(applyExplosionDecay(slab, LootItem.lootTableItem(slab)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(slab)
                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(SlabBlock.TYPE, SlabType.DOUBLE)))
                                )
                        )
                )
        );
    }

    @Override
    public void dropOther(@NotNull Block block, @NotNull ItemLike drop) {
        add(block, createSingleItemTable(drop));
    }

    @NotNull
    @Override
    public LootTable.Builder createSingleItemTable(@NotNull ItemLike item) {
        return LootTable.lootTable().withPool(applyExplosionCondition(item, LootPool.lootPool()
                .name("main")
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(item))
        ));
    }

    @NotNull
    @Override
    protected LootTable.Builder createSingleItemTableWithSilkTouch(@NotNull Block block, @NotNull ItemLike item, @NotNull NumberProvider range) {
        return createSilkTouchDispatchTable(block, applyExplosionDecay(block, LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(range))));
    }

    @NotNull
    protected static LootTable.Builder createSilkTouchDispatchTable(@NotNull Block block, @NotNull LootPoolEntryContainer.Builder<?> builder) {
        return createSelfDropDispatchTable(block, HAS_SILK_TOUCH, builder);
    }

    @NotNull
    protected static LootTable.Builder createSelfDropDispatchTable(@NotNull Block block, @NotNull LootItemCondition.Builder conditionBuilder,
                                                                   @NotNull LootPoolEntryContainer.Builder<?> entry) {
        return LootTable.lootTable().withPool(LootPool.lootPool()
                .name("main")
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(block)
                        .when(conditionBuilder)
                        .otherwise(entry)
                )
        );
    }

    @Override
    protected void generate() {
        dropSelfWithContents(MESBlocks.REGISTER.getAllBlocks());
    }

    @NothingNullByDefault
    public static class DelayedLootItemBuilder implements ConditionUserBuilder<DelayedLootItemBuilder>, FunctionUserBuilder<DelayedLootItemBuilder> {
        private final List<LootItemFunction.Builder> functions = new ArrayList<>();
        private final List<LootItemCondition.Builder> conditions = new ArrayList<>();

        @Override
        public DelayedLootItemBuilder apply(LootItemFunction.Builder pFunctionBuilder) {
            functions.add(pFunctionBuilder);
            return this;
        }

        @Override
        public DelayedLootItemBuilder when(LootItemCondition.Builder pConditionBuilder) {
            conditions.add(pConditionBuilder);
            return this;
        }

        @Override
        public DelayedLootItemBuilder unwrap() {
            return this;
        }
    }
}