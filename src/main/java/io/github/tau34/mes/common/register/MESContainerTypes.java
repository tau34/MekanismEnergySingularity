package io.github.tau34.mes.common.register;

import io.github.tau34.mes.common.tile.TileEntityNeutronCondenser;
import io.github.tau34.mes.common.tile.zpm.TileEntityZPMBlock;
import io.github.tau34.mes.common.tile.zpm.TileEntityZPMLogicAdapter;
import mekanism.common.inventory.container.tile.EmptyTileContainer;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;

public class MESContainerTypes {
    public static final ContainerTypeDeferredRegister REGISTER = new ContainerTypeDeferredRegister("mes");

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityZPMBlock>> ZPM = REGISTER.register("zpm", TileEntityZPMBlock.class);
    public static final ContainerTypeRegistryObject<EmptyTileContainer<TileEntityZPMLogicAdapter>> ZPM_LOGIC_ADAPTER = REGISTER.registerEmpty("zpm_logic_adapter", TileEntityZPMLogicAdapter.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityNeutronCondenser>> NEUTRON_CONDENSER = REGISTER.register("neutron_condenser", TileEntityNeutronCondenser.class);
}
