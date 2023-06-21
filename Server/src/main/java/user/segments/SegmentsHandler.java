package user.segments;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dependencies.fileprocessing.gpx.GpxFile;
import dependencies.fileprocessing.gpx.GpxResults;
import dependencies.fileprocessing.gpx.WaypointImpl;
import dependencies.user.Segment;
import dependencies.user.SegmentAttempt;
import dependencies.user.UserData;
import user.userdata.DataExchangeHandler;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

import static server.Utils.LOGGER;

public class SegmentsHandler {

    public static ArrayList<Segment> allSegments = new ArrayList<>();

    public static final Object ALL_SEGMENTS_LIST_LOCK = new Object();

    private static final String segmentsFilePath = "src/main/java/user/segments/segments_db.json";


    public static ArrayList<WaypointImpl> isSegmentPresentInRoute(
            ArrayList<WaypointImpl> segmentWps,
            ArrayList<WaypointImpl> routeWps) {


        if (segmentWps.size() > routeWps.size()) {
            throw new RuntimeException("Segment waypoints sequence must be smaller than route waypoints sequence.");
        }

        ArrayList<WaypointImpl> sublistWps = new ArrayList<>();
        int index = 0;
        for (var i = 0; i < routeWps.size(); i++) {
            if (segmentWps.get(index).isEqualTo(routeWps.get(i))) {
                index++;
                sublistWps.add(routeWps.get(i));
                if (index == segmentWps.size()) break;
            }
        }

        if (index < segmentWps.size()) {
            return null;
        }

        return sublistWps;
    }


    public static void readAllSegments() {
        Gson gson = new Gson();
        synchronized (ALL_SEGMENTS_LIST_LOCK) {
            try (FileReader fileReader = new FileReader(segmentsFilePath)) {
                allSegments = gson.fromJson(fileReader, new TypeToken<ArrayList<Segment>>() {
                }.getType());
                LOGGER.info("All existing segments and leaderboards have been read from file");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void writeAllSegmentsToJson() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(allSegments);
        synchronized (ALL_SEGMENTS_LIST_LOCK) {
            try (FileWriter writer = new FileWriter(segmentsFilePath)) {
                writer.write(jsonString);
                LOGGER.info("All segments in memory have been written to json file");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public static void main(String[] args) {


    }
}
