package io.github.tau34.mes.common.register;

import io.github.tau34.mes.common.tile.TileEntityNeutronCondenser;
import io.github.tau34.mes.common.tile.zpm.*;
import mekanism.api.math.FloatingLong;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.Attributes;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.Machine;
import mekanism.generators.common.GeneratorsLang;
import mekanism.generators.common.tile.fission.TileEntityFissionReactorLogicAdapter;
import net.minecraft.core.Direction;

public class MESBlockTypes {
    public static final BlockTypeTile<TileEntityZPMBlock> ZPM_FRAME = BlockTypeTile.BlockTileBuilder.createBlock(() -> MESTiles.ZPM_FRAME, GeneratorsLang.DESCRIPTION_FUSION_REACTOR_FRAME).withGui(() -> MESContainerTypes.ZPM).externalMultiblock().build();
    public static final BlockTypeTile<TileEntityZPMPort> ZPM_PORT = BlockTypeTile.BlockTileBuilder.createBlock(() -> MESTiles.ZPM_PORT, GeneratorsLang.DESCRIPTION_FUSION_REACTOR_PORT).with(Attributes.ACTIVE).externalMultiblock().build();
    public static final BlockTypeTile<TileEntityVoidResonanceEngine> VOID_RESONANCE_ENGINE = BlockTypeTile.BlockTileBuilder.createBlock(() -> MESTiles.VOID_RESONANCE_ENGINE, GeneratorsLang.DESCRIPTION_FUSION_REACTOR_FRAME).externalMultiblock().build();
    public static final BlockTypeTile<TileEntityAirExtractor> AIR_EXTRACTOR = BlockTypeTile.BlockTileBuilder.createBlock(() -> MESTiles.AIR_EXTRACTOR, GeneratorsLang.DESCRIPTION_FUSION_REACTOR_FRAME).with(Attributes.ACTIVE).build();
    public static final BlockTypeTile<TileEntityZPMLogicAdapter> ZPM_LOGIC_ADAPTER = BlockTypeTile.BlockTileBuilder.createBlock(() -> MESTiles.ZPM_LOGIC_ADAPTER, GeneratorsLang.DESCRIPTION_FUSION_REACTOR_LOGIC_ADAPTER).with(new Attributes.AttributeRedstoneEmitter<>(TileEntityZPMLogicAdapter::getRedstoneLevel)).with(Attributes.REDSTONE).withGui(() -> MESContainerTypes.ZPM_LOGIC_ADAPTER).externalMultiblock().build();
    public static final Machine<TileEntityNeutronCondenser> NEUTRON_CONDENSER = Machine.MachineBuilder.createMachine(() -> MESTiles.NEUTRON_CONDENSER, MekanismLang.DESCRIPTION_SOLAR_NEUTRON_ACTIVATOR).withGui(() -> MESContainerTypes.NEUTRON_CONDENSER).withEnergyConfig(() -> FloatingLong.createConst(25000L), () -> FloatingLong.createConst(10000000L)).withComputerSupport("neutron_activator").build();
}
