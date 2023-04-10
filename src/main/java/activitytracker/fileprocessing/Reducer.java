package main.java.activitytracker.fileprocessing;

import java.io.Serializable;
import java.util.ArrayList;

public class Reducer {

    public static ReducedResult reduce(ArrayList<Mapper.WorkerResult> workerResults) {

        workerResults.sort(new Mapper.WorkerResultComparator());
        Mapper.WorkerResult currentChunkResults;
        Mapper.WorkerResult nextChunkResults;

        long totalTime = 0;
        double totalAscent = 0;
        double totalDistance = 0;

        for (int i = 1; i < workerResults.size(); i++) {
            currentChunkResults = workerResults.get(i - 1);
            nextChunkResults = workerResults.get(i);

            Mapper.ChunkTimeData currentChunkTimeData = currentChunkResults.value().chunkTimeData();
            Mapper.ChunkTimeData nextChunkTimeData = nextChunkResults.value().chunkTimeData();

            Mapper.ChunkAscentData currentChunkAscentData = currentChunkResults.value().chunkAscentData();
            Mapper.ChunkAscentData nextChunkAscentData = nextChunkResults.value().chunkAscentData();

            Mapper.ChunkDistanceData currentChunkDistanceData = currentChunkResults.value().chunkDistanceData();
            Mapper.ChunkDistanceData nextChunkDistanceData = nextChunkResults.value().chunkDistanceData();

            totalTime += currentChunkTimeData.chunkTime();
            totalTime += nextChunkTimeData.chunkTime();
            totalTime += (nextChunkTimeData.firstWaypointTime() - currentChunkTimeData.lastWaypointTime());

            totalAscent += currentChunkAscentData.ascent();
            totalAscent += nextChunkAscentData.ascent();
            totalAscent += Math.max(0, (nextChunkAscentData.firstWaypointElevation() - currentChunkAscentData.lastWaypointElevation()));

            totalDistance += currentChunkDistanceData.distance();
            totalDistance += nextChunkDistanceData.distance();
            totalDistance += Haversine.distance(
                    nextChunkDistanceData.firstWaypointLat(),
                    nextChunkDistanceData.firstWaypointLong(),
                    currentChunkDistanceData.lastWaypointLat(),
                    currentChunkDistanceData.lastWaypointLong()
            );
        }

        double averageSpeed = totalDistance / totalTime;

        return new ReducedResult(
                new Key(workerResults.get(0).key().gpxFileId()),
                new Value(totalDistance, totalAscent, totalTime, averageSpeed)
        );
    }


    public record ReducedResult(Key key, Value value) implements Serializable {
    }

    public record Key(int gpxFileId) implements Serializable {
    }

    public record Value(double totalDistance, double totalAscent, long totalTime,
                        double averageSpeed) implements Serializable {
    }
}
