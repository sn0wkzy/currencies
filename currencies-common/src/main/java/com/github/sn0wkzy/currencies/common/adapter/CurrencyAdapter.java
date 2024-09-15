package com.github.sn0wkzy.currencies.common.adapter;

import com.github.sn0wkzy.currencies.common.configuration.ConfigurationValue;
import com.github.sn0wkzy.currencies.common.model.currency.Currency;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CurrencyAdapter {

    public Set<Currency> getAllCurrency() {
        final ConfigurationSection configurationSection = ConfigurationValue.get(ConfigurationValue::CURRENCY_SECTION);
        if (configurationSection == null) return null;

        return configurationSection.getKeys(false).stream()
                .map(configurationSection::getConfigurationSection)
                .filter(Objects::nonNull)
                .map(this::readCurrency)
                .collect(Collectors.toSet());
    }

    private Currency readCurrency(ConfigurationSection configurationSection) {
        final String id = configurationSection.getName();

        final String singular = configurationSection.getString("singular");
        final String plural = configurationSection.getString("plural");
        final String symbol = configurationSection.getString("symbol");
        final String color = configurationSection.getString("color");
        final String[] aliases = configurationSection.getStringList("aliases").toArray(new String[0]);

        return new Currency(id, singular, plural, symbol, color, aliases);
    }
}
