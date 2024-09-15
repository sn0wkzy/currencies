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

public class CurrencyPayCommand extends CustomCommand {

    private final Currency currency;
    private final CurrencyUserCache currencyUserCache;

    public CurrencyPayCommand(Currency currency, CurrencyUserCache currencyUserCache) {
        super("enviar", "currencies.pay", true, "pay");

        this.currency = currency;
        this.currencyUserCache = currencyUserCache;
    }

    @Override
    protected void onCommand(CommandSender commandSender, String[] arguments) {
        final Player player = (Player) commandSender;
        if (arguments.length < 2) {
            player.sendMessage("§cUtilize /" + currency.getSingular() + " enviar <jogador> <quantia>");
            return;
        }

        final Player target = Bukkit.getPlayerExact(arguments[0]);
        if (target == null) {
            commandSender.sendMessage(ConfigurationValue.get(ConfigurationValue::INVALID_PLAYER));
            return;
        }

        if (player.getName().equals(target.getName())) {
            player.sendMessage(ConfigurationValue.get(ConfigurationValue::SEND_TO_YOURSELF));
            return;
        }

        final double amount = Double.parseDouble(arguments[1]);
        if (amount <= 0 || Double.isNaN(amount) || Double.isInfinite(amount)) {
            commandSender.sendMessage(ConfigurationValue.get(ConfigurationValue::INVALID_AMOUNT));
            return;
        }

        final CurrencyUser currencyUser = currencyUserCache.getIfPresent(player.getName());
        if (!currencyUser.hasAmount(currency.getId(), amount)) {
            player.sendMessage(ConfigurationValue.get(ConfigurationValue::HAS_NOT_AMOUNT));
            return;
        }

        currencyUser.removeAmount(currency.getId(), amount);
        currencyUser.setDirty(true);

        final CurrencyUser currencyTarget = currencyUserCache.getIfPresent(target.getName());
        currencyTarget.addAmount(currency.getId(), amount);
        currencyTarget.setDirty(true);

        if (target.isOnline()) {
            final String message = ConfigurationValue.get(ConfigurationValue::CURRENCY_PAY_RECEIVE)
                    .replace("{amount}", NumberFormatter.formatWithSuffix(amount))
                    .replace("{currency_color}", currency.getColor())
                    .replace("{currency_symbol}", currency.getSymbol())
                    .replace("{target}", TagProvider.getOfflineTagWithName(target.getName()));
            target.sendMessage(message);
        }

        final String message = ConfigurationValue.get(ConfigurationValue::CURRENCY_PAY_SEND)
                .replace("{amount}", NumberFormatter.formatWithSuffix(amount))
                .replace("{currency_color}", currency.getColor())
                .replace("{currency_symbol}", currency.getSymbol())
                .replace("{target}", TagProvider.getOfflineTagWithName(target.getName()));
        player.sendMessage(message);
    }
}
