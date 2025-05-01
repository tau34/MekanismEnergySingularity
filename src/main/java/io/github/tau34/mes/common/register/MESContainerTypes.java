package io.github.tau34.mes.common.register;

import io.github.tau34.mes.common.container.UniverseConfigContainer;
import io.github.tau34.mes.common.container.UniverseGeneratorContainer;
import io.github.tau34.mes.common.container.UniverseGeneratorManagerContainer;
import io.github.tau34.mes.common.tile.TileEntityNeutronCondenser;
import io.github.tau34.mes.common.tile.TileEntityPlasmaCooler;
import io.github.tau34.mes.common.tile.TileEntityUniverseGenerator;
import io.github.tau34.mes.common.tile.fusion.TileEntityAdvancedFusionBlock;
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
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityUniverseGenerator>> UNIVERSE_GENERATOR = REGISTER.register("universe_generator", TileEntityUniverseGenerator.class, UniverseGeneratorContainer::new);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityUniverseGenerator>> UNIVERSE_CONFIG = REGISTER.register("universe_config", TileEntityUniverseGenerator.class, UniverseConfigContainer::new);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityUniverseGenerator>> UNIVERSE_GENERATOR_OUTPUT = REGISTER.register("universe_generator_output", TileEntityUniverseGenerator.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityUniverseGenerator>> UNIVERSE_GENERATOR_MANAGER = REGISTER.register("universe_generator_manager", TileEntityUniverseGenerator.class, UniverseGeneratorManagerContainer::new);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityAdvancedFusionBlock>> ADVANCED_FUSION = REGISTER.register("advanced_fusion", TileEntityAdvancedFusionBlock.class);
    public static final ContainerTypeRegistryObject<MekanismTileContainer<TileEntityPlasmaCooler>> PLASMA_COOLER = REGISTER.register("plasma_cooler", TileEntityPlasmaCooler.class);
}
