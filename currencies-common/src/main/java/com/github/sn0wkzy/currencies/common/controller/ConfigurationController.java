package com.github.sn0wkzy.currencies.common.controller;

import com.github.sn0wkzy.currencies.common.configuration.ConfigurationValue;
import com.henryfabio.minecraft.configinjector.bukkit.injector.BukkitConfigurationInjector;
import lombok.Data;
import org.bukkit.plugin.Plugin;

@Data(staticConstructor = "of")
public class ConfigurationController {

    private final Plugin plugin;

    public void setup() {
        final BukkitConfigurationInjector configurationInjector = new BukkitConfigurationInjector(plugin);
        configurationInjector.saveDefaultConfiguration(plugin, "config.yml");
        configurationInjector.injectConfiguration(ConfigurationValue.instance());
    }

}
