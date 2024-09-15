package com.github.sn0wkzy.currencies.common.controller;

import com.github.sn0wkzy.currencies.common.cache.leadboard.LeadboardCache;
import com.github.sn0wkzy.currencies.common.model.currency.Currency;
import com.github.sn0wkzy.currencies.common.model.leaderboard.Leadboard;
import com.github.sn0wkzy.currencies.common.model.leaderboard.LeadboardUser;
import com.github.sn0wkzy.currencies.common.repository.CurrencyUserRepository;
import lombok.RequiredArgsConstructor;

import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class LeadboardController {

    private final long DEFAULT_UPDATE = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(10L);

    private final LeadboardCache leadboardCache;
    private final CurrencyUserRepository currencyUserRepository;

    public void updateLeadboard(Currency currency) {
        final LinkedList<LeadboardUser> leadboardUsers = currencyUserRepository.searchLeaderboard(currency);

        Leadboard leadboard = leadboardCache.getLeadboard(currency.getId());
        if (leadboard == null) {
            leadboard = new Leadboard(currency.getId(), leadboardUsers, DEFAULT_UPDATE);

            leadboardCache.registerLeadboard(leadboard);
            return;
        }

        leadboard.setLeadboardUsers(leadboardUsers);
        leadboard.setNextUpdate(DEFAULT_UPDATE);
    }

    public Leadboard getLeadboard(Currency currency) {
        if (!leadboardCache.hasLeadboard(currency.getId())) {
            updateLeadboard(currency);
            return leadboardCache.getLeadboard(currency.getId());
        }

        return leadboardCache.getLeadboard(currency.getId());
    }
}
