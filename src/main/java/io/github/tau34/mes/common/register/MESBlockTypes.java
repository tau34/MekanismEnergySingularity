package io.github.tau34.mes.common.register;

import io.github.tau34.mes.common.tile.TileEntityAirExtractor;
import io.github.tau34.mes.common.tile.TileEntityVoidResonanceEngine;
import io.github.tau34.mes.common.tile.TileEntityZPMBlock;
import io.github.tau34.mes.common.tile.TileEntityZPMPort;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.Attributes;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.generators.common.GeneratorsLang;

public class MESBlockTypes {
    public static final BlockTypeTile<TileEntityZPMBlock> ZPM_FRAME = BlockTypeTile.BlockTileBuilder.createBlock(() -> MESTiles.ZPM_FRAME, GeneratorsLang.DESCRIPTION_FUSION_REACTOR_FRAME).withGui(() -> MESContainerTypes.ZPM).externalMultiblock().build();
    public static final BlockTypeTile<TileEntityZPMPort> ZPM_PORT = BlockTypeTile.BlockTileBuilder.createBlock(() -> MESTiles.ZPM_PORT, GeneratorsLang.DESCRIPTION_FUSION_REACTOR_PORT).with(Attributes.ACTIVE).externalMultiblock().build();
    public static final BlockTypeTile<TileEntityVoidResonanceEngine> VOID_RESONANCE_ENGINE = BlockTypeTile.BlockTileBuilder.createBlock(() -> MESTiles.VOID_RESONANCE_ENGINE, GeneratorsLang.DESCRIPTION_FUSION_REACTOR_FRAME).externalMultiblock().build();
    public static final BlockTypeTile<TileEntityAirExtractor> AIR_EXTRACTOR = BlockTypeTile.BlockTileBuilder.createBlock(() -> MESTiles.AIR_EXTRACTOR, GeneratorsLang.DESCRIPTION_FUSION_REACTOR_FRAME).build();
}
