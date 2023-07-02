package server;

import com.google.gson.Gson;
import dependencies.fileprocessing.TransmissionObject;
import dependencies.fileprocessing.TransmissionObjectBuilder;
import dependencies.fileprocessing.TransmissionObjectType;
import dependencies.fileprocessing.gpx.GpxFile;
import dependencies.fileprocessing.gpx.GpxResults;
import dependencies.fileprocessing.gpx.WaypointImpl;
import dependencies.mapper.Map;
import dependencies.user.*;
import fileprocessing.ClientData;
import user.Authentication;
import user.segments.SegmentsHandler;
import user.userdata.DataExchangeHandler;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static server.Utils.*;
import static user.userdata.DataExchangeHandler.populateGenericStatsObject;


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

    private GpxResults analyzeGpxFile(GpxFile gpxFile) throws InterruptedException {

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
        long totalTimeInMillis = finalResults.value().totalTimeInMillis();


        LOGGER.debug("GPX File ID: " + finalResults.key() +
                ", Total Distance: " + totalDistanceInKilometers +
                ", Total Ascent: " + totalAscentInMeters +
                ", Total Time: " + totalTimeInMinutes +
                ", Average Speed: " + averageSpeedInKilometersPerHour + "\n");


        return new GpxResults(
                totalDistanceInKilometers,
                totalAscentInMeters,
                totalTimeInMinutes,
                averageSpeedInKilometersPerHour,
                totalTimeInMillis
        );
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
                        // See if user exists
                        int userId = auth.handleLoginProcess(receivedData.username, receivedData.password);
                        this.clientData.setUsername(receivedData.username);

                        // if so, log him in
                        loggedIn = true;
                        System.out.println("Login from " + receivedData.username + " successful.");

                        // fetch his user data
                        userData = DataExchangeHandler.getSpecificUserData(userId);

                        // craft a transmission object to send a success message including his data
                        TransmissionObject to = new TransmissionObjectBuilder()
                                .type(TransmissionObjectType.USER_DATA)
                                .userData(userData)
                                .message("Successful login")
                                .success(1)
                                .craft();

                        // send back to client success message and his user data
                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);

                    } catch (Exception e) {
                        handleAuthFailure(TransmissionObjectType.LOGIN_MESSAGE, e.getMessage(), gson);
                    }
                }

                if (receivedData.type == TransmissionObjectType.LEADERBOARD) {
                    ArrayList<LeaderboardEntry> leaderboard = DataExchangeHandler.fetchGenericLeaderboard();
                    try {
                        TransmissionObject to = new TransmissionObjectBuilder()
                                .type(TransmissionObjectType.LEADERBOARD)
                                .leaderboardList(leaderboard)
                                .message("Leaderboard")
                                .success(1)
                                .craft();

                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);
                    } catch (Exception e) {
                        /*TODO*/
                    }

                }


                if (receivedData.type == TransmissionObjectType.REGISTRATION_MESSAGE) {
                    try {
                        int userId = auth.handleRegistration(receivedData.username, receivedData.password);
                        UserData newUserData = handleNewAccUserData(userId);
                        newUserData.username = receivedData.username;
                        DataExchangeHandler.userData.add(newUserData);
                        DataExchangeHandler.writeAllUserDataToJson();

                        loggedIn = true;
                        userData = newUserData;
                        userData.username = receivedData.username;

                        TransmissionObject to = new TransmissionObjectBuilder()
                                .type(TransmissionObjectType.USER_DATA)
                                .userData(userData)
                                .message("Successful Registration")
                                .success(1)
                                .craft();

                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);

                    } catch (Exception e) {
                        handleAuthFailure(TransmissionObjectType.REGISTRATION_MESSAGE, e.getMessage(), gson);
                    }
                }
                if (loggedIn) {
                    if (receivedData.type == TransmissionObjectType.LOGOUT_REQUEST) {
                        TransmissionObject to = new TransmissionObjectBuilder()
                                .type(TransmissionObjectType.LOGOUT_REQUEST)
                                .message("logging out")
                                .success(1)
                                .craft();

                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);
                        loggedIn = false;
                        System.out.println("Logged out.");
                        
                    }

                    if (receivedData.type == TransmissionObjectType.GPX_FILE) {
                        System.out.println("Received gpx file from " + this.clientData.getUsername());
                        GpxFile gpxFile = new GpxFile(new ByteArrayInputStream(receivedData.gpxFile.getBytes()));

                        GpxResults results = analyzeGpxFile(gpxFile);

                        Route newRoute = processGpxResults(results, receivedData);
                        newRoute.routeWaypoints = gpxFile.getWps();
                        newRoute.routeId = userData.routes.size();

                        userData.routes.add(newRoute);
                        userData.points += newRoute.points;
                        userData.routesDoneThisMonth++;
                        userData.totalKmThisMonth += results.distanceInKilometers();
                        userData.totalElevation += results.totalAscentInMete();
                        userData.totalTime += results.totalTimeInMillis();

                        // Check if new route contains any existing segment. If so, add it to user
                        synchronized (SegmentsHandler.ALL_SEGMENTS_LIST_LOCK) {
                            if (SegmentsHandler.allSegments == null) {
                                SegmentsHandler.allSegments = new ArrayList<>();
                            }
                            for (var seg : SegmentsHandler.allSegments) {
                                ArrayList<WaypointImpl> sublist;
                                if ((sublist = SegmentsHandler.isSegmentPresentInRoute(seg.waypoints, newRoute.routeWaypoints)) != null) {
                                    GpxResults segmentResults = this.analyzeGpxFile(new GpxFile(sublist));
                                    this.createSegmentAttemptForUser(userData, segmentResults, seg.segmentName, sublist, seg.segmentId);
                                    SegmentsHandler.updateLeaderboardOfSegment(seg);
                                }
                            }
                        }

                        DataExchangeHandler.updateAverageMetrics();

                        DataExchangeHandler.writeAllUserDataToJson();

                        TransmissionObject to = new TransmissionObjectBuilder()
                                .type(TransmissionObjectType.USER_DATA)
                                .userData(userData)
                                .message("New GPX file has been processed.")
                                .success(1)
                                .craft();

                        to.gpxResults = results;

                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);
                    }

                    if (receivedData.type == TransmissionObjectType.SEGMENT_LEADERBOARD) {

                        try {
                            TransmissionObject to = new TransmissionObjectBuilder()
                                    .type(TransmissionObjectType.SEGMENT_LEADERBOARD)
                                    .segmentObject(SegmentsHandler.allSegments.get(receivedData.segmentId))
                                    .message("Segment Object")
                                    .success(1)
                                    .craft();

                            String jsonTransmissionObject = gson.toJson(to);
                            outputStream.writeObject(jsonTransmissionObject);
                        } catch (Exception e) {
                            /*TODO*/
                        }
                    }

                    // If a user requests to see his stats compared to average population stats, he makes
                    // a separate request to receive fresh and updated data.
                    if (receivedData.type == TransmissionObjectType.GENERIC_STATS_REQUEST) {

                        GenericStats genericStatsObject = new GenericStats();
                        genericStatsObject.totalDistance = userData.totalKmThisMonth;
                        genericStatsObject.totalElevation = userData.totalElevation;
                        genericStatsObject.totalTime = userData.totalTime;
                        populateGenericStatsObject(genericStatsObject);

                        TransmissionObject to = new TransmissionObjectBuilder()
                                .type(TransmissionObjectType.GENERIC_STATS_REQUEST)
                                .genericStats(genericStatsObject)
                                .message("Request Successful")
                                .success(1)
                                .craft();

                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);
                    }
                    // If a user requests a new state of his data
                    if (receivedData.type == TransmissionObjectType.USER_DATA) {
                        TransmissionObject to = new TransmissionObjectBuilder()
                                .type(TransmissionObjectType.USER_DATA)
                                .userData(userData)
                                .message("New state of user data")
                                .success(1)
                                .craft();

                        String jsonTransmissionObject = gson.toJson(to);
                        outputStream.writeObject(jsonTransmissionObject);
                    }

                    // if we receive a new segment
                    if (receivedData.type == TransmissionObjectType.SEGMENT) {

                        var sublist = this.userData.routes
                                .get(receivedData.routeId)
                                .routeWaypoints
                                .subList(receivedData.segmentStart, receivedData.segmentEnd);

                        ArrayList<WaypointImpl> segmentWaypoints = new ArrayList<>(sublist);

                        GpxFile gpxFile = new GpxFile(
                                segmentWaypoints
                        );

                        GpxResults gpxResults = analyzeGpxFile(gpxFile);

                        Segment segment = new Segment();
                        segment.leaderboard = new ArrayList<>();
                        segment.segmentName = receivedData.message;
                        segment.waypoints = gpxFile.getWps();
                        synchronized (SegmentsHandler.ALL_SEGMENTS_LIST_LOCK) {
                            segment.segmentId = SegmentsHandler.allSegments.size();
                            SegmentsHandler.allSegments.add(segment);
                        }

                        createSegmentAttemptForUser(
                                this.userData, gpxResults, receivedData.message, gpxFile.getWps(), segment.segmentId);

                        // because we have introduced a new segment to our system
                        checkEveryUserIfTheyCompletedSegment(userData.userId, segment);


                        DataExchangeHandler.writeAllUserDataToJson();
                        TransmissionObject to = new TransmissionObjectBuilder()
                                .type(TransmissionObjectType.USER_DATA)
                                .userData(userData)
                                .gpxResults(gpxResults)
                                .message("Segment has been processed.")
                                .success(1)
                                .craft();


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

    public static int calculateRoutePoints(GpxResults results) {
        return (int) results.totalAscentInMete() * 10 + (int) results.distanceInKilometers() * 100;
    }

    private UserData handleNewAccUserData(int userId) {
        UserData newUserData = new UserData();
        newUserData.routes = new ArrayList<>();
        newUserData.segments = new ArrayList<>();
        newUserData.userId = userId;
        return newUserData;
    }

    private void handleAuthFailure(TransmissionObjectType type, String message, Gson gson) throws IOException {
        TransmissionObject to = new TransmissionObject();
        to.type = type;
        to.message = message;
        to.success = 0;
        String jsonTransmissionObject = gson.toJson(to);
        outputStream.writeObject(jsonTransmissionObject);
    }

    public Route processGpxResults(GpxResults results, TransmissionObject receivedData) {
        Route newRoute = new Route();
        newRoute.routeName = receivedData.message;
        newRoute.coordinates = receivedData.coordinates;
        newRoute.totalTimeInMinutes = results.totalTimeInMinutes();
        newRoute.totalTimeInMillis = results.totalTimeInMillis();
        newRoute.totalDistanceInKm = results.distanceInKilometers();
        newRoute.totalElevationInM = results.totalAscentInMete();
        newRoute.averageSpeedInKmH = results.avgSpeedInKilometersPerHour();
        newRoute.routeType = getRouteType(results);
        newRoute.points = calculateRoutePoints(results);

        return newRoute;
    }

    public static int getRouteType(GpxResults results) {
        var ascent = results.totalAscentInMete();
        if (ascent > 200) return 0;
        var speed = results.avgSpeedInKilometersPerHour();
        if (speed > 6.4) return 2;

        return 1;

    }

    public void checkEveryUserIfTheyCompletedSegment(int ignoreUserId, Segment segment) {
        new Thread(() -> {

            for (var userData : DataExchangeHandler.userData) {
                if (userData.userId == ignoreUserId) continue;

                try {
                    hasUserCompletedSegment(userData, segment);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            SegmentsHandler.updateLeaderboardOfSegment(segment);
        }).start();
    }


    public void hasUserCompletedSegment(
            UserData userData, Segment segment) throws InterruptedException {

        for (var route : userData.routes) {
            ArrayList<WaypointImpl> sublist;
            if (route == null) continue;
            if (route.routeWaypoints == null) continue;
            if ((sublist = SegmentsHandler.isSegmentPresentInRoute(segment.waypoints, route.routeWaypoints)) != null) {

                GpxResults results = this.analyzeGpxFile(new GpxFile(sublist));
                createSegmentAttemptForUser(userData, results, segment.segmentName, sublist, segment.segmentId);
            }
        }
    }

    private void createSegmentAttemptForUser(UserData userData, GpxResults results, String segmentName, ArrayList<WaypointImpl> sublist, int segId) {
        SegmentAttempt segmentAttempt = new SegmentAttempt();
        segmentAttempt.segmentName = segmentName;

        segmentAttempt.segmentId = segId;

        segmentAttempt.waypoints = sublist;
        segmentAttempt.totalTime = results.totalTimeInMillis();
        segmentAttempt.totalDistance = (float) results.distanceInKilometers();
        segmentAttempt.avgSpeed = (float) results.avgSpeedInKilometersPerHour();
        segmentAttempt.elevation = (float) results.totalAscentInMete();

        if (userData.segments == null) {
            userData.segments = new ArrayList<>();
        }

        userData.segments.add(segmentAttempt);
    }

}

