package io.github.tau34.mes.common.register;

import io.github.tau34.mes.MESLang;
import io.github.tau34.mes.common.attribute.AttributeStateAdvancedFusionPortMode;
import io.github.tau34.mes.common.blocktype.MESMachine;
import io.github.tau34.mes.common.tile.TileEntityNeutronCondenser;
import io.github.tau34.mes.common.tile.TileEntityPlasmaCooler;
import io.github.tau34.mes.common.tile.TileEntityUniverseGenerator;
import io.github.tau34.mes.common.tile.fusion.TileEntityAdvancedFusionBlock;
import io.github.tau34.mes.common.tile.fusion.TileEntityAdvancedFusionPort;
import io.github.tau34.mes.common.tile.zpm.*;
import mekanism.api.Upgrade;
import mekanism.api.math.FloatingLong;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.block.attribute.Attributes;
import mekanism.common.content.blocktype.BlockTypeTile;

import java.util.Set;

public class MESBlockTypes {
    public static final BlockTypeTile<TileEntityZPMBlock> ZPM_FRAME = BlockTypeTile.BlockTileBuilder.createBlock(() -> MESTiles.ZPM_FRAME, MESLang.DESCRIPTION_ZPM_FRAME).withGui(() -> MESContainerTypes.ZPM).externalMultiblock().build();
    public static final BlockTypeTile<TileEntityZPMPort> ZPM_PORT = BlockTypeTile.BlockTileBuilder.createBlock(() -> MESTiles.ZPM_PORT, MESLang.DESCRIPTION_ZPM_PORT).with(Attributes.ACTIVE).externalMultiblock().build();
    public static final BlockTypeTile<TileEntityVoidResonanceEngine> VOID_RESONANCE_ENGINE = BlockTypeTile.BlockTileBuilder.createBlock(() -> MESTiles.VOID_RESONANCE_ENGINE, MESLang.DESCRIPTION_VOID_RESONANCE_ENGINE).externalMultiblock().build();
    public static final BlockTypeTile<TileEntityAirExtractor> AIR_EXTRACTOR = BlockTypeTile.BlockTileBuilder.createBlock(() -> MESTiles.AIR_EXTRACTOR, MESLang.DESCRIPTION_AIR_EXTRACTOR).with(Attributes.ACTIVE).build();
    public static final BlockTypeTile<TileEntityZPMLogicAdapter> ZPM_LOGIC_ADAPTER = BlockTypeTile.BlockTileBuilder.createBlock(() -> MESTiles.ZPM_LOGIC_ADAPTER, MESLang.DESCRIPTION_ZPM_LOGIC_ADAPTER).with(new Attributes.AttributeRedstoneEmitter<>(TileEntityZPMLogicAdapter::getRedstoneLevel)).with(Attributes.REDSTONE).withGui(() -> MESContainerTypes.ZPM_LOGIC_ADAPTER).externalMultiblock().build();
    public static final MESMachine<TileEntityNeutronCondenser> NEUTRON_CONDENSER = MESMachine.MESMachineBuilder.createMachine(() -> MESTiles.NEUTRON_CONDENSER, MESLang.DESCRIPTION_NEUTRON_CONDENSER).withGui(() -> MESContainerTypes.NEUTRON_CONDENSER).withEnergyConfig(() -> FloatingLong.createConst(25000L), () -> FloatingLong.createConst(10000000L)).withComputerSupport("neutron_activator").build();
    public static final MESMachine<TileEntityUniverseGenerator> UNIVERSE_GENERATOR = MESMachine.MESMachineBuilder.createMachine(() -> MESTiles.UNIVERSE_GENERATOR, MESLang.DESCRIPTION_UNIVERSE_GENERATOR).withGui(() -> MESContainerTypes.UNIVERSE_GENERATOR).without(AttributeUpgradeSupport.class).withEnergyConfig(() -> FloatingLong.createConst(2.5E14D)).withComputerSupport("universe_generator").build();
    public static final BlockTypeTile<TileEntityAdvancedFusionBlock> ADVANCED_FUSION_FRAME = BlockTypeTile.BlockTileBuilder.createBlock(() -> MESTiles.ADVANCED_FUSION_FRAME, MESLang.DESCRIPTION_ZPM_FRAME).withGui(() -> MESContainerTypes.ADVANCED_FUSION).externalMultiblock().build();
    public static final BlockTypeTile<TileEntityAdvancedFusionPort> ADVANCED_FUSION_PORT = BlockTypeTile.BlockTileBuilder.createBlock(() -> MESTiles.ADVANCED_FUSION_PORT, MESLang.DESCRIPTION_ZPM_PORT).with(new AttributeStateAdvancedFusionPortMode()).externalMultiblock().build();
    public static final MESMachine<TileEntityPlasmaCooler> PLASMA_COOLER = MESMachine.MESMachineBuilder.createMachine(() -> MESTiles.PLASMA_COOLER, MESLang.DESCRIPTION_NEUTRON_CONDENSER).withGui(() -> MESContainerTypes.PLASMA_COOLER).withSupportedUpgrades(Set.of(Upgrade.SPEED)).withComputerSupport("plasma_cooler").build();
}
