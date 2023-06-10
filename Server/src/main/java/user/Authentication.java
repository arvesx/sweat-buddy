package user;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Authentication {

    public static final Object ALL_USERS_LIST_LOCK = new Object();
    public static ArrayList<UserCredentials> allUsers = new ArrayList<>();

    private static final String filePath = "src/main/java/user/file.json";

    public int handleLoginProcess(String username, String password) throws Exception {
        if (allUsers == null) allUsers = new ArrayList<>();

        UserCredentials userCredentials = null;
        for (var i : allUsers) {
            if (username.equals(i.username)) {
                userCredentials = i;
                break;
            }
        }

        if (userCredentials == null) throw new Exception("Incorrect Username");

        if (!hashPassword(password).equals(userCredentials.hashedPassword)) throw new Exception("Incorrect Password");

        return userCredentials.userId;
    }

    public int handleRegistration(String username, String password) throws Exception {

        if (allUsers == null) allUsers = new ArrayList<>();

        if (isUsernameAlreadyRegistered(username)) {
            throw new Exception("Username has already been registered.");
        }

        if (isNotValidPassword(password)) {
            throw new Exception("Invalid password");
        }

        String hashedPassword = hashPassword(password);

        UserCredentials newUserCredentials = new UserCredentials(
                fetchNextId(),
                username,
                hashedPassword
        );

        synchronized (ALL_USERS_LIST_LOCK) {
            allUsers.add(newUserCredentials);
            writeAllUserCredentialsToJson();
        }
        return newUserCredentials.userId;
    }

    public static void writeAllUserCredentialsToJson() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(allUsers);


        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(jsonString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void readAllUserCredentialsFromJson() {
        Gson gson = new Gson();

        try (FileReader fileReader = new FileReader(filePath)) {
            allUsers = gson.fromJson(fileReader, new TypeToken<ArrayList<UserCredentials>>() {
            }.getType());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String hashPassword(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // Compute the hash value
        byte[] hashBytes = digest.digest(password.getBytes());

        // Convert the byte array to hexadecimal representation
        StringBuilder hexString = new StringBuilder();
        for (byte hashByte : hashBytes) {
            String hex = Integer.toHexString(0xff & hashByte);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public int fetchNextId() {
        synchronized (ALL_USERS_LIST_LOCK) {
            if (allUsers.isEmpty()) return 1;
            return allUsers.get(allUsers.size() - 1).userId + 1;
        }

    }

    public static boolean isNotValidPassword(String password) {
        return password.isEmpty();
    }

    public static boolean isUsernameAlreadyRegistered(String username) {
        synchronized (ALL_USERS_LIST_LOCK) {
            for (var i : allUsers) {
                if (i.username.equals(username)) return true;
            }
            return false;
        }
    }
}
