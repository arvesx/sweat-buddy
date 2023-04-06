package main.java.activitytracker;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Worker extends Node {

    Socket socket;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public Worker(String ip_address, int port) {
        super(ip_address, port);
        this.init();

    }

    public void init() {
        try {
            this.socket = new Socket(this.ipAddress, this.port);
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());

            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() {

    }

    public void disconnect() {

    }

    public static void main(String[] args) {

        Worker worker = new Worker("127.0.0.1", 2345);

        System.out.println("Worker " + worker.ipAddress + " connected to server ");

        while (true) {

        }
    }

}
