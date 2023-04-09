package main.java.activitytracker;

import main.java.activitytracker.server.fileprocessing.Chunk;
import main.java.activitytracker.server.structures.FifoQueue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

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

    public static void main(String[] args) {

        Worker worker = new Worker("127.0.0.1", 2345);

        System.out.println("Worker " + worker.ipAddress + " connected to server ");

    }
}
