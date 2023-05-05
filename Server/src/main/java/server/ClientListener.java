package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import static server.Utils.*;

/**
 * This class is responsible for listening to incoming clients. It's instantiated only once, by the server.
 * When a new client connects, it creates a new ClientHandlerThread object, which is a new thread to handle
 * the connection.
 */
public class ClientListener extends Thread {

    private final ServerSocket serverSocket;
    private final ArrayList<ClientHandlerThread> clientThreads;
    private int clientID;
    private volatile boolean running;

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
        synchronized (CLIENTS_LIST_LOCK) {
            return clientThreads;
        }
    }

    private void startListening() {
        this.running = true;
    }

    public void stopListening() {
        // gets called when Server shuts down
        LOGGER.info("Shutting down client listener");
        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.running = false;
    }

    @Override
    public void run() {


        startListening();
        while (running) {
            // Accept client incoming connection
            try {
                Socket clientSocket = this.serverSocket.accept();
                LOGGER.info("Client#" + this.clientID + ": Connected");

                // Handle this client
                ClientHandlerThread clientThread = new ClientHandlerThread(clientSocket, this.clientID);
                clientThread.start();
                synchronized (CLIENTS_LIST_LOCK) {
                    clientThreads.add(clientThread);
                }

                this.clientID++;
            } catch (SocketException e) {
                LOGGER.info("Stopped accepting clients");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }


}
