package com.github.sn0wkzy.currencies.common.cache.currency;

import com.github.sn0wkzy.currencies.common.adapter.CurrencyAdapter;
import com.github.sn0wkzy.currencies.common.model.currency.Currency;
import org.bukkit.Bukkit;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class CurrencyCache {

    private final Logger logger = Bukkit.getLogger();
    private final Set<Currency> cache = new HashSet<>();

    public CurrencyCache() {
        new CurrencyAdapter().getAllCurrency().forEach(this::registerCurrency);
    }

    public void registerCurrency(Currency currency) {
        cache.add(currency);
        logger.info("Currency '" + currency.getId() + "' registered");
    }

    public Set<Currency> getAll() {
        return cache;
    }
}
