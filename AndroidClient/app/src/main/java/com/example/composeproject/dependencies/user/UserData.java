package com.example.composeproject.dependencies.user;

import java.util.ArrayList;

public class UserData {
    public int userId;
    public ArrayList<Route> routes;
    public ArrayList<Segment> segments;
    public int points;
    public float mostRecentRouteKm;

    public int routesDoneThisMonth;
    public int totalKmThisMonth;

    public int totalSegments;
    public int bestPositionInSegments;
}
