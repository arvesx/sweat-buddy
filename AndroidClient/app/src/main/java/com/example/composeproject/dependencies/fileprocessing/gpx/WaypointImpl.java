package com.example.composeproject.dependencies.fileprocessing.gpx;

import java.io.Serializable;
import java.util.Objects;

public class WaypointImpl implements Serializable {
    private int id;
    private final double longitude;
    private final double latitude;
    private final double elevation;
    private final long time;

    public WaypointImpl(double longitude, double latitude, double elevation, long time) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.elevation = elevation;
        this.time = time;
    }

    public boolean isEqualTo(WaypointImpl wp) {

        // 5 decimal places equals approximately 1.1 meters in the equator
        // we can't reduce it to 4 decimals because that would mean approximately 11 meters in the equator.
        if (!Objects.equals(truncateDouble(this.longitude), truncateDouble(wp.getLongitude()))) {
            return false;
        }

        if (!Objects.equals(truncateDouble(this.latitude), truncateDouble(wp.getLatitude()))) {
            return false;
        }

        return true;
    }

    public Double truncateDouble(Double d) {
        return Math.floor(d * 100000) / 100000;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getID() {
        return this.id;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getElevation() {
        return elevation;
    }

    public long getTime() {
        return time;
    }
}
