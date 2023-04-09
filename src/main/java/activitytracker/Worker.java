package main.java.activitytracker;

import main.java.activitytracker.fileprocessing.Chunk;
import main.java.activitytracker.fileprocessing.Haversine;
import main.java.activitytracker.structures.FifoQueue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Worker extends Node {

    private FifoQueue<Chunk> messageQueue;
    private Socket socket;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private final Object MSG_Q_LOCK = new Object();


    public Worker(String ip_address, int port) {
        super(ip_address, port);
        this.init();

    }

    public void init() {
        try {
            this.socket = new Socket(this.ipAddress, this.port);
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.messageQueue = new FifoQueue<>();
            this.inputStream = new ObjectInputStream(socket.getInputStream());

            beginProcessing();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void beginProcessing() {
        ChunkProcessor chunkProcessor = new ChunkProcessor();
        chunkProcessor.start();

        MessageQueueAdder messageQueueAdder = new MessageQueueAdder();
        messageQueueAdder.start();
    }

    public void connect() {

    }

    public void disconnect() {

    }


    private class ChunkProcessor extends Thread {
        @Override
        public void run() {
            while (true) {
                Chunk chunkToProcess;
                synchronized (MSG_Q_LOCK) {
                    while (messageQueue.isEmpty()) {
                        try {
                            MSG_Q_LOCK.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    chunkToProcess = messageQueue.dequeue();
                }

                // process the chunk
                System.out.println(chunkToProcess);
            }
        }
    }

    private class MessageQueueAdder extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Chunk chunk = (Chunk) inputStream.readObject();
                    synchronized (MSG_Q_LOCK) {
                        messageQueue.enqueue(chunk);
                        MSG_Q_LOCK.notify();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public record ChunkDistanceData(double distance, double firstWaypointLong, double firstWaypointLat,
                                    double lastWaypointLong, double lastWaypointLat) {
    }


    public record Key(int gpxFileId, int chunkId) {
    }

    public static ChunkDistanceData calculateDistance(Chunk chunk) {

        ArrayList<Waypoint> waypoints = chunk.getWaypoints();

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

        return new ChunkDistanceData(distance * 1000,
                waypoints.get(0).getLongitude(),
                waypoints.get(0).getLatitude(),
                waypoints.get(waypoints.size() - 1).getLongitude(),
                waypoints.get(waypoints.size() - 1).getLatitude());
    }


    public record ChunkAscentData(double ascent, double firstWaypointElevation, double lastWaypointElevation) {
    }

    public static ChunkAscentData calculateAscent(Chunk chunk) {

        // Total ascent = Σ(max(0, Elevation[i] - Elevation[i-1]))
        ArrayList<Waypoint> waypoints = chunk.getWaypoints();

        if (waypoints.size() < 2) {
            return new ChunkAscentData(0, waypoints.get(0).getElevation(), Double.MIN_VALUE);
        }

        double sum = 0;
        for (var i = 1; i < waypoints.size(); i++) {
            sum += Math.max(0, waypoints.get(i).getElevation() - waypoints.get(i - 1).getElevation());
        }

        return new ChunkAscentData(sum, waypoints.get(0).getElevation(), waypoints.get(waypoints.size() - 1).getElevation());
    }


    public record ChunkTimeData(long chunkTime, long firstWaypointTime, long lastWaypointTime) {
    }

    public static ChunkTimeData calculateTime(Chunk chunk) {

        ArrayList<Waypoint> waypoints = chunk.getWaypoints();

        if (waypoints.size() <= 1) {
            return new ChunkTimeData(0, waypoints.get(0).getTime(), Long.MIN_VALUE);
        }

        long totalTime = 0;
        for (int i = 1; i < waypoints.size(); i++) {
            totalTime += waypoints.get(i).getTime() - waypoints.get(i - 1).getTime();
        }

        return new ChunkTimeData(totalTime / 1000, waypoints.get(0).getTime(), waypoints.get(waypoints.size() - 1).getTime());
    }

    public static void main(String[] args) {

        Worker worker = new Worker("127.0.0.1", 2345);

        System.out.println("Worker " + worker.ipAddress + " connected to server ");

    }
}
