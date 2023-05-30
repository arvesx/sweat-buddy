package server;

import com.google.gson.Gson;
import dependencies.fileprocessing.TransmissionObject;
import dependencies.fileprocessing.TransmissionObjectType;
import dependencies.fileprocessing.gpx.GpxFile;
import dependencies.fileprocessing.gpx.GpxResults;
import dependencies.mapper.Map;
import dependencies.user.Route;
import dependencies.user.UserData;
import fileprocessing.ClientData;
import user.Authentication;
import user.userdata.DataExchangeHandler;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static server.Utils.*;


/**
 * This class is responsible to handle the connection between one client and the server. It gets instantiated from the
 * ClientListener every time a new client connects. The new object is put in a ArrayList for later management.
 * e.g. Access to the ClientData of each object.
 */
public class ClientHandlerThread extends Thread {

    private final Socket clientSocket;
    private final ClientData clientData;

    private boolean loggedIn;
    private UserData userData;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public ClientHandlerThread(Socket clientSocket, int clientId) {
        this.clientSocket = clientSocket;
        this.clientData = new ClientData(clientId);
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public ClientData getClientData() {
        return clientData;
    }

    private GpxResults handleGpxFile(InputStream gpxInputStream) throws InterruptedException {

        GpxFile gpxFile = new GpxFile(gpxInputStream);
        this.clientData.setGpxFile(gpxFile);

        LOGGER.info("Client#" + this.clientData.getID() + ": Received GPX File");

        synchronized (GXP_FILE_ID_LOCK) {
            gpxFileId++;
            gpxFile.setGpxFileId(gpxFileId);

            synchronized (INTERMEDIATE_RESULTS_LOCK) {
                intermediateResults.put(gpxFileId, new ArrayList<>());
            }
        }

        gpxFile.makeChunks();
        for (var chunk : gpxFile.getChunks()) {
            synchronized (MESSAGE_Q_LOCK) {
                messageQueue.enqueue(chunk);
                MESSAGE_Q_LOCK.notify();
            }
        }

        int gpxFileId = gpxFile.getGpxFileId();
        // Waiting to receive all intermediate results to perform aggregation and calculate the final result.
        ArrayList<Map.WorkerResult> processedResults;
        synchronized (INTERMEDIATE_RESULTS_LOCK) {
            int intermediateResultsListLength = intermediateResults.get(gpxFileId).size();
            int numberOfChunksInGpx = gpxFile.getChunks().size();
            while (intermediateResultsListLength != numberOfChunksInGpx) {
                INTERMEDIATE_RESULTS_LOCK.wait();
                intermediateResultsListLength = intermediateResults.get(gpxFile.getGpxFileId()).size();
                numberOfChunksInGpx = gpxFile.getChunks().size();
            }
            processedResults = intermediateResults.get(gpxFileId);
        }

        Reduce.ReducedResult finalResults = Reduce.reduce(processedResults);

        double totalDistanceInKilometers = finalResults.value().totalDistanceInKilometers();
        double totalAscentInMeters = finalResults.value().totalAscentInMeters();
        double totalTimeInMinutes = finalResults.value().totalTimeInMinutes();
        double averageSpeedInKilometersPerHour = finalResults.value().averageSpeedKilometerPerHour();
        double totalTimeInMillis = finalResults.value().totalTimeInMillis();


        LOGGER.debug("GPX File ID: " + finalResults.key() +
                ", Total Distance: " + totalDistanceInKilometers +
                ", Total Ascent: " + totalAscentInMeters +
                ", Total Time: " + totalTimeInMinutes +
                ", Average Speed: " + averageSpeedInKilometersPerHour + "\n");

        GpxResults gpxResults = new GpxResults(
                totalDistanceInKilometers,
                totalAscentInMeters,
                totalTimeInMinutes,
                averageSpeedInKilometersPerHour
        );
        return gpxResults;
    }

    @Override
    public void run() {
        try {
            loggedIn = false;
            this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(clientSocket.getInputStream());
            Gson gson = new Gson();

            Authentication auth = new Authentication();


            while (true) {

                String receivedJsonString = (String) this.inputStream.readObject();
                TransmissionObject receivedData = gson.fromJson(receivedJsonString, TransmissionObject.class);

                if (receivedData.type == TransmissionObjectType.LOGIN_MESSAGE) {
                    try {
                        int userId = auth.handleLoginProcess(receivedData.username, receivedData.password);
                        this.clientData.setUsername(receivedData.username);
                        loggedIn = true;
                        System.out.println("Login from " + receivedData.username + " successful.");
                        this.userData = DataExchangeHandler.getSpecificUserData(userId);
                        TransmissionObject to = new TransmissionObject();
                        to.type = TransmissionObjectType.USER_DATA;
                        to.userData = userData;
                        to.message = "Successful Login";
                        to.success = 1;
                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);

                    } catch (Exception e) {
                        TransmissionObject to = new TransmissionObject();
                        to.type = TransmissionObjectType.LOGIN_MESSAGE;
                        to.message = e.getMessage();
                        to.success = 0;
                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);
                    }
                }

                if (receivedData.type == TransmissionObjectType.REGISTRATION_MESSAGE) {
                    try {
                        int userId = auth.handleRegistration(receivedData.username, receivedData.password);
                        UserData newUserData = new UserData();
                        newUserData.routes = new ArrayList<>();
                        newUserData.segments = new ArrayList<>();
                        newUserData.userId = userId;
                        DataExchangeHandler.userData.add(newUserData);
                        DataExchangeHandler.writeAllUserDataToJson();

                        loggedIn = true;
                        userData = newUserData;

                        TransmissionObject to = new TransmissionObject();
                        to.type = TransmissionObjectType.USER_DATA;
                        to.userData = userData;
                        to.message = "Successful Registration";
                        to.success = 1;
                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);


                    } catch (Exception e) {
                        TransmissionObject to = new TransmissionObject();
                        to.type = TransmissionObjectType.REGISTRATION_MESSAGE;
                        to.message = e.getMessage();
                        to.success = 0;
                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);
                    }
                }
                if (loggedIn) {
                    if (receivedData.type == TransmissionObjectType.GPX_FILE) {
                        System.out.println("Received gpx file from " + this.clientData.getUsername());
                        GpxResults results = handleGpxFile(new ByteArrayInputStream(receivedData.gpxFile.getBytes()));

                        Route newRoute = new Route();
                        newRoute.routeName = receivedData.message;
                        newRoute.totalTimeInMinutes = results.totalTimeInMinutes();
                        newRoute.totalDistanceInKm = results.distanceInKilometers();
                        newRoute.totalElevationInM = results.totalAscentInMete();
                        newRoute.averageSpeedInKmH = results.avgSpeedInKilometersPerHour();

                        userData.routes.add(newRoute);

                        DataExchangeHandler.writeAllUserDataToJson();

                        TransmissionObject to = new TransmissionObject();
                        to.type = TransmissionObjectType.USER_DATA;
                        to.userData = userData;
                        to.gpxResults = results;

                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
