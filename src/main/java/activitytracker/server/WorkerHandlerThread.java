package main.java.activitytracker.server;

import main.java.activitytracker.fileprocessing.Chunk;
import main.java.activitytracker.fileprocessing.Mapper;
import main.java.activitytracker.structures.FifoQueue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static main.java.activitytracker.server.Utilities.INTERMEDIATE_RESULTS_LOCK;
import static main.java.activitytracker.server.Utilities.intermediateResults;

public class WorkerHandlerThread extends Thread {

    private final Socket workerSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private boolean running;
    private boolean receivingResults;
    private final FifoQueue<Chunk> messageQueue;
    private final Object MSG_Q_LOCK = new Object();

    public WorkerHandlerThread(Socket workerSocket) {
        this.workerSocket = workerSocket;
        messageQueue = new FifoQueue<>();
        running = true;
        receivingResults = true;
    }

    @Override
    public void run() {
        try {
            this.outputStream = new ObjectOutputStream(workerSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(workerSocket.getInputStream());

            SenderThread sender = new SenderThread();
            sender.start();

            ResultReceiverThread receiverThread = new ResultReceiverThread();
            receiverThread.start();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private class ResultReceiverThread extends Thread {
        @Override
        public void run() {
            while (receivingResults) {
                try {
                    Mapper.WorkerResult workerResult = (Mapper.WorkerResult) inputStream.readObject();
                    int comingFromGpxFileId = workerResult.key().gpxFileId();
                    synchronized (INTERMEDIATE_RESULTS_LOCK) {
                        intermediateResults.get(comingFromGpxFileId).add(workerResult);
                        INTERMEDIATE_RESULTS_LOCK.notifyAll();
                    }

                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private class SenderThread extends Thread {

        @Override
        public void run() {
            while (running) {

                synchronized (MSG_Q_LOCK) {
                    while (messageQueue.isEmpty()) {
                        try {
                            MSG_Q_LOCK.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Chunk nextChunk = messageQueue.dequeue();
                    try {
                        outputStream.writeObject(nextChunk);
                        outputStream.flush();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }

    public void assign(Chunk chunk) {
        synchronized (MSG_Q_LOCK) {
            messageQueue.enqueue(chunk);
            MSG_Q_LOCK.notify();
        }

    }
}
