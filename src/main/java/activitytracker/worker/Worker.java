package main.java.activitytracker.worker;

import main.java.activitytracker.fileprocessing.gpx.Chunk;
import main.java.activitytracker.structures.FifoQueue;
import main.java.activitytracker.worker.Map.WorkerResult;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Worker {

    private final String ipAddress;

    private FifoQueue<Chunk> messageQueueIn;
    private final Object MSG_Q_IN_LOCK = new Object();

    private FifoQueue<WorkerResult> messageQueueOut;
    private final Object MSG_Q_OUT_LOCK = new Object();

    private final Socket socket;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public Worker(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        try {
            this.socket = new Socket(this.ipAddress, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.init();

    }

    public void init() {
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.messageQueueIn = new FifoQueue<>();
            this.messageQueueOut = new FifoQueue<>();

            beginProcessing();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void beginProcessing() {
        ChunkProcessorThread chunkProcessor = new ChunkProcessorThread(
                messageQueueIn, MSG_Q_IN_LOCK, messageQueueOut, MSG_Q_OUT_LOCK
        );
        chunkProcessor.start();

        ChunksListenerThread incomingChunksListener = new ChunksListenerThread(
                inputStream, messageQueueIn, MSG_Q_IN_LOCK
        );
        incomingChunksListener.start();

        ResultSenderThread resultSender = new ResultSenderThread(
                outputStream, messageQueueOut, MSG_Q_OUT_LOCK
        );
        resultSender.start();
    }

    public static void main(String[] args) {

        Worker worker = new Worker("127.0.0.1", 2345);

        System.out.println("Worker " + worker.ipAddress + " connected to server ");

    }
}
