package dependencies.mapper;

import dependencies.fileprocessing.gpx.Chunk;
import dependencies.fileprocessing.gpx.WaypointImpl;
import dependencies.fileprocessing.distance.Haversine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class Map {

    public static WorkerResult map(Chunk chunk) {
        return new WorkerResult(
                new Key(chunk.getGpxFileId(), chunk.getChunkId()),
                new Value(
                        calculateDistance(chunk),
                        calculateAscent(chunk),
                        calculateTime(chunk)
                )
        );
    }


    public record WorkerResult(Key key, Value value) implements Serializable {
    }

    public static class WorkerResultComparator implements Comparator<WorkerResult> {

        @Override
        public int compare(WorkerResult o1, WorkerResult o2) {
            return Integer.compare(o1.key().chunkId(), o2.key().chunkId());
        }
    }

    public record Key(int gpxFileId, int chunkId) implements Serializable {
    }

    public record Value(ChunkDistanceData chunkDistanceData,
                        ChunkAscentData chunkAscentData,
                        ChunkTimeData chunkTimeData) implements Serializable {
    }

    public record ChunkDistanceData(double distance, double firstWaypointLong, double firstWaypointLat,
                                    double lastWaypointLong, double lastWaypointLat) implements Serializable {
    }


    public static ChunkDistanceData calculateDistance(Chunk chunk) {

        ArrayList<WaypointImpl> waypoints = chunk.getWaypoints();

        if (waypoints.size() <= 1) {
            return new ChunkDistanceData(0,
                    waypoints.get(0).getLongitude(),
                    waypoints.get(0).getLatitude(),
                    Double.MIN_VALUE,
                    Double.MIN_VALUE);
        }

        double distance = 0;
        for (int i = 1; i < waypoints.size(); i++) {
            distance += Haversine.distance(waypoints.get(i - 1).getLatitude(),
                    waypoints.get(i - 1).getLongitude(),
                    waypoints.get(i).getLatitude(),
                    waypoints.get(i).getLongitude());
        }

        return new ChunkDistanceData(distance,
                waypoints.get(0).getLongitude(),
                waypoints.get(0).getLatitude(),
                waypoints.get(waypoints.size() - 1).getLongitude(),
                waypoints.get(waypoints.size() - 1).getLatitude());
    }


    public record ChunkAscentData(double ascent, double firstWaypointElevation,
                                  double lastWaypointElevation) implements Serializable {
    }

    public static ChunkAscentData calculateAscent(Chunk chunk) {

        // Total ascent = Σ(max(0, Elevation[i] - Elevation[i-1]))
        ArrayList<WaypointImpl> waypoints = chunk.getWaypoints();

        if (waypoints.size() < 2) {
            return new ChunkAscentData(0, waypoints.get(0).getElevation(), Double.MIN_VALUE);
        }

        double sum = 0;
        for (var i = 1; i < waypoints.size(); i++) {
            sum += Math.max(0, waypoints.get(i).getElevation() - waypoints.get(i - 1).getElevation());
        }

        return new ChunkAscentData(sum, waypoints.get(0).getElevation(), waypoints.get(waypoints.size() - 1).getElevation());
    }


    public record ChunkTimeData(long chunkTime, long firstWaypointTime, long lastWaypointTime) implements Serializable {
    }

    public static ChunkTimeData calculateTime(Chunk chunk) {

        ArrayList<WaypointImpl> waypoints = chunk.getWaypoints();

        if (waypoints.size() <= 1) {
            return new ChunkTimeData(0, waypoints.get(0).getTime(), Long.MIN_VALUE);
        }

        long totalTime = 0;
        for (int i = 1; i < waypoints.size(); i++) {
            totalTime += waypoints.get(i).getTime() - waypoints.get(i - 1).getTime();
        }

        return new ChunkTimeData(totalTime, waypoints.get(0).getTime(), waypoints.get(waypoints.size() - 1).getTime());
    }
}
