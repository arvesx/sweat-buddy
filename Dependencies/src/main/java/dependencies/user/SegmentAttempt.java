package dependencies.user;

import dependencies.fileprocessing.gpx.WaypointImpl;

import java.util.ArrayList;

public class SegmentAttempt {
    public int segmentId;
    public String segmentName;
    public ArrayList<WaypointImpl> waypoints;

    public float totalDistance;
    public float elevation;
    public long totalTime;
    public float avgSpeed;
}
