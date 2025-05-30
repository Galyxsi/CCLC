package com.eclipticcosmos.cclc;

import com.google.gson.JsonObject;
import com.mojang.brigadier.context.CommandContext;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import io.github.lightman314.lightmanscurrency.api.misc.player.OwnerData;
import io.github.lightman314.lightmanscurrency.api.money.MoneyAPI;
import io.github.lightman314.lightmanscurrency.api.money.bank.BankAPI;
import io.github.lightman314.lightmanscurrency.api.money.bank.IBankAccount;
import io.github.lightman314.lightmanscurrency.api.money.bank.menu.IBankAccountMenu;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.money.bank.source.BankAccountSource;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValueParser;
import io.github.lightman314.lightmanscurrency.api.money.value.builtin.CoinValue;
import io.github.lightman314.lightmanscurrency.api.money.value.builtin.CoinValuePair;
import io.github.lightman314.lightmanscurrency.api.traders.TradeContext;
import io.github.lightman314.lightmanscurrency.api.traders.TraderData;
import io.github.lightman314.lightmanscurrency.api.traders.trade.TradeData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.trade.DisplayEntry;
import io.github.lightman314.lightmanscurrency.common.bank.BankAccount;
import io.github.lightman314.lightmanscurrency.common.commands.arguments.MoneyValueArgument;
import io.github.lightman314.lightmanscurrency.common.impl.BankAPIImpl;
import io.github.lightman314.lightmanscurrency.common.traders.item.tradedata.ItemTradeData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CardReaderPeripheral implements GenericPeripheral {

    @LuaFunction
    public final String getBalance(BlockEntityCardReader cardReader) {
        return cardReader.getStoredBalance().getString();
    }

    @LuaFunction
    public final long getNumericalBalance(BlockEntityCardReader cardReader) {
        return cardReader.getStoredBalance().getCoreValue();
    }

    /*@LuaFunction
    public final List<String> getAllTrades(BlockEntityCardReader cardReader) {
        List<TradeData> allTrades = new ArrayList<>();
        List<String> formattedTrades = new ArrayList<>();
        for (TraderData trader : cardReader.getTraders())
        {
            for (int i = 0; i < trader.getTradeCount(); i++)
            {

                TradeData data = trader.getTrade(i);
                trader.getTradeStock(i);
                if (data.getCost() != null && data.getCost() != MoneyValue.empty() && data.getStock(TradeContext.createStorageMode(trader)) != 0)
                {
                    formattedTrades.add("Cost: " + data.getCost().getString() + " Stock: " + data.getStock(TradeContext.createStorageMode(trader)) + TradeContext.createStorageMode(trader));
                    allTrades.add(trader.getTrade(i));
                }

            }
        }


        return formattedTrades;
    }*/

    @LuaFunction
    public final String payAccount(BlockEntityCardReader cardReader, IArguments arguments) throws LuaException {
        int accountID = arguments.getInt(0);
        int amount = arguments.getInt(1);
        List<IBankAccount> bankAccounts = BankAPI.API.GetAllBankAccounts(false);
        List<BankReference> bankReferences = BankAPI.API.GetAllBankReferences(false);
        String test = "";
        MoneyValue moneyValue = cardReader.getStoredBalance();
        moneyValue = moneyValue.multiplyValue((double) 1 / moneyValue.getCoreValue()).multiplyValue(amount);
        if (cardReader.getStoredBalance().getCoreValue() > MoneyValue.empty().getCoreValue() && cardReader.getStoredBalance().getCoreValue() >= moneyValue.getCoreValue())
        {
            for (int i = 0; i < bankAccounts.size(); i++)
            {
                if (accountID == bankReferences.get(i).hashCode())
                {
                    // "Found";
                    if (BankAPI.API.BankDepositFromServer(bankReferences.get(i).get(), moneyValue, true))
                    {
                        test = "" + true;
                        cardReader.removeMoney(moneyValue);
                    }
                    //test = "" + (cardReader.getStoredBalance());
                }
            }
        }
        //BankAPI.API.BankWithdrawFromServer();
        return test;
    }

    @LuaFunction
    public final List<String> getAllAccounts(BlockEntityCardReader cardReader)  {
        List<IBankAccount> bankAccounts = BankAPI.API.GetAllBankAccounts(false);
        List<BankReference> bankReferences = BankAPI.API.GetAllBankReferences(false);
        List<String> test = new ArrayList<>();
        for (int i = 0; i < bankAccounts.size(); i++)
        {
            test.add(bankAccounts.get(i).getName().getString() + ": " + bankReferences.get(i).hashCode());
        }

        //BankAPI.API.BankWithdrawFromServer();
        return test;
    }

    /*@LuaFunction
    public final List<String> getAllReferences(BlockEntityCardReader cardReader)  {
        List<BankReference> bankAccounts = BankAPI.API.GetAllBankReferences(false);
        List<String> test = new ArrayList<>();
        for (int i = 0; i < bankAccounts.size(); i++)
        {
            test.add(bankAccounts.get(i).toString() + ": " + bankAccounts.get(i).hashCode());
        }

        //BankAPI.API.BankWithdrawFromServer();
        return test;
    }*/

    @Override
    public String id() {
        return ResourceLocation.fromNamespaceAndPath(CCLC.MODID, "cardreader").toString();
    }
}
