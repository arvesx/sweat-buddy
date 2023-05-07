package server;

import dependencies.fileprocessing.gpx.GpxFile;
import dependencies.fileprocessing.gpx.GpxResults;
import dependencies.mapper.Map;
import fileprocessing.ClientData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    @Override
    public void run() {
        try {

            this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(clientSocket.getInputStream());

            String username;
            GpxFile gpxFile;

            username = (String) this.inputStream.readObject();
            this.clientData.setUsername(username);

            gpxFile = (GpxFile) this.inputStream.readObject();
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

            outputStream.writeObject(gpxResults);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
