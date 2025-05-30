package com.eclipticcosmos.cclc;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import javax.annotation.Nonnull;

@EventBusSubscriber(modid = CCLC.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
    @SubscribeEvent
    public static void registerScreens(@Nonnull RegisterMenuScreensEvent event)
    {
        event.register(ModMenus.CARD_READER.get(), CardReaderScreen::new);
    }
}
