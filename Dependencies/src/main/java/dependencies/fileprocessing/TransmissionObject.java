package dependencies.fileprocessing;

import java.util.List;

import ch.qos.logback.core.joran.sanity.Pair;
import dependencies.fileprocessing.gpx.GpxResults;
import dependencies.user.GenericStats;
import dependencies.user.UserData;

public class TransmissionObject {

    public TransmissionObjectType type;
    public int userId;
    public String gpxFile;
    public UserData userData;
    public GpxResults gpxResults;

    public GenericStats genericStats;

    public List<Pair<Double, Double>> coordinates;
    public List<UserData> leaderboardList;

    public String username;
    public String password;

    public String message;
    public int success;
}
