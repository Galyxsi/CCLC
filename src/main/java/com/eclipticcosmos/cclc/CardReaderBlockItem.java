package com.eclipticcosmos.cclc;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.awt.*;

public class CardReaderBlockItem extends BlockItem {

    public CardReaderBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Component getName(ItemStack stack) {
        if (stack.has(DataComponents.CUSTOM_NAME)) {
            return stack.get(DataComponents.CUSTOM_NAME);
        }
        return Component.translatable(this.getDescriptionId(stack));
    }
}
