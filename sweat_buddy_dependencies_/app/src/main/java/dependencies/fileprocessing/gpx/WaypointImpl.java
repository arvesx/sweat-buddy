package dependencies.fileprocessing.gpx;

import java.io.Serializable;

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
