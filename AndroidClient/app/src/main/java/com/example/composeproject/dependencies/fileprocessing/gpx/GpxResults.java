package com.example.composeproject.dependencies.fileprocessing.gpx;

import java.io.Serializable;

public class GpxResults implements Serializable {
    public double distanceInKilometers;
    public double totalAscentInMete;
    public double totalTimeInMinutes;
    public double avgSpeedInKilometersPerHour;
    public long totalTimeInMillis;
}
