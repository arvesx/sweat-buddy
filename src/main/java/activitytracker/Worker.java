package main.java.activitytracker;

import main.java.activitytracker.fileprocessing.Chunk;
import main.java.activitytracker.fileprocessing.Mapper;
import main.java.activitytracker.structures.FifoQueue;
import main.java.activitytracker.fileprocessing.Mapper.WorkerResult;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Worker extends Node {

    private FifoQueue<Chunk> messageQueueIn;
    private final Object MSG_Q_IN_LOCK = new Object();

    private FifoQueue<WorkerResult> messageQueueOut;
    private final Object MSG_Q_OUT_LOCK = new Object();

    private Socket socket;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;


    private boolean listeningForIncomingChunks;
    private boolean sendingResultsBack;
    private boolean processingChunks;

    public Worker(String ip_address, int port) {
        super(ip_address, port);
        this.init();

    }

    public void init() {
        try {
            this.socket = new Socket(this.ipAddress, this.port);
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
        processingChunks = true;
        ChunkProcessor chunkProcessor = new ChunkProcessor();
        chunkProcessor.start();

        listeningForIncomingChunks = true;
        IncomingChunksListener incomingChunksListener = new IncomingChunksListener();
        incomingChunksListener.start();

        sendingResultsBack = true;
        ResultSender resultSender = new ResultSender();
        resultSender.start();
    }

    public void connect() {

    }

    public void disconnect() {

    }


    private class ChunkProcessor extends Thread {
        @Override
        public void run() {
            while (processingChunks) {
                Chunk chunkToProcess;
                synchronized (MSG_Q_IN_LOCK) {
                    while (messageQueueIn.isEmpty()) {
                        try {
                            MSG_Q_IN_LOCK.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    chunkToProcess = messageQueueIn.dequeue();
                }

                // process the chunk
                System.out.println(chunkToProcess);
                WorkerResult result = Mapper.map(chunkToProcess);
                synchronized (MSG_Q_OUT_LOCK) {
                    messageQueueOut.enqueue(result);
                    MSG_Q_OUT_LOCK.notify();
                }
            }
        }
    }

    private class IncomingChunksListener extends Thread {
        @Override
        public void run() {
            while (listeningForIncomingChunks) {
                try {
                    Chunk chunk = (Chunk) inputStream.readObject();
                    synchronized (MSG_Q_IN_LOCK) {
                        messageQueueIn.enqueue(chunk);
                        MSG_Q_IN_LOCK.notify();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    private class ResultSender extends Thread {
        @Override
        public void run() {
            while (sendingResultsBack) {
                synchronized (MSG_Q_OUT_LOCK) {
                    while (messageQueueOut.isEmpty()) {
                        try {
                            MSG_Q_OUT_LOCK.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    WorkerResult resultToSendBack = messageQueueOut.dequeue();
                    try {
                        outputStream.writeObject(resultToSendBack);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public static void main(String[] args) {

        Worker worker = new Worker("127.0.0.1", 2345);

        System.out.println("Worker " + worker.ipAddress + " connected to server ");

    }
}
