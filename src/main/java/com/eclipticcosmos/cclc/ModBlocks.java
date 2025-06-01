package com.eclipticcosmos.cclc;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static void init() {}

    private static <T extends Block>ResourceLocation idGetter(T block) { return BuiltInRegistries.BLOCK.getKey(block); }

    private static Function<Block, Item> getDefaultGenerator() {return block -> new BlockItem(block, new Item.Properties());}

    public static final Supplier<Block> CARD_READER;

    static {
        CARD_READER = register("cardreader",
                block -> new CardReaderBlockItem(block, new Item.Properties()),
                () -> new CardReader(
                BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).strength(3.0f).requiresCorrectToolForDrops().sound(SoundType.METAL)
        ));
    }

    private static <T extends Block> Supplier<T> register(String name, Supplier<T> sup)
    {
        return register(name, getDefaultGenerator(), sup);
    }
    private static <T extends Block> Supplier<T> register(String name, Function<Block, Item> itemGenerator, Supplier<T> sup)
    {
        Supplier<T> block = ModRegistries.BLOCKS.register(name, sup);
        if (block!= null)
            ModRegistries.ITEMS.register(name, () -> itemGenerator.apply(block.get()));
        return block;
    }
}
