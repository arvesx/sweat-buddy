package com.example.composeproject.dependencies.user;

import java.util.ArrayList;

public class UserData {
    public int userId;
    public String username;
    public ArrayList<Route> routes;
    public ArrayList<Segment> segments;
    public int points;
    public float mostRecentRouteKm;

    public int routesDoneThisMonth;
    public float totalKmThisMonth;

    public float totalElevation;
    public long totalTime;

    public int totalSegments;
    public int bestPositionInSegments;
}
