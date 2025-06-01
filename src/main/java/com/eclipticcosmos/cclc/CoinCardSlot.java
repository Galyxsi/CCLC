package com.eclipticcosmos.cclc;

import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.common.core.ModItems;
import io.github.lightman314.lightmanscurrency.common.menus.slots.CoinSlot;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class CoinCardSlot extends CoinSlot {

    private final Player player;


    public CoinCardSlot(Container inventory, int index, int x, int y, Player player) {
        super(inventory, index, x, y);
        this.player = player;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {

        if(!MoneyAPI.API.ItemAllowedInMoneySlot(player, stack) /*&& !stack.is(LCTags.Items.WALLET)*/ /*stack.getTags().noneMatch(tag -> tag.equals(LCTags.Items.WALLET))*/)
        {
            return false;
        }

        if (stack.is(ModItems.ATM_CARD.get())) {
            for (int i = 0; i < this.container.getContainerSize(); i++) {
                ItemStack existing = this.container.getItem(i);
                if (!existing.isEmpty() && existing.is(ModItems.ATM_CARD.get())) {
                    return false;
                }
            }
        }

        return true;
    }
}