package dependencies.user;

import java.util.Comparator;

public class SegmentLeaderboardEntryComparator implements Comparator<SegmentLeaderboardEntry> {
    @Override
    public int compare(SegmentLeaderboardEntry o1, SegmentLeaderboardEntry o2) {
        return Long.compare(o1.totalTime, o2.totalTime);
    }
}
