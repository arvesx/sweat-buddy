package main.java.activitytracker.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static main.java.activitytracker.server.Utilities.*;

public class WorkerListener extends Thread {

    private final ServerSocket serverSocket;
    private final ArrayList<WorkerHandlerThread> workerThreads;
    private boolean running;

    public WorkerListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        workerThreads = new ArrayList<>();
    }

    public ArrayList<WorkerHandlerThread> getWorkerThreads() {
        synchronized (WORKERS_LIST_LOCK) {
            return workerThreads;
        }
    }

    @Override
    public void run() {
        try {
            startListening();
            while (running) {
                // Accept worker connection
                Socket workerSocket = this.serverSocket.accept();
                System.out.println("[Server] Worker node added");

                // Handle the new connection
                WorkerHandlerThread workerThread = new WorkerHandlerThread(workerSocket);
                workerThread.start();
                synchronized (WORKERS_LIST_LOCK) {
                    workerThreads.add(workerThread);
                }

                synchronized (WORKERS_RING_BUFFER_LOCK) {
                    workersRingBuffer.add(workerThread);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startListening() {
        this.running = true;
    }

    public void stopListening() {
        System.out.println("[Server] Shutting down worker listener");
        this.running = false;
    }

}
