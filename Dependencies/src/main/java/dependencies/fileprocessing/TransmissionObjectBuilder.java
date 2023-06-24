package dependencies.fileprocessing;

import dependencies.fileprocessing.gpx.GpxResults;
import dependencies.user.GenericStats;

import java.util.ArrayList;
import java.util.List;

import ch.qos.logback.core.joran.sanity.Pair;
import dependencies.user.LeaderboardEntry;
import dependencies.user.Segment;
import dependencies.user.UserData;

public class TransmissionObjectBuilder {
    private final TransmissionObject to;

    public TransmissionObjectBuilder() {
        to = new TransmissionObject();
    }

    public TransmissionObjectBuilder type(TransmissionObjectType type) {
        to.type = type;
        return this;
    }

    public TransmissionObjectBuilder userData(UserData userData) {
        to.userData = userData;
        return this;
    }

    public TransmissionObjectBuilder leaderboardList(ArrayList<LeaderboardEntry> leaderboardList) {
        to.leaderboardList = leaderboardList;
        return this;
    }

    public TransmissionObjectBuilder segmentObject(Segment segmentObject) {
        to.segmentObject = segmentObject;
        return this;
    }

    public TransmissionObjectBuilder segmentFile(String segmentFile) {
        to.segmentFile = segmentFile;
        return this;
    }

    public TransmissionObjectBuilder coordinates(List<Pair<Double, Double>> coordinates) {
        to.coordinates = coordinates;
        return this;
    }

    public TransmissionObjectBuilder username(String username) {
        to.username = username;
        return this;
    }

    public TransmissionObjectBuilder password(String password) {
        to.password = password;
        return this;
    }

    public TransmissionObjectBuilder success(int success) {
        to.success = success;
        return this;
    }

    public TransmissionObjectBuilder message(String message) {
        to.message = message;
        return this;
    }

    public TransmissionObjectBuilder routeId(int routeId) {
        to.routeId = routeId;
        return this;
    }

    public TransmissionObjectBuilder userId(int userId) {
        to.userId = userId;
        return this;
    }

    public TransmissionObjectBuilder segmentId(int segmentId) {
        to.segmentId = segmentId;
        return this;
    }

    public TransmissionObjectBuilder segmentStart(int segmentStart) {
        to.segmentStart = segmentStart;
        return this;
    }

    public TransmissionObjectBuilder segmentEnd(int segmentEnd) {
        to.segmentEnd = segmentEnd;
        return this;
    }

    public TransmissionObjectBuilder gpxFile(String gpxFile) {
        to.gpxFile = gpxFile;
        return this;
    }

    public TransmissionObjectBuilder gpxResults(GpxResults gpxResults) {
        to.gpxResults = gpxResults;
        return this;
    }

    public TransmissionObjectBuilder genericStats(GenericStats genericStats) {
        to.genericStats = genericStats;
        return this;
    }

    public TransmissionObject craft() {
        return to;
    }
}
