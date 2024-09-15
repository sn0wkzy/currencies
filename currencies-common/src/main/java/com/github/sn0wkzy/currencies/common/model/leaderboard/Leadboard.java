package com.github.sn0wkzy.currencies.common.model.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@Getter
public class Leadboard {

    private final String currencyId;

    @Setter
    private LinkedList<LeadboardUser> leadboardUsers;

    @Setter
    private long nextUpdate;

    public boolean canUpdate() {
        return System.currentTimeMillis() >= nextUpdate;
    }
}
