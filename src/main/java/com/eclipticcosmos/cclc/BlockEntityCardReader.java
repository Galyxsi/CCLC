package com.eclipticcosmos.cclc;

import io.github.lightman314.lightmanscurrency.api.misc.blockentity.EasyBlockEntity;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.coins.CoinAPI;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.traders.TraderAPI;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.common.core.ModItems;
import io.github.lightman314.lightmanscurrency.common.menus.containers.CoinContainer;
import io.github.lightman314.lightmanscurrency.common.util.IClientTracker;
import io.github.lightman314.lightmanscurrency.util.BlockEntityUtil;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import io.github.lightman314.lightmanscurrency.util.VersionUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class BlockEntityCardReader extends EasyBlockEntity implements IClientTracker/*, IVariantSupportingBlockEntity*/ {

    private CoinContainer storage;
    public final CoinContainer getStorage() { return this.storage; }

    private ResourceLocation currentVariant = null;

    public static final int STORAGE_SIZE = 9 * 3;

    private boolean allowEvents = true;
    private Component customName = null;
    public void setCustomName(Component name) { this.customName = name; this.markCustomNameDirty(); }
    public Component getDisplayName() { return this.customName != null ? this.customName : Component.translatable("block.cclc.cardreader"); }

    @Override
    public @Nullable ResourceLocation getCurrentVariant() {
        return this.currentVariant;
    }

    protected CompoundTag saveStorage(CompoundTag compoundTag, @Nonnull HolderLookup.Provider lookup) {
        InventoryUtil.saveAllItems("Storage", compoundTag, this.storage, lookup);
        return compoundTag;
    }

    protected CompoundTag saveCustomName(CompoundTag compoundTag, @Nonnull HolderLookup.Provider lookup) {
        if (this.customName != null)
            compoundTag.putString("Name", Component.Serializer.toJson(this.customName, lookup));
        return compoundTag;
    }

    public final void markCustomNameDirty()
    {
        this.setChanged();
        if(this.isServer())
            BlockEntityUtil.sendUpdatePacket(this, this.saveCustomName(new CompoundTag(), this.level.registryAccess()));
    }

    public final void markStorageDirty()
    {
        this.setChanged();
        if(this.isServer() && this.allowEvents)
        {
            CompoundTag tag = new CompoundTag();
            this.saveStorage(tag, this.level.registryAccess());
            BlockEntityUtil.sendUpdatePacket(this, tag);

        }
    }

    protected void saveAdditional(@Nonnull CompoundTag tag, @Nonnull HolderLookup.Provider lookup) {
        super.saveAdditional(tag, lookup);
        this.saveStorage(tag, lookup);
        this.saveCustomName(tag, lookup);
        if(this.currentVariant != null)
            tag.putString("Variant",this.currentVariant.toString());
        else
            tag.putBoolean("NoVariant",true);
    }

    @Override
    public void loadAdditional(@Nonnull CompoundTag tag, @Nonnull HolderLookup.Provider lookup) {
        this.storage = new CoinContainer(STORAGE_SIZE);
        CoinContainer loaded = new CoinContainer(STORAGE_SIZE);

        SimpleContainer temp = InventoryUtil.loadAllItems("Storage", tag, STORAGE_SIZE, lookup);
        for (int i = 0; i < STORAGE_SIZE; i++)
        {
            loaded.setItem(i, temp.getItem(i));
        }

        this.storage = loaded;
        this.storage.addListener(i -> this.markStorageDirty());

        if (tag.contains("Name")) {
            this.customName = Component.Serializer.fromJson(tag.getString("Name"), lookup);
        }

        if(tag.contains("Variant"))
            this.currentVariant = VersionUtil.parseResource(tag.getString("Variant"));
        else if(tag.contains("NoVariant"))
            this.currentVariant = null;
        super.loadAdditional(tag, lookup);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider lookup) {
        CompoundTag tag = new CompoundTag();
        this.saveWithoutMetadata(lookup);
        this.saveStorage(tag, lookup);
        this.saveCustomName(tag, lookup);
        return tag;
    }

    public MoneyValue getStoredBalance() {
        List<MoneyValue> moneyValues = MoneyAPI.API.GetContainersMoneyHandler(this.getStorage(), level.players().get(0)).getStoredMoney().allValues();
        if (moneyValues.isEmpty())
            return MoneyValue.empty();

        MoneyValue totalValue = MoneyValue.empty();
        for (int i = 0; i < moneyValues.size(); i++) {
            if (moneyValues.get(i).getUniqueName().contains("lightmanscurrency:coins!main"))
            {
                if (totalValue.isEmpty()) {
                    totalValue = moneyValues.get(i);
                } else {
                    totalValue.addValue(moneyValues.get(i));
                }

            }

            //moneyValues.get(i).getCurrency().getType().equals(ResourceLocation.tryParse());
        }
        return totalValue;
    }

    public void removeMoney(MoneyValue value) {
        MoneyAPI.API.GetContainersMoneyHandler(this.getStorage(), level.players().get(0)).extractMoney(value, false);
    }

    public boolean hasCoins() {
        for (int i = 0; i < this.getStorage().getContainerSize(); i++)
        {
            ItemStack itemStack = this.getStorage().getItem(i);
            if (itemStack.is((Item) ModItems.COIN_COPPER) || itemStack.is((Item) ModItems.COIN_IRON) || itemStack.is((Item) ModItems.COIN_GOLD) || itemStack.is((Item) ModItems.COIN_DIAMOND) || itemStack.is((Item) ModItems.COIN_EMERALD) || itemStack.is((Item) ModItems.COIN_NETHERITE))
            {
                return true;
            }
        }
        return false;
    }
    public boolean hasCard() {
        for (int i = 0; i < this.getStorage().getContainerSize(); i++)
        {

            ItemStack itemStack = this.getStorage().getItem(i);
            if (itemStack.is((Item) ModItems.ATM_CARD))
            {
                return true;
            }
        }
        return false;
    }

    public List<TradeData> getTrades() {
        List<TraderData> traders = TraderAPI.API.GetAllTraders(false);
        List<TradeData> allTrades = new ArrayList<>();
        for (TraderData trader : traders)
        {
            for (int i = 0; i < trader.getTradeCount(); i++)
            {
                allTrades.add(trader.getTrade(i));
            }
        }
        return allTrades;
    }

    public List<TraderData> getTraders() {
        return TraderAPI.API.GetAllTraders(false);
    }

    /*public List<MoneyValue> getCoinBalances() {

    }*/

    /*public MoneyValue getTotalCoinBalances() {
        List<MoneyValue> moneyValues = MoneyAPI.API.GetContainersMoneyHandler(this.getStorage(), level.players().get(0)).getStoredMoney().allValues();

        for (int i = 0; i < this.getStorage().getContainerSize(); i++)
        {
            ItemStack itemStack = this.getStorage().getItem(i);
            if (itemStack.is((Item) ModItems.COIN_COPPER) || itemStack.is((Item) ModItems.COIN_IRON) || itemStack.is((Item) ModItems.COIN_GOLD) || itemStack.is((Item) ModItems.COIN_DIAMOND) || itemStack.is((Item) ModItems.COIN_EMERALD) || itemStack.is((Item) ModItems.COIN_NETHERITE))
            {
                this.getStorage().getItem(i);
                return true;
            }
        }


        List<MoneyValue> moneyValues = MoneyAPI.API.GetContainersMoneyHandler(this.getStorage(), level.players().get(0)).getStoredMoney().allValues();
        if (moneyValues.isEmpty())
            return MoneyValue.empty();

        MoneyValue totalValue = MoneyValue.empty();
        for (int i = 0; i < moneyValues.size(); i++) {
            if (moneyValues.get(i).getUniqueName().contains("lightmanscurrency:coins!main"))
            {
                if (totalValue.isEmpty()) {
                    totalValue = moneyValues.get(i);
                } else {
                    totalValue.addValue(moneyValues.get(i));
                }

            }

            //moneyValues.get(i).getCurrency().getType().equals(ResourceLocation.tryParse());
        }
        return totalValue;
    }*/



    /*public MoneyValue getCardBalance() {

    }*/



    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookup) {
        SimpleContainer temp = InventoryUtil.loadAllItems("Storage", tag, STORAGE_SIZE, lookup);
        for (int i = 0; i < STORAGE_SIZE; i++) {
            this.storage.setItem(i, temp.getItem(i));
        }

        if (tag.contains("Name"))
            this.customName = Component.Serializer.fromJson(tag.getString("Name"), lookup);
    }

    public static MenuProvider getMenuProvider(BlockEntityCardReader be) {return new CardReaderMenuProvider(be); }

    /*@Override
    public @org.jetbrains.annotations.Nullable ResourceLocation getCurrentVariant() {
        return this.currentVariant;
    }


     */
    @Override
    public void setVariant(@org.jetbrains.annotations.Nullable ResourceLocation resourceLocation) {
        this.currentVariant = resourceLocation;
        this.setChanged();
        if(this.isServer())
            BlockEntityUtil.sendUpdatePacket(this);
    }

    private record CardReaderMenuProvider(BlockEntityCardReader be) implements MenuProvider {
        @Nonnull
        @Override
        public Component getDisplayName() { return this.be.getDisplayName(); }
        @Nullable
        @Override
        public AbstractContainerMenu createMenu(int id, @Nonnull Inventory inventory, @Nonnull Player player) { return new CardReaderMenu(id, inventory, this.be); }
    }


    public BlockEntityCardReader(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.CARD_READER.get(), pos, blockState);
        this.storage = new CoinContainer(STORAGE_SIZE);
        this.storage.addListener(i -> this.markStorageDirty());
    }

    public boolean isClient() {
        return this.level != null && this.level.isClientSide;
    }

    public boolean isServer() {
        return this.level != null && !this.level.isClientSide;
    }

    public boolean allowAccess(@Nullable Player player)

    {
        return true;
    }

    private static class ItemHandler extends InvWrapper
    {
        private final BlockEntityCardReader blockEntity;
        public ItemHandler(BlockEntityCardReader blockEntity) { super(blockEntity.storage); this.blockEntity = blockEntity; }
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) { return CoinAPI.API.IsCoin(stack, false); }
        @Nonnull
        @Override
        public Container getInv() {return this.blockEntity.storage; }
    }
}
