package com.github.sn0wkzy.currencies.spigot.listener;

import com.github.sn0wkzy.currencies.common.cache.user.CurrencyUserCache;
import com.github.sn0wkzy.currencies.common.model.user.CurrencyUser;
import com.github.sn0wkzy.currencies.common.repository.CurrencyUserRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

@RequiredArgsConstructor
public class PlayerConnectionListener implements Listener {

    private final CurrencyUserRepository currencyUserRepository;
    private final CurrencyUserCache currencyUserCache;

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        CurrencyUser currencyUser = currencyUserRepository.search(player.getName());
        if (currencyUser == null) {
            currencyUser = new CurrencyUser(player.getName(), new HashMap<>());

            currencyUserCache.save(currencyUser);
            return;
        }

        currencyUserCache.update(currencyUser);
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        final CurrencyUser currencyUser = currencyUserCache.getIfPresent(player.getName());

        currencyUserCache.invalidate(currencyUser);
    }
}
