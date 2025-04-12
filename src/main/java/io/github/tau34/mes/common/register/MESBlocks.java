package io.github.tau34.mes.common.register;

import io.github.tau34.mes.common.tile.*;
import mekanism.common.block.prefab.BlockBasicMultiblock;
import mekanism.common.item.block.ItemBlockTooltip;
import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.BlockRegistryObject;
import net.minecraft.world.level.material.MapColor;

public class MESBlocks {
    public static final BlockDeferredRegister REGISTER = new BlockDeferredRegister("mes");

    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityZPMBlock>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityZPMBlock>>> ZPM_FRAME = REGISTER.register("zpm_frame", () -> new BlockBasicMultiblock<>(MESBlockTypes.ZPM_FRAME, properties -> properties.mapColor(MapColor.COLOR_BLACK)), ItemBlockTooltip::new);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityZPMPort>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityZPMPort>>> ZPM_PORT = REGISTER.register("zpm_port", () -> new BlockBasicMultiblock<>(MESBlockTypes.ZPM_PORT, properties -> properties.mapColor(MapColor.COLOR_BLACK)), ItemBlockTooltip::new);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityVoidResonanceEngine>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityVoidResonanceEngine>>> VOID_RESONANCE_ENGINE = REGISTER.register("void_resonance_engine", () -> new BlockBasicMultiblock<>(MESBlockTypes.VOID_RESONANCE_ENGINE, properties -> properties.mapColor(MapColor.COLOR_BLACK)), ItemBlockTooltip::new);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityAirExtractor>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityAirExtractor>>> AIR_EXTRACTOR = REGISTER.register("air_extractor", () -> new BlockBasicMultiblock<>(MESBlockTypes.AIR_EXTRACTOR, properties -> properties.mapColor(MapColor.COLOR_BLACK)), ItemBlockTooltip::new);
    public static final BlockRegistryObject<BlockBasicMultiblock<TileEntityZPMLogicAdapter>, ItemBlockTooltip<BlockBasicMultiblock<TileEntityZPMLogicAdapter>>> ZPM_LOGIC_ADAPTER = REGISTER.register("zpm_logic_adapter", () -> new BlockBasicMultiblock<>(MESBlockTypes.ZPM_LOGIC_ADAPTER, properties -> properties.mapColor(MapColor.COLOR_BLACK)), ItemBlockTooltip::new);
}
