package com.github.sn0wkzy.currencies.spigot.command.impl;

import com.github.sn0wkzy.currencies.common.cache.user.CurrencyUserCache;
import com.github.sn0wkzy.currencies.common.command.CustomCommand;
import com.github.sn0wkzy.currencies.common.configuration.ConfigurationValue;
import com.github.sn0wkzy.currencies.common.model.currency.Currency;
import com.github.sn0wkzy.currencies.common.model.user.CurrencyUser;
import com.github.sn0wkzy.currencies.common.util.NumberFormatter;
import com.github.sn0wkzy.currencies.common.util.TagProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CurrencyAddCommand extends CustomCommand {

    private final Currency currency;
    private final CurrencyUserCache currencyUserCache;

    public CurrencyAddCommand(Currency currency, CurrencyUserCache currencyUserCache) {
        super("adicionar", "currencies.add", false, "add");

        this.currency = currency;
        this.currencyUserCache = currencyUserCache;
    }

    @Override
    protected void onCommand(CommandSender commandSender, String[] arguments) {
        if (arguments.length < 2) {
            commandSender.sendMessage("§cUtilize /" + currency.getSingular() + " adicionar <jogador> <quantia>");
            return;
        }

        final Player target = Bukkit.getPlayerExact(arguments[0]);
        if (target == null) {
            commandSender.sendMessage(ConfigurationValue.get(ConfigurationValue::INVALID_PLAYER));
            return;
        }

        final double amount = Double.parseDouble(arguments[1]);
        if (amount <= 0 || Double.isNaN(amount) || Double.isInfinite(amount)) {
            commandSender.sendMessage(ConfigurationValue.get(ConfigurationValue::INVALID_AMOUNT));
            return;
        }

        final CurrencyUser currencyUser = currencyUserCache.getIfPresent(target.getName());
        currencyUser.addAmount(currency.getId(), amount);
        currencyUser.setDirty(true);

        if (target.isOnline()) {
            final String message = ConfigurationValue.get(ConfigurationValue::CURRENCY_ADD_RECEIVE)
                    .replace("{amount}", NumberFormatter.formatWithSuffix(amount))
                    .replace("{currency_color}", currency.getColor())
                    .replace("{currency_symbol}", currency.getSymbol());
            target.sendMessage(message);
        }

        final String message = ConfigurationValue.get(ConfigurationValue::CURRENCY_ADD_SEND)
                .replace("{amount}", NumberFormatter.formatWithSuffix(amount))
                .replace("{currency_color}", currency.getColor())
                .replace("{currency_symbol}", currency.getSymbol())
                .replace("{target}", TagProvider.getOfflineTagWithName(target.getName()));
        commandSender.sendMessage(message);
    }
}
