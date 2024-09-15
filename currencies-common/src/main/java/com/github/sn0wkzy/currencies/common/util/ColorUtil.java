package com.github.sn0wkzy.currencies.common.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ColorUtil {

    public String colored(String message) {
        if (message == null) {
            return "";
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public String[] colored(String... messages) {
        for (int i = 0; i < messages.length; i++) {
            messages[i] = colored(messages[i]);
        }

        return messages;
    }

    public List<String> colored(List<String> description) {
        return description.stream().map(ColorUtil::colored).collect(Collectors.toList());
    }
}
