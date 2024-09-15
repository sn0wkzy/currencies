package com.github.sn0wkzy.currencies.common.model.currency;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Currency {

    private final String id, singular, plural, symbol, color;
    private final String[] aliases;

    public String getFormattedName() {
        return color + plural;
    }

    public String getFormattedSymbol() {
        return color + symbol;
    }
}
