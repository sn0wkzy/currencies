package com.github.sn0wkzy.currencies.spigot.command;

import com.github.sn0wkzy.currencies.common.cache.user.CurrencyUserCache;
import com.github.sn0wkzy.currencies.common.command.CustomCommand;
import com.github.sn0wkzy.currencies.common.configuration.ConfigurationValue;
import com.github.sn0wkzy.currencies.common.controller.LeadboardController;
import com.github.sn0wkzy.currencies.common.model.currency.Currency;
import com.github.sn0wkzy.currencies.common.model.user.CurrencyUser;
import com.github.sn0wkzy.currencies.common.util.NumberFormatter;
import com.github.sn0wkzy.currencies.spigot.command.impl.CurrencyAddCommand;
import com.github.sn0wkzy.currencies.spigot.command.impl.CurrencyPayCommand;
import com.github.sn0wkzy.currencies.spigot.command.impl.CurrencyRemoveCommand;
import com.github.sn0wkzy.currencies.spigot.command.impl.CurrencyTopCommand;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CurrencyCommand extends CustomCommand {

    private final Currency currency;
    private final CurrencyUserCache currencyUserCache;
    private final LeadboardController leadboardController;
    private final ViewFrame viewFrame;

    public CurrencyCommand(Currency currency, CurrencyUserCache currencyUserCache, LeadboardController leadboardController,
                           ViewFrame viewFrame) {
        super(currency.getSingular(), "", false, currency.getAliases());
        this.currency = currency;
        this.currencyUserCache = currencyUserCache;
        this.leadboardController = leadboardController;
        this.viewFrame = viewFrame;

        registerSubCommands(
                new CurrencyAddCommand(currency, currencyUserCache),
                new CurrencyRemoveCommand(currency, currencyUserCache),
                new CurrencyPayCommand(currency, currencyUserCache),
                new CurrencyTopCommand(currency, leadboardController, viewFrame)
        );
    }

    @Override
    protected void onCommand(CommandSender commandSender, String[] arguments) {
        final Player player = (Player) commandSender;
        final CurrencyUser currencyUser = currencyUserCache.getIfPresent(player.getName());

        final double amount = currencyUser.getAmount(currency.getId());
        final String message = ConfigurationValue.get(ConfigurationValue::CURRENCY_VIEW)
                .replace("{amount}", NumberFormatter.formatWithSuffix(amount))
                .replace("{currency_color}", currency.getColor())
                .replace("{currency_symbol}", currency.getSymbol())
                .replace("{currency_plural}", currency.getPlural());
        player.sendMessage(message);
    }
}
