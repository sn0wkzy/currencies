package com.github.sn0wkzy.currencies.common.cache.user;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.sn0wkzy.currencies.common.model.user.CurrencyUser;
import com.github.sn0wkzy.currencies.common.repository.CurrencyUserRepository;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CurrencyUserCache {

    private final CurrencyUserRepository currencyUserRepository;
    private final Map<String, CurrencyUser> cache = new HashMap<>();

    public CurrencyUser getIfPresent(String playerName) {
        return cache.get(playerName);
    }

    public void update(CurrencyUser currencyUser) {
        cache.put(currencyUser.getPlayerName(), currencyUser);
    }

    public void save(CurrencyUser currencyUser) {
        cache.put(currencyUser.getPlayerName(), currencyUser);
        currencyUserRepository.insertOne(currencyUser);
    }

    public void invalidate(CurrencyUser currencyUser) {
        cache.remove(currencyUser.getPlayerName());
    }

    public Set<CurrencyUser> getDirtyUsers() {
        return cache.values()
                .stream()
                .filter(CurrencyUser::isDirty)
                .collect(Collectors.toSet());
    }
}
