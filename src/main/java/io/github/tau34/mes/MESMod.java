package io.github.tau34.mes;

import com.mojang.logging.LogUtils;
import io.github.tau34.mes.client.gui.GuiZPM;
import io.github.tau34.mes.common.multiblock.cache.ZPMCache;
import io.github.tau34.mes.common.multiblock.data.ZPMMultiblockData;
import io.github.tau34.mes.common.multiblock.validator.ZPMValidator;
import io.github.tau34.mes.common.register.MESBlocks;
import io.github.tau34.mes.common.register.MESContainerTypes;
import io.github.tau34.mes.common.register.MESCreativeTabs;
import io.github.tau34.mes.common.register.MESTiles;
import mekanism.client.ClientRegistrationUtil;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.lib.multiblock.MultiblockCache;
import mekanism.common.lib.multiblock.MultiblockManager;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.tier.FactoryTier;
import mekanism.common.util.EnumUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.slf4j.Logger;

@Mod(MESMod.MOD_ID)
public class MESMod {
    public static final String MOD_ID = "mes";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static MultiblockManager<ZPMMultiblockData> zpmManager = new MultiblockManager<>("zpm", ZPMCache::new, ZPMValidator::new);

    public MESMod() {
        //MMCConfig.registerConfig(ModLoadingContext.get());
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MESBlocks.REGISTER.register(modEventBus);
        MESContainerTypes.REGISTER.register(modEventBus);
        MESCreativeTabs.REGISTER.register(modEventBus);
        MESTiles.REGISTER.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static ResourceLocation rl(String path){
        return new ResourceLocation(MESMod.MOD_ID, path);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }

        @SubscribeEvent(priority = EventPriority.LOW)
        public static void registerContainers(RegisterEvent event) {
            event.register(Registries.MENU, helper -> {
                ClientRegistrationUtil.registerScreen(MESContainerTypes.ZPM, GuiZPM::new);
            });
        }
    }
}
