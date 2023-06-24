package com.example.composeproject.dependencies.user;

import com.example.composeproject.dependencies.fileprocessing.gpx.WaypointImpl;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import kotlin.Pair;

public class Route {

    public int routeId;
    public String routeName;
    public int points;

    public List<LatLng> coordinatesLatLng;
    public List<Pair<Double, Double>> coordinates;

    public ArrayList<WaypointImpl> routeWaypoints;

    public double totalDistanceInKm;
    public double totalElevationInM;
    public double averageSpeedInKmH;
    public double totalTimeInMinutes;

    public long totalTimeInMillis;
    public int routeType;

    public Route(int routeId)
    {
        this.routeId = routeId;
    }

}
