package user.userdata;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dependencies.fileprocessing.gpx.GpxResults;
import dependencies.user.*;
import server.Utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import static server.Utils.LOGGER;

public class DataExchangeHandler {
    public static ArrayList<UserData> userData = new ArrayList<>();
    public static final Object USER_DATA_LOCK = new Object();
    private static final String filePath = "src/main/java/user/userdata/userdata_db.json";

    public static GenericData genericData;
    public static final Object GENERIC_DATA_LOCK = new Object();

    private static final String genericDataFilePath = "src/main/java/user/userdata/genericdata_db.json";

    public static ArrayList<LeaderboardEntry> fetchGenericLeaderboard() {
        ArrayList<LeaderboardEntry> leaderboard = new ArrayList<>();
        
        synchronized (USER_DATA_LOCK)
        {
            for (var i : userData) {
                leaderboard.add(new LeaderboardEntry(i.userId, i.username, i.points));
            }
            leaderboard.sort(new LeaderboardEntryComparator());
            return leaderboard;
        }
        
    }

    public static void readGenericData() {
        Gson gson = new Gson();
        synchronized (GENERIC_DATA_LOCK) {
            try (FileReader fileReader = new FileReader(genericDataFilePath)) {
                genericData = gson.fromJson(fileReader, new TypeToken<GenericData>() {
                }.getType());
                LOGGER.info("Generic data have been read from file");
                LOGGER.info("Avg total distance: " + genericData.avgTotalDistance + ", Avg total elevation: " +
                        genericData.avgElevation + ", Avg total time: " + genericData.avgTotalTime);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void updateAverageMetrics() {
        synchronized (USER_DATA_LOCK) {
            if (genericData == null) genericData = new GenericData();
            genericData.avgTotalDistance = calculateNewAvgTotalDistance();
            genericData.avgElevation = calculateNewAvgTotalAscent();
            genericData.avgTotalTime = calculateNewAvgTotalTime();
        }
        LOGGER.info("New avg total distance: " + genericData.avgTotalDistance + ", New avg total elevation: " +
                genericData.avgElevation + ", New avg total time: " + genericData.avgTotalTime);
        writeGenericData();
    }

    public static void populateGenericStatsObject(GenericStats obj) {

        obj.avgTotalDistance = genericData.avgTotalDistance;
        obj.avgTotalElevation = genericData.avgElevation;
        obj.avgTotalTime = genericData.avgTotalTime;

    }

    private static long calculateNewAvgTotalTime() {
        long totalTime = 0;
        for (var user : userData) {
            totalTime += user.totalTime;
        }
        return totalTime / userData.size();
    }


    private static float calculateNewAvgTotalAscent() {
        float totalAscent = 0;
        for (var user : userData) {
            totalAscent += user.totalElevation;
        }
        return totalAscent / userData.size();
    }

    private static float calculateNewAvgTotalDistance() {
        float totalKm = 0;
        for (var user : userData) {
            totalKm += user.totalKmThisMonth;
        }
        return totalKm / userData.size();
    }


    public static void writeGenericData() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(genericData);
        synchronized (GENERIC_DATA_LOCK) {
            try (FileWriter writer = new FileWriter(genericDataFilePath)) {
                writer.write(jsonString);
                LOGGER.info("Generic data file has been updated with new data");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void writeAllUserDataToJson() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(userData);
        synchronized (USER_DATA_LOCK) {
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(jsonString);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void readAllUserDataFromJson() {
        Gson gson = new Gson();
        synchronized (USER_DATA_LOCK) {
            try (FileReader fileReader = new FileReader(filePath)) {
                userData = gson.fromJson(fileReader, new TypeToken<ArrayList<UserData>>() {
                }.getType());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (userData == null) userData = new ArrayList<>();
        }
    }

    public static UserData getSpecificUserData(int userId) {
        for (var i : userData) {
            if (i.userId == userId) return i;
        }
        throw new NoSuchElementException("User data with userId" + userId + " do not exist.");
    }
}
