package main.java.activitytracker.server;

import static main.java.activitytracker.server.Utilities.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {
    private volatile boolean serverRunning;
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
            System.out.println("[Server] Starting Server...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void connect() throws InterruptedException {
        System.out.println("[Server] Listening for new clients on port " + this.clientListenerPort);
        this.clientListener = new ClientListener(this.clientServerSocket);
        clientListener.start();
        this.listensForClients = true;


        System.out.println("[Server] Listening for new workers on port " + this.workerListenerPort);
        this.workerListener = new WorkerListener(this.workerServerSocket);
        workerListener.start();
        this.listensForWorkers = true;

        Scanner scanner = new Scanner(System.in);
        while (serverRunning) {
            System.out.print(">>> ");
            String command = scanner.nextLine();
            if (command.equals("?disconnect")) {
                serverRunning = false;
                disconnect();
            }
        }

    }

    private void handleCommand(String command) {

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
        this.clientListenerPort = 1234;
        this.workerListenerPort = 2345;
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
