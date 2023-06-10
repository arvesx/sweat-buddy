package user.userdata;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dependencies.user.UserData;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class DataExchangeHandler {
    public static ArrayList<UserData> userData = new ArrayList<>();
    public static final Object USER_DATA_LOCK = new Object();
    private static final String filePath = "src/main/java/user/userdata/userdata_db.json";

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
