package main.java.activitytracker.server;

import main.java.activitytracker.server.fileprocessing.Chunk;
import main.java.activitytracker.server.structures.FifoQueue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class WorkerHandlerThread extends Thread {

    private final Socket workerSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private final boolean running;
    private final FifoQueue<Chunk> messageQueue;

    public WorkerHandlerThread(Socket workerSocket) {
        this.workerSocket = workerSocket;
        messageQueue = new FifoQueue<>();
        running = true;
    }

    @Override
    public void run() {
        try {
            this.outputStream = new ObjectOutputStream(workerSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(workerSocket.getInputStream());

            while (running) {

                synchronized (this) {
                    while (messageQueue.isEmpty()) {
                        wait();
                    }
                    Chunk nextChunk = messageQueue.dequeue();
                    outputStream.writeObject(nextChunk);
                    outputStream.flush();
                }
            }


        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void assign(Chunk chunk) {
        messageQueue.enqueue(chunk);
        notify();
    }
}
