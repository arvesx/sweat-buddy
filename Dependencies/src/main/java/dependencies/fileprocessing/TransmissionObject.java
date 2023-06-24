package dependencies.fileprocessing;

import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.core.joran.sanity.Pair;
import dependencies.fileprocessing.gpx.GpxResults;
import dependencies.user.GenericStats;
import dependencies.user.LeaderboardEntry;
import dependencies.user.Segment;
import dependencies.user.UserData;

public class TransmissionObject {

    public TransmissionObjectType type;
    public int userId;
    public String gpxFile;

    public String segmentFile;
    public int segmentId;
    public int segmentStart;
    public int segmentEnd;
    public Segment segmentObject;


    public int routeId;

    public UserData userData;
    public GpxResults gpxResults;

    public GenericStats genericStats;

    public List<Pair<Double, Double>> coordinates;
    public ArrayList<LeaderboardEntry> leaderboardList;

    public String username;
    public String password;

    public String message;
    public int success;
}
