package io.github.tau34.mes.common.register;

import io.github.tau34.mes.common.blocktype.MESMachine;
import io.github.tau34.mes.common.tile.TileEntityNeutronCondenser;
import io.github.tau34.mes.common.tile.TileEntityPlasmaCooler;
import io.github.tau34.mes.common.tile.TileEntityUniverseGenerator;
import io.github.tau34.mes.common.tile.fusion.TileEntityAdvancedFusionBlock;
import io.github.tau34.mes.common.tile.fusion.TileEntityAdvancedFusionPort;
import io.github.tau34.mes.common.tile.zpm.*;
import mekanism.common.block.prefab.BlockBasicMultiblock;
import mekanism.common.block.prefab.BlockTile;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import mekanism.common.resource.BlockResourceInfo;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public class MESBlocks {
    public static final BlockDeferredRegister REGISTER = new BlockDeferredRegister("mes");

    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityZPMBlock>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityZPMBlock>>> ZPM_FRAME = REGISTER.register("zpm_frame", () -> new BlockBasicMultiblock<>(MESBlockTypes.ZPM_FRAME, properties -> properties.mapColor(MapColor.COLOR_BLACK)), ItemBlockTooltip::new);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityZPMPort>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityZPMPort>>> ZPM_PORT = REGISTER.register("zpm_port", () -> new BlockBasicMultiblock<>(MESBlockTypes.ZPM_PORT, properties -> properties.mapColor(MapColor.COLOR_BLACK)), ItemBlockTooltip::new);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityVoidResonanceEngine>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityVoidResonanceEngine>>> VOID_RESONANCE_ENGINE = REGISTER.register("void_resonance_engine", () -> new BlockBasicMultiblock<>(MESBlockTypes.VOID_RESONANCE_ENGINE, properties -> properties.mapColor(MapColor.COLOR_BLACK)), ItemBlockTooltip::new);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityAirExtractor>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityAirExtractor>>> AIR_EXTRACTOR = REGISTER.register("air_extractor", () -> new BlockBasicMultiblock<>(MESBlockTypes.AIR_EXTRACTOR, properties -> properties.mapColor(MapColor.COLOR_BLACK)), ItemBlockTooltip::new);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityZPMLogicAdapter>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityZPMLogicAdapter>>> ZPM_LOGIC_ADAPTER = REGISTER.register("zpm_logic_adapter", () -> new BlockBasicMultiblock<>(MESBlockTypes.ZPM_LOGIC_ADAPTER, properties -> properties.mapColor(MapColor.COLOR_BLACK)), ItemBlockTooltip::new);
    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityNeutronCondenser, MESMachine<TileEntityNeutronCondenser>>, ItemBlockMachine> NEUTRON_CONDENSER = REGISTER.register("neutron_condenser", () -> new BlockTile.BlockTileModel<>(MESBlockTypes.NEUTRON_CONDENSER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);
    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityUniverseGenerator, MESMachine<TileEntityUniverseGenerator>>, ItemBlockMachine> UNIVERSE_GENERATOR = REGISTER.register("universe_generator", () -> new BlockTile.BlockTileModel<>(MESBlockTypes.UNIVERSE_GENERATOR, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityAdvancedFusionBlock>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityAdvancedFusionBlock>>> ADVANCED_FUSION_FRAME = REGISTER.register("advanced_fusion_frame", () -> new BlockBasicMultiblock<>(MESBlockTypes.ADVANCED_FUSION_FRAME, properties -> properties.mapColor(MapColor.COLOR_BLACK)), ItemBlockTooltip::new);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityAdvancedFusionPort>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityAdvancedFusionPort>>> ADVANCED_FUSION_PORT = REGISTER.register("advanced_fusion_port", () -> new BlockBasicMultiblock<>(MESBlockTypes.ADVANCED_FUSION_PORT, properties -> properties.mapColor(MapColor.COLOR_BLACK)), ItemBlockTooltip::new);
    public static final BlockRegistryObject<BlockTile.BlockTileModel<TileEntityPlasmaCooler, MESMachine<TileEntityPlasmaCooler>>, ItemBlockMachine> PLASMA_COOLER = REGISTER.register("plasma_cooler", () -> new BlockTile.BlockTileModel<>(MESBlockTypes.PLASMA_COOLER, properties -> properties.mapColor(BlockResourceInfo.STEEL.getMapColor())), ItemBlockMachine::new);

    public static final BlockRegistryObject<Block, BlockItem> QUANTUM_CASING = REGISTER.register("quantum_casing", BlockBehaviour.Properties.of().strength(3.5F, 16.0F).requiresCorrectToolForDrops());
}
