package com.example.composeproject;

import android.util.JsonReader;

import com.example.composeproject.dependencies.Utilities;
import com.example.composeproject.dependencies.fileprocessing.TransmissionObject;
import com.example.composeproject.dependencies.fileprocessing.TransmissionObjectType;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.net.Socket;


public class BackendCommunicator {
    private static BackendCommunicator instance = null;
    private Socket socket;

    private boolean connectionEstablished;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    private static final Gson gson = new Gson();

    private BackendCommunicator() {
        connectionEstablished = false;
    }

    public void attemptConnection() {
        try {
            this.socket = new Socket("192.168.1.9", Utilities.CLIENTS_PORT);
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.connectionEstablished = true;
            System.out.println("connection established");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public TransmissionObject sendClientInfo(TransmissionObject to) {
        if (!connectionEstablished) attemptConnection();

        try {
            System.out.println("sending to server to");
            String jsonString = gson.toJson(to);

            this.outputStream.writeObject(jsonString);
            this.outputStream.flush();

            System.out.println("all fine 1");
            String answer = (String) this.inputStream.readObject();
            System.out.println("all fine 2");

            return decodeJsonString(answer);
        } catch (IOException e) {
            System.out.println("error");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return new TransmissionObject();
    }

    public static TransmissionObject decodeJsonString(String jsonString) {
        return gson.fromJson(jsonString, TransmissionObject.class);
    }


    public static TransmissionObject createTransmissionObject(TransmissionObjectType type) {
        TransmissionObject to = new TransmissionObject();
        to.type = type;
        return to;
    }


    public static synchronized BackendCommunicator getInstance() {
        if (instance == null) instance = new BackendCommunicator();
        return instance;
    }
}
