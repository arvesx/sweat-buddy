package activitytracker;

import java.io.Serializable;

public class Waypoint implements Serializable{
    
    double longitude;
    double latitude;
    double elevation;
    long time;

    public Waypoint(double longitude, double latitude, double elevation, long time) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.elevation = elevation;
        this.time = time;
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
