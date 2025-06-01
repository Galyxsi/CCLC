package com.eclipticcosmos.cclc;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(BuiltInRegistries.MENU, CCLC.MODID);

    public static Supplier<MenuType<CardReaderMenu>> CARD_READER = MENUS.register("card_reader", () ->
            new MenuType<>((IContainerFactory<CardReaderMenu>) (id, inventory, data) -> {
                var be = (BlockEntityCardReader) inventory.player.level().getBlockEntity(data.readBlockPos());
                return new CardReaderMenu(id, inventory, be);
            }, FeatureFlagSet.of())
    );;

    public static void init() {
        /*CARD_READER = MENUS.register("card_reader", () ->
                new MenuType<>((IContainerFactory<CardReaderMenu>) (id, inventory, data) -> {
                    var be = (BlockEntityCardReader) inventory.player.level().getBlockEntity(data.readBlockPos());
                    return new CardReaderMenu(id, inventory, be);
                }, FeatureFlagSet.of())
        );*/
    }

    private static <T extends AbstractContainerMenu> MenuType<T> CreateType(MenuType.MenuSupplier<T> supplier) {
        return new MenuType<>(supplier, FeatureFlagSet.of());
    }

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }
}
