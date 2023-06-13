package dependencies.user;

import java.util.List;

import ch.qos.logback.core.joran.sanity.Pair;

public class Route {
    public String routeName;
    public int points;

    public List<Pair<Double, Double>> coordinates;

    public double totalDistanceInKm;
    public double totalElevationInM;
    public double averageSpeedInKmH;
    public double totalTimeInMinutes;

    public int routeType;

}
