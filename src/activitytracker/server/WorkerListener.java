package activitytracker.server;

import java.net.ServerSocket;
import java.util.ArrayList;

public class WorkerListener extends Thread {

    private ServerSocket serverSocket;
    private ArrayList<ClientHandlerThread> workerThreads;
    private int workerID;
    private boolean running;

    public WorkerListener(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {


    }

    public void startListening() {

    }

    public void stopListening() {

    }

}
