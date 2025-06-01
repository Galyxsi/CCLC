package com.eclipticcosmos.cclc;

import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRegistries {

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
        ModBlocks.init();

        ITEMS.register(bus);
        //ModItems.init();

        MENUS.register(bus);
        ModMenus.register(bus);

        BLOCK_ENTITIES.register(bus);
        ModBlockEntities.init();

        ComputerCraftAPI.registerGenericSource(new CardReaderPeripheral());
    }

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, CCLC.MODID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, CCLC.MODID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CCLC.MODID);


    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, CCLC.MODID);


}
