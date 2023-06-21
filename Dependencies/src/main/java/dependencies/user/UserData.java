package dependencies.user;

import java.util.ArrayList;

public class UserData {
    public int userId;
    public String username;
    public ArrayList<Route> routes;
    public ArrayList<SegmentAttempt> segments;
    public int points;
    public float mostRecentRouteKm;

    public int routesDoneThisMonth;
    public float totalKmThisMonth;

    public float totalElevation;
    public long totalTime;

    public int totalSegments;
    public int bestPositionInSegments;

    public int getPoints()
    {
        return this.points;
    }
}