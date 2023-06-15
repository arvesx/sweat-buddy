package com.example.composeproject.dependencies.user;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import kotlin.Pair;

public class Route {
    public String routeName;
    public int points;

    public List<LatLng> coordinatesLatLng;
    public List<Pair<Double, Double>> coordinates;

    public double totalDistanceInKm;
    public double totalElevationInM;
    public double averageSpeedInKmH;
    public double totalTimeInMinutes;

    public int routeType;

}
