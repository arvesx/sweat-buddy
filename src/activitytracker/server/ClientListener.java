package activitytracker.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class is responsible for listening to incoming clients. It's instantiated only once, by the server.
 * When a new client connects, it creates a new ClientHandlerThread object, which is a new thread to handle
 * the connection.
 */
public class ClientListener extends Thread {

    private ServerSocket serverSocket;
    private ArrayList<ClientHandlerThread> clientThreads;
    private int clientID;
    private boolean running;

    public ClientListener(ServerSocket serverSocket) {
        this.clientID = 0;
        this.serverSocket = serverSocket;
        this.clientThreads = new ArrayList<>();
    }

    /**
     * Returns the list of client threads (objects of type ClientHandlerThread). Each thread corresponds to a connection
     * between the server and some client. Each of these objects has useful information such as client data. If we want
     * to access these threads from the Server class we can utilize this method.
     */
    public ArrayList<ClientHandlerThread> getClientThreads() {
        return clientThreads;
    }

    private void startListening() {
        this.running = true;
    }

    public void stopListening() {
        // gets called when Server shuts down
        System.out.println("[Server] Shutting down client listener");
        this.running = false;
    }

    @Override
    public void run() {

        try {
            startListening();
            while (running) {
                // Accept client incoming connection
                Socket clientSocket = this.serverSocket.accept();
                System.out.println("[Server] Client#" + this.clientID + ": Connected");

                // Handle this client
                ClientHandlerThread clientThread = new ClientHandlerThread(clientSocket, this.clientID);
                clientThread.start();
                clientThreads.add(clientThread);

                this.clientID++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
