package io.github.tau34.mes.common.register;

import io.github.tau34.mes.common.tile.TileEntityNeutronCondenser;
import io.github.tau34.mes.common.tile.zpm.*;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;

public class MESTiles {
    public static final TileEntityTypeDeferredRegister REGISTER = new TileEntityTypeDeferredRegister("mes");

    public static final TileEntityTypeRegistryObject<TileEntityZPMBlock> ZPM_FRAME = REGISTER.register(MESBlocks.ZPM_FRAME, TileEntityZPMBlock::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    public static final TileEntityTypeRegistryObject<TileEntityZPMPort> ZPM_PORT = REGISTER.register(MESBlocks.ZPM_PORT, TileEntityZPMPort::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    public static final TileEntityTypeRegistryObject<TileEntityVoidResonanceEngine> VOID_RESONANCE_ENGINE = REGISTER.register(MESBlocks.VOID_RESONANCE_ENGINE, TileEntityVoidResonanceEngine::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    public static final TileEntityTypeRegistryObject<TileEntityAirExtractor> AIR_EXTRACTOR = REGISTER.register(MESBlocks.AIR_EXTRACTOR, TileEntityAirExtractor::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    public static final TileEntityTypeRegistryObject<TileEntityZPMLogicAdapter> ZPM_LOGIC_ADAPTER = REGISTER.register(MESBlocks.ZPM_LOGIC_ADAPTER, TileEntityZPMLogicAdapter::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
    public static final TileEntityTypeRegistryObject<TileEntityNeutronCondenser> NEUTRON_CONDENSER = REGISTER.register(MESBlocks.NEUTRON_CONDENSER, TileEntityNeutronCondenser::new, TileEntityMekanism::tickServer, TileEntityMekanism::tickClient);
}
