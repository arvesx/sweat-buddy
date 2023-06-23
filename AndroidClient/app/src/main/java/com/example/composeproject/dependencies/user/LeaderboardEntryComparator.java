package com.example.composeproject.dependencies.user;

import java.util.Comparator;

public class LeaderboardEntryComparator implements Comparator<LeaderboardEntry> {
    @Override
    public int compare(LeaderboardEntry o1, LeaderboardEntry o2) {
        return Integer.compare(o2.points, o1.points);
    }
}
