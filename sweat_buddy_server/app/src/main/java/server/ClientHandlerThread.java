package server;

import dependencies.mapper.Map;
import fileprocessing.ClientData;
import dependencies.fileprocessing.gpx.GpxFile;

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
            System.out.println();
            System.out.println("[Server] Client#" + this.clientData.getID() + ": Received GPX File");
            System.out.print(">>> ");

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
            System.out.println("GPX File ID: " + finalResults.key());
            System.out.println("Total Distance: " + finalResults.value().totalDistance());
            System.out.println("Total Ascent: " + finalResults.value().totalAscent());
            System.out.println("Total Time: " + finalResults.value().totalTime());
            System.out.println("Average Speed: " + finalResults.value().averageSpeed());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
