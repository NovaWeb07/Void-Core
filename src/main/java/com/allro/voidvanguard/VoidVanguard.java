package com.allro.voidvanguard;

import com.allro.voidvanguard.network.ModMessages;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(VoidVanguard.MODID)
public class VoidVanguard {
    public static final String MODID = "voidvanguard";
    private static final Logger LOGGER = LogManager.getLogger();

    public VoidVanguard() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        com.allro.voidvanguard.registry.ItemRegistry.REGISTER.register(modEventBus);
        com.allro.voidvanguard.registry.ItemRegistry.TABS.register(modEventBus);

        modEventBus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ModMessages::register);
    }
}
