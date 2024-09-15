package com.github.sn0wkzy.currencies.common.cache.leadboard;

import com.github.sn0wkzy.currencies.common.model.leaderboard.Leadboard;

import java.util.HashMap;
import java.util.Map;

public class LeadboardCache {

    private final Map<String, Leadboard> cache = new HashMap<>();

    public void registerLeadboard(Leadboard leadboard) {
        cache.put(leadboard.getCurrencyId(), leadboard);
    }

    public boolean hasLeadboard(String currencyId) {
        return cache.containsKey(currencyId);
    }

    public Leadboard getLeadboard(String currencyId) {
        return cache.get(currencyId);
    }
}
