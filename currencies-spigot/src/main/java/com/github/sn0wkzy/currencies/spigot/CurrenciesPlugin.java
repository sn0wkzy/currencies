package com.github.sn0wkzy.currencies.spigot;

import com.github.sn0wkzy.currencies.common.cache.currency.CurrencyCache;
import com.github.sn0wkzy.currencies.common.cache.leadboard.LeadboardCache;
import com.github.sn0wkzy.currencies.common.cache.user.CurrencyUserCache;
import com.github.sn0wkzy.currencies.common.command.CustomCommand;
import com.github.sn0wkzy.currencies.common.controller.ConfigurationController;
import com.github.sn0wkzy.currencies.common.controller.LeadboardController;
import com.github.sn0wkzy.currencies.common.repository.CurrencyUserRepository;
import com.github.sn0wkzy.currencies.spigot.command.CurrencyCommand;
import com.github.sn0wkzy.currencies.spigot.inventory.CurrencyTopInventory;
import com.github.sn0wkzy.currencies.spigot.listener.PlayerConnectionListener;
import com.github.sn0wkzy.currencies.spigot.task.CurrencyUserSaveTask;
import lombok.SneakyThrows;
import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.lang.reflect.Field;

public class CurrenciesPlugin extends JavaPlugin {

    @Override
    public void onLoad() {
        ConfigurationController.of(this).setup();
    }

    @Override
    public void onEnable() {

        final CurrencyUserRepository currencyUserRepository = new CurrencyUserRepository();
        final CurrencyUserCache currencyUserCache = new CurrencyUserCache(currencyUserRepository);

        final CurrencyCache currencyCache = new CurrencyCache();

        final LeadboardCache leadboardCache = new LeadboardCache();
        final LeadboardController leadboardController = new LeadboardController(leadboardCache, currencyUserRepository);

        final ViewFrame viewFrame = ViewFrame.of(this).with(new CurrencyTopInventory(leadboardController)).register();

        currencyCache.getAll().forEach(currency -> registerCommand(new CurrencyCommand(currency, currencyUserCache, leadboardController, viewFrame)));
        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(currencyUserRepository, currencyUserCache), this);

        new CurrenciesAPI(currencyUserCache);

        final BukkitScheduler scheduler = Bukkit.getScheduler();
        scheduler.runTaskTimerAsynchronously(this, new CurrencyUserSaveTask(currencyUserCache, currencyUserRepository), 40L, 40L);
    }

    @SneakyThrows
    private void registerCommand(CustomCommand customCommand) {
        final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);

        final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        commandMap.register(customCommand.getName(), customCommand);
    }
}
