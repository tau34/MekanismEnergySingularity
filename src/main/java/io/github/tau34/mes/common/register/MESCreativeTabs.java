package io.github.tau34.mes.common.register;

import io.github.tau34.mes.MESLang;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.registration.impl.CreativeTabDeferredRegister;
import mekanism.common.registration.impl.CreativeTabRegistryObject;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismCreativeTabs;
import mekanism.common.tier.FactoryTier;

public class MESCreativeTabs {
    public static final CreativeTabDeferredRegister REGISTER = new CreativeTabDeferredRegister("mes");

    public static final CreativeTabRegistryObject MFE = REGISTER.registerMain(MESLang.MES, MekanismBlocks.getFactory(FactoryTier.ULTIMATE, FactoryType.SMELTING),
            builder -> builder.withTabsBefore(MekanismCreativeTabs.MEKANISM.key())
                    .displayItems((d, o) -> {
                        CreativeTabDeferredRegister.addToDisplay(MESBlocks.REGISTER, o);
                        CreativeTabDeferredRegister.addToDisplay(MESItems.REGISTER, o);
                    }));
}
