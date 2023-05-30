package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import com.google.gson.Gson;
import dependencies.fileprocessing.TransmissionObject;
import dependencies.fileprocessing.TransmissionObjectType;
import dependencies.fileprocessing.gpx.GpxResults;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import dependencies.Utilities;
import dependencies.fileprocessing.gpx.GpxFile;

public class Client {

    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    private String username;
    private GpxFile gpxFile;

    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private Gson gson = new Gson();

    public Client(String host_address, int port) {

        try {
            this.socket = new Socket(host_address, port);

            this.outputStream = new ObjectOutputStream(socket.getOutputStream());

            this.inputStream = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendClientInfo(TransmissionObject to) {
        try {
            String jsonString = gson.toJson(to);

            this.outputStream.writeObject(jsonString);
            this.outputStream.flush();


        } catch (IOException e) {
            System.out.println("error");
        }

    }

    public static void main(String[] args) {
        Client c = new Client(Utilities.HOST_ADDRESS, Utilities.CLIENTS_PORT);

        Scanner s = new Scanner(System.in);
        System.out.print("Username: ");
        String username = s.nextLine();
        System.out.println("Password: ");
        String password = s.nextLine();

        TransmissionObject to = new TransmissionObject();
        to.type = TransmissionObjectType.LOGIN_MESSAGE;
        to.username = username;
        to.password = password;
        c.sendClientInfo(to);

        while (true) {

        }

    }
}
