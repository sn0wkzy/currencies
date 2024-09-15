package com.github.sn0wkzy.currencies.common.configuration;

import com.henryfabio.minecraft.configinjector.common.annotations.ConfigField;
import com.henryfabio.minecraft.configinjector.common.annotations.ConfigFile;
import com.henryfabio.minecraft.configinjector.common.annotations.TranslateColors;
import com.henryfabio.minecraft.configinjector.common.injector.ConfigurationInjectable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Function;

@Getter
@TranslateColors
@Accessors(fluent = true)
@ConfigFile("config.yml")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConfigurationValue implements ConfigurationInjectable {

    @Getter
    private static final ConfigurationValue instance = new ConfigurationValue();

    @ConfigField("database.connection-url")
    private String CONNECTION_URL;

    @ConfigField("currency")
    private ConfigurationSection CURRENCY_SECTION;

    @ConfigField("messages.currency-view")
    private String CURRENCY_VIEW;

    @ConfigField("messages.invalid-amount")
    private String INVALID_AMOUNT;

    @ConfigField("messages.invalid-player")
    private String INVALID_PLAYER;

    @ConfigField("messages.currency-set-send")
    private String CURRENCY_SET_SEND;

    @ConfigField("messages.currency-add-send")
    private String CURRENCY_ADD_SEND;

    @ConfigField("messages.currency-add-receive")
    private String CURRENCY_ADD_RECEIVE;

    @ConfigField("messages.currency-remove-send")
    private String CURRENCY_REMOVE_SEND;

    @ConfigField("messages.currency-remove-receive")
    private String CURRENCY_REMOVE_RECEIVE;

    @ConfigField("messages.currency-pay-send")
    private String CURRENCY_PAY_SEND;

    @ConfigField("messages.currency-pay-receive")
    private String CURRENCY_PAY_RECEIVE;

    @ConfigField("messages.has-not-amount")
    private String HAS_NOT_AMOUNT;

    @ConfigField("messages.cant-send-to-yourself")
    private String SEND_TO_YOURSELF;

    public static <T> T get(Function<ConfigurationValue, T> function) {
        return function.apply(instance);
    }
}
