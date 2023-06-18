package user.segments;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dependencies.fileprocessing.gpx.WaypointImpl;
import dependencies.user.Segment;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static server.Utils.LOGGER;

public class SegmentsHandler {

    public static ArrayList<Segment> allSegments = new ArrayList<>();

    private static final Object ALL_SEGMENTS_LIST_LOCK = new Object();

    private static final String segmentsFilePath = "src/main/java/user/segments/segments_db.json";

    public static boolean isSegmentPresentInRoute(ArrayList<WaypointImpl> segmentWps, ArrayList<WaypointImpl> routeWps) {
        return false;
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
}
