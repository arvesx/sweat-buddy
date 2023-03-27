package activitytracker.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerListenerThread extends Thread {

    private ServerSocket serverSocket;

    private ArrayList<ClientData> clientData;

    private int clientID;

    public ServerListenerThread(ServerSocket server_socket, ArrayList<ClientData> client_data, int client_id) 
    {
        this.clientID = client_id;
        this.serverSocket = server_socket;
        this.clientData = client_data;
    }

    @Override
    public void run() {
        
        try {
            while (true)
            {
                Socket socket = this.serverSocket.accept();
                this.clientData.add(new ClientData(this.clientID));
                System.out.println("** Client#" + this.clientID + ": Connected");
    
                ServerWorkerThread worker_td = new ServerWorkerThread(socket, this.clientData.get(this.clientID));
                worker_td.start();

                this.clientID++;
            } 

            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    
    
    

}
