package main.java.activitytracker.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import main.java.activitytracker.fileprocessing.gpx.GpxFile;

public class Client {

    private final String username;
    private final GpxFile gpxFile;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public Client(String username, String host_address, int port) {
        this.username = username;
        this.gpxFile = new GpxFile("src/main/resources/gpxfiles/route1.xml");

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

            System.out.println("username: " + this.username);

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

        scanner.close();
        while (true) { }
    }
}
