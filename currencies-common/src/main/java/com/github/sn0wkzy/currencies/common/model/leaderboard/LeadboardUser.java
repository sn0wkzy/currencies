package com.github.sn0wkzy.currencies.common.model.leaderboard;

import com.github.sn0wkzy.currencies.common.util.TagProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class LeadboardUser {

    private final String playerName;
    private final int position;
    private final double amount;

    public String getDisplayName() {
        return "§7" + position + "º " + TagProvider.getOfflineTagWithName(playerName);
    }
}
