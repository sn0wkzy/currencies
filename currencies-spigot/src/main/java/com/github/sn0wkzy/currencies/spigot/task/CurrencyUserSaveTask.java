package com.github.sn0wkzy.currencies.spigot.task;

import com.github.sn0wkzy.currencies.common.cache.user.CurrencyUserCache;
import com.github.sn0wkzy.currencies.common.model.user.CurrencyUser;
import com.github.sn0wkzy.currencies.common.repository.CurrencyUserRepository;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class CurrencyUserSaveTask implements Runnable {

    private final CurrencyUserCache currencyUserCache;
    private final CurrencyUserRepository currencyUserRepository;

    @Override
    public void run() {
        final Set<CurrencyUser> dirtyUsers = currencyUserCache.getDirtyUsers();
        if (dirtyUsers.isEmpty()) return;

        dirtyUsers.forEach(currencyUser -> currencyUser.setDirty(false));
        currencyUserRepository.bulkUpdate(dirtyUsers);
    }
}
