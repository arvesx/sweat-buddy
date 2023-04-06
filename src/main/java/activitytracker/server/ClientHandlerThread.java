package main.java.activitytracker.server;

import main.java.activitytracker.server.fileprocessing.ClientData;
import main.java.activitytracker.server.fileprocessing.GpxFile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This class is responsible to handle the connection between one client and the server. It gets instantiated from the
 * ClientListener every time a new client connects. The new object is put in a ArrayList for later management.
 * e.g. Access to the ClientData of each object.
 */
public class ClientHandlerThread extends Thread {

    private Socket clientSocket;
    private ClientData clientData;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public ClientHandlerThread(Socket clientSocket, int clientId) {
        this.clientSocket = clientSocket;
        this.clientData = new ClientData(clientId);
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public ClientData getClientData() {
        return clientData;
    }

    @Override
    public void run() {
        try {

            this.outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            this.inputStream = new ObjectInputStream(clientSocket.getInputStream());

            String username = "";
            GpxFile gpx_file = null;

            username = (String) this.inputStream.readObject();
            this.clientData.setUsername(username);

            gpx_file = (GpxFile) this.inputStream.readObject();
            this.clientData.setGpxFile(gpx_file);
            System.out.println("[Server] Client#" + this.clientData.getID() + ": Received GPX File");
//            System.out.println(gpx_file.toString());

            // simulate work
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // System.out.println("exit work thread");

            outputStream.close();
            inputStream.close();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
