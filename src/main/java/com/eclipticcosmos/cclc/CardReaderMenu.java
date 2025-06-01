package com.eclipticcosmos.cclc;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.network.LazyPacketData;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.common.menus.LazyMessageMenu;
import io.github.lightman314.lightmanscurrency.common.menus.slots.InteractionSlot;
import io.github.lightman314.lightmanscurrency.common.menus.slots.easy.EasySlot;
import io.github.lightman314.lightmanscurrency.common.menus.validation.types.BlockEntityValidator;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class CardReaderMenu extends LazyMessageMenu {

    public final BlockEntityCardReader be;

    private final List<CoinCardSlot> coinSlots;
    private final List<EasySlot> inventorySlots;

    private final Map<Long,TradeContext> contextCache = new HashMap<>();
    private final Container coins;

    InteractionSlot interactionSlot;
    public InteractionSlot getInteractionSlot() { return this.interactionSlot; }

    public CardReaderMenu(int id, Inventory inventory, BlockEntityCardReader be) {
        super(ModMenus.CARD_READER.get(), id, inventory);
        this.be = be;
        this.coins = be.getStorage();

        this.addValidator(BlockEntityValidator.of(be));
        this.addValidator(this.be::allowAccess);

        List<CoinCardSlot> cSlots = new ArrayList<>();
        for(int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 9; x++)
            {
                CoinCardSlot s = new CoinCardSlot(this.coins, x + 9 * y, 8 + x * 18, 93 + y * 18, this.player);
                this.addSlot(s);
                cSlots.add(s);
            }
        }
        this.coinSlots = ImmutableList.copyOf(cSlots);

        List<EasySlot> iSlots = new ArrayList<>();
        for(int y = 0; y < 3; y++)
        {
            for (int x = 0; x < 9; x++)
            {
                EasySlot s = new EasySlot(inventory, x + y * 9 + 9, 8 + x * 18, 161 + y * 18);
                iSlots.add(s);
                this.addSlot(s);
            }
        }
        for(int x = 0; x < 9; x++)
        {
            EasySlot s = new EasySlot(inventory, x, 8 + x * 18, 219);
            iSlots.add(s);
            this.addSlot(s);
        }
        this.inventorySlots = ImmutableList.copyOf(iSlots);
    }

    public void SetCoinSlotVisibility(boolean visible) {EasySlot.SetActive(this.coinSlots, visible);}
    public void SetInventoryVisibility(boolean visible) {EasySlot.SetActive(this.inventorySlots, visible);}

    @Override
    public void HandleMessage(@NotNull LazyPacketData lazyPacketData) {
        this.extraHandler.accept(lazyPacketData);
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack clickedStack = ItemStack.EMPTY;

        Slot slot = this.slots.get(index);

        if(slot != null && slot.hasItem())
        {
            ItemStack slotStack = slot.getItem();
            clickedStack = slotStack.copy();

            if (index < BlockEntityCardReader.STORAGE_SIZE)
            {
                if(!this.moveItemStackTo(slotStack, BlockEntityCardReader.STORAGE_SIZE, this.slots.size(), true))
                {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 0, BlockEntityCardReader.STORAGE_SIZE, false)) {
                return ItemStack.EMPTY;
            }

            if(slotStack.isEmpty())
            {
                slot.set(ItemStack.EMPTY);
            }
            else {
                slot.setChanged();
            }
        }

        return clickedStack;
    }

    @Override
    public void removed(@Nonnull Player player) {super.removed(player); }

    public MoneyValue getStoredBalance() {
        return be.getStoredBalance();
    }

    private Consumer<LazyPacketData> extraHandler = d -> {};
    public final void AddExtraHandler(@Nonnull Consumer<LazyPacketData> extraHandler) {this.extraHandler = extraHandler; }
}
