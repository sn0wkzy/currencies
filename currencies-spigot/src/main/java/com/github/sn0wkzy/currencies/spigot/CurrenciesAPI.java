package com.github.sn0wkzy.currencies.spigot;

import com.github.sn0wkzy.currencies.common.cache.user.CurrencyUserCache;
import com.github.sn0wkzy.currencies.common.model.user.CurrencyUser;

public class CurrenciesAPI {

    private static CurrenciesAPI instance;
    private final CurrencyUserCache currencyUserCache;

    public CurrenciesAPI(CurrencyUserCache currencyUserCache) {
        this.currencyUserCache = currencyUserCache;

        instance = this;
    }

    public void setAmount(String playerName, String currencyId, double amount) {
        final CurrencyUser currencyUser = currencyUserCache.getIfPresent(playerName);
        currencyUser.setAmount(currencyId, amount);
        currencyUser.setDirty(true);
    }

    public void addAmount(String playerName, String currencyId, double amount) {
        final CurrencyUser currencyUser = currencyUserCache.getIfPresent(playerName);
        currencyUser.addAmount(currencyId, amount);
        currencyUser.setDirty(true);
    }

    public void removeAmount(String playerName, String currencyId, double amount) {
        final CurrencyUser currencyUser = currencyUserCache.getIfPresent(playerName);
        currencyUser.removeAmount(currencyId, amount);
        currencyUser.setDirty(true);
    }

    public boolean hasAmount(String playerName, String currencyId, double amount) {
        final CurrencyUser currencyUser = currencyUserCache.getIfPresent(playerName);
        return currencyUser.hasAmount(currencyId, amount);
    }

}
