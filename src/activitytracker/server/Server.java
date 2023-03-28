package activitytracker.server;

import activitytracker.Node;


import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Server extends Node{

    private ArrayList<ClientData> clientData;
    private int usersServed;
    
    private ServerSocket serverSocket;

    @Override
    protected void init()
    {
        try {
            this.serverSocket = new ServerSocket(this.port);
            System.out.println("Starting Server...");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void connect()
    {
        try {

            ServerListenerThread listener_td = new ServerListenerThread(this.serverSocket, this.clientData, this.usersServed);
            listener_td.start();


            listener_td.join();

            // Test Server Data
            for (ClientData cd : this.clientData)
            {
                System.out.println("Server Data: " + cd);
            }

                
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void disconnect()
    {
        
    }


    public Server(int port)
    {
        super(port);

        this.usersServed = 0;
        this.clientData = new ArrayList<ClientData>(); // ClientData -> keep ID, username, GpxFile
        this.init(); //Initialize server socket  

    }

    public void startServer() 
    {
        this.connect();    

    }

    public static void main(String[] args) {

        Server server = new Server(1234);
        server.startServer();
    }
}
