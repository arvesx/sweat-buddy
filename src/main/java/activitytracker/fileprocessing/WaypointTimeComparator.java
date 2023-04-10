package main.java.activitytracker.fileprocessing;

import main.java.activitytracker.Waypoint;

import java.util.Comparator;

public class WaypointTimeComparator implements Comparator<Waypoint> {
    @Override
    public int compare(Waypoint o1, Waypoint o2) {
        return Long.compare(o1.getTime(), o2.getTime());
    }
}
