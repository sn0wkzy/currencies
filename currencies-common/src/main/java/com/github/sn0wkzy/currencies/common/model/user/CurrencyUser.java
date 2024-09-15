package com.github.sn0wkzy.currencies.common.model.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class CurrencyUser {

    private final String playerName;
    private final Map<String, Double> currencies;

    @Setter
    private boolean dirty = false;

    public boolean hasAmount(String currencyId, double amount) {
        return getAmount(currencyId) >= amount;
    }

    public void setAmount(String currencyId, double amount) {
        this.currencies.put(currencyId, amount);
    }

    public void addAmount(String currencyId, double amount) {
        this.currencies.merge(currencyId, amount, Double::sum);
    }

    public void removeAmount(String currencyId, double amount) {
        this.currencies.compute(currencyId, (key, currencyAmount) -> {
            if (currencyAmount == null || currencyAmount <= 0) {
                return 0.0;
            }

            double newAmount = currencyAmount - amount;
            return newAmount > 0 ? newAmount : 0.0;
        });
    }

    public double getAmount(String currencyId) {
        return this.currencies.getOrDefault(currencyId, 0D);
    }
}
