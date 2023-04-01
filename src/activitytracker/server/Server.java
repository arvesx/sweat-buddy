package activitytracker.server;

import activitytracker.Node;

import java.io.IOException;

import java.net.ServerSocket;

public class Server {

    private ServerSocket clientServerSocket;
    private ServerSocket workerServerSocket;

    private int clientListenerPort;
    private int workerListenerPort;

    ClientListener clientListener;
    WorkerListener workerListener;

    boolean listensForClients;
    boolean listensForWorkers;

    protected void init() {
        try {
            this.clientServerSocket = new ServerSocket(this.clientListenerPort);
            this.workerServerSocket = new ServerSocket(this.workerListenerPort);
            System.out.println("[Server] Starting Server...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void connect() {
        System.out.println("[Server] Listening for new clients on port " + this.clientListenerPort);
        this.clientListener = new ClientListener(this.clientServerSocket);
        clientListener.start();
        this.listensForClients = true;

        System.out.println("[Server] Listening for new workers on port " + this.workerListenerPort);
        this.workerListener = new WorkerListener(this.workerServerSocket);
        workerListener.start();
        this.listensForWorkers = true;


    }

    protected void disconnect() {

        if (listensForClients) this.clientListener.stopListening();

        if (listensForWorkers) this.workerListener.stopListening();

    }


    public Server() {
        this.clientListenerPort = 1234;
        this.workerListenerPort = 2345;
        this.init(); //Initialize server socket
    }

    public void startServer() {
        this.connect();

    }

    public static void main(String[] args) {

        Server server = new Server();
        server.startServer();
    }
}
