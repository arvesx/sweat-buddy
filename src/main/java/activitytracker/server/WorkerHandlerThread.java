package main.java.activitytracker.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WorkerHandlerThread extends Thread {

    private Socket workerSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public WorkerHandlerThread(Socket workerSocket) {
        this.workerSocket = workerSocket;
    }

    @Override
    public void run() {
        try {
            this.outputStream = new ObjectOutputStream(workerSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(workerSocket.getInputStream());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
