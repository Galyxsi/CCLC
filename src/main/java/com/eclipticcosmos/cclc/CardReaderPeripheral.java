package com.eclipticcosmos.cclc;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.GenericPeripheral;
import io.github.lightman314.lightmanscurrency.api.money.bank.BankAPI;
import io.github.lightman314.lightmanscurrency.api.money.bank.IBankAccount;
import io.github.lightman314.lightmanscurrency.api.money.bank.reference.BankReference;
import io.github.lightman314.lightmanscurrency.api.money.value.MoneyValue;
import net.minecraft.resources.ResourceLocation;


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
