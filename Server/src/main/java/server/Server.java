package server;

import dependencies.Utilities;
import dependencies.fileprocessing.gpx.Chunk;
import user.Authentication;
import user.segments.SegmentsHandler;
import user.userdata.DataExchangeHandler;

import static server.Utils.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;


public class Server {

    private volatile boolean serverRunning;
    private volatile boolean distributing;
    private ServerSocket clientServerSocket;
    private ServerSocket workerServerSocket;

    private final int clientListenerPort;
    private final int workerListenerPort;

    private ClientListener clientListener;
    private WorkerListener workerListener;

    private boolean listensForClients;
    private boolean listensForWorkers;

    protected void init() {
        try {
            this.clientServerSocket = new ServerSocket(this.clientListenerPort);
            this.workerServerSocket = new ServerSocket(this.workerListenerPort);
            serverRunning = true;
            distributing = true;

            LOGGER.info("Starting Server...");
            Authentication.readAllUserCredentialsFromJson();
            DataExchangeHandler.readAllUserDataFromJson();
            DataExchangeHandler.readGenericData();
            SegmentsHandler.readAllSegments();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void connect() throws InterruptedException {

        LOGGER.info("Listening for new clients on port " + this.clientListenerPort);
        this.clientListener = new ClientListener(this.clientServerSocket);
        clientListener.start();
        this.listensForClients = true;


        LOGGER.info("Listening for new workers on port " + this.workerListenerPort);
        this.workerListener = new WorkerListener(this.workerServerSocket);
        workerListener.start();
        this.listensForWorkers = true;

        this.startDistributing();

        Scanner scanner = new Scanner(System.in);
        while (serverRunning) {
            System.out.print(">>> ");
            String command = scanner.nextLine();
            if (command.equals("?disconnect")) {
                serverRunning = false;
                distributing = false;
                disconnect();
            }
        }
        scanner.close();

    }

    private void startDistributing() throws InterruptedException {

        while (distributing) {
            Chunk chunk = getNextChunk();
            WorkerHandlerThread thread;
            synchronized (WORKERS_RING_BUFFER_LOCK) {
                while (workersRingBuffer.isEmpty()) {
                    WORKERS_RING_BUFFER_LOCK.wait();
                }
                thread = workersRingBuffer.next();
            }
            thread.assign(chunk);
        }

    }

    private Chunk getNextChunk() throws InterruptedException {
        synchronized (MESSAGE_Q_LOCK) {
            while (messageQueue.isEmpty()) {
                MESSAGE_Q_LOCK.wait();
            }
            return messageQueue.dequeue();
        }
    }

    protected void disconnect() {

        if (listensForClients)
            this.clientListener.stopListening();

        if (!this.clientServerSocket.isClosed()) {
            try {
                this.clientServerSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (listensForWorkers)
            this.workerListener.stopListening();

        if (!this.workerServerSocket.isClosed()) {
            try {
                this.workerServerSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public Server() {
        this.clientListenerPort = Utilities.CLIENTS_PORT;
        this.workerListenerPort = Utilities.WORKERS_PORT;
        this.init(); //Initialize server socket
    }

    public void startServer() throws InterruptedException {
        this.connect();

    }

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        server.startServer();
    }

}
