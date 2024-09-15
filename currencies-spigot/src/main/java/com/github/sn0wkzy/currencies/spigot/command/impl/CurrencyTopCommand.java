package com.github.sn0wkzy.currencies.spigot.command.impl;

import com.github.sn0wkzy.currencies.common.command.CustomCommand;
import com.github.sn0wkzy.currencies.common.controller.LeadboardController;
import com.github.sn0wkzy.currencies.common.model.currency.Currency;
import com.github.sn0wkzy.currencies.common.model.leaderboard.Leadboard;
import com.github.sn0wkzy.currencies.spigot.inventory.CurrencyTopInventory;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CurrencyTopCommand extends CustomCommand {

    private final Currency currency;
    private final LeadboardController leadboardController;
    private final ViewFrame viewFrame;

    public CurrencyTopCommand(Currency currency, LeadboardController leadboardController, ViewFrame viewFrame) {
        super("top", "", false, "ranking");

        this.currency = currency;
        this.leadboardController = leadboardController;
        this.viewFrame = viewFrame;
    }

    @Override
    protected void onCommand(CommandSender commandSender, String[] arguments) {
        final Player player = (Player) commandSender;
        final Leadboard leadboard = leadboardController.getLeadboard(currency);
        if (leadboard.canUpdate()) {
            leadboardController.updateLeadboard(currency);
        }

        viewFrame.open(CurrencyTopInventory.class, player, Map.of("currency", currency));
    }
}
