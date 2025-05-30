package com.eclipticcosmos.cclc;

import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class ModBlockEntities {

    public static Supplier<BlockEntityType<BlockEntityCardReader>> CARD_READER;

    public static void init() {
        CARD_READER = ModRegistries.BLOCK_ENTITIES.register("card_reader", () -> BlockEntityType.Builder.of(BlockEntityCardReader::new, ModBlocks.CARD_READER.get()).build(null));

    }



}
