package dependencies.user;

import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.core.joran.sanity.Pair;
import dependencies.fileprocessing.gpx.WaypointImpl;

public class Route {
    public int routeId;
    public String routeName;
    public int points;

    public List<Pair<Double, Double>> coordinates;
    public ArrayList<WaypointImpl> routeWaypoints;
    public double totalDistanceInKm;
    public double totalElevationInM;
    public double averageSpeedInKmH;
    public double totalTimeInMinutes;

    public long totalTimeInMillis;

    public int routeType;

}