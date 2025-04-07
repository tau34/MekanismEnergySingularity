package io.github.tau34.mes.common.register;

import io.github.tau34.mes.common.tile.TileEntityZPMBlock;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;

public class MESContainerTypes {
    public static final ContainerTypeDeferredRegister REGISTER = new ContainerTypeDeferredRegister("mes");

    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityZPMBlock>> ZPM = REGISTER.register("zpm", TileEntityZPMBlock.class);
}
