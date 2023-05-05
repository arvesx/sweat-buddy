package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import dependencies.fileprocessing.gpx.GpxResults;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import dependencies.fileprocessing.gpx.GpxFile;

public class Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private final String username;
    private final GpxFile gpxFile;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public Client(String username, String host_address, int port) {

        this.username = username;
        String file_path = "src\\main\\resources\\gpxfiles\\route1.xml";

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("File Sent to Server: " + file_path);
        }

        this.gpxFile = new GpxFile(file_path);

        try {
            this.socket = new Socket(host_address, port);

            this.outputStream = new ObjectOutputStream(socket.getOutputStream());

            this.inputStream = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendClientInfo() {
        try {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Username: " + this.username);
            }

            this.outputStream.writeObject(this.username);
            this.outputStream.flush();

            this.outputStream.writeObject(this.gpxFile);
            this.outputStream.flush();


        } catch (IOException e) {
            System.out.println("error");
        }

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Pick a username: ");
        String username = scanner.nextLine();

        Client cl = new Client(username, "127.0.0.1", 1234);

        cl.sendClientInfo();

        GpxResults results;
        scanner.close();
        try {
            results = (GpxResults) cl.inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        System.out.println(results);
    }
}
