package activitytracker.server;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import activitytracker.Node;

public class Worker extends Node {

    Socket socket;

    public Worker(String ip_address, int port)
    {
        super(ip_address, port);
        this.init();

    }

    public void init()
    {
        try {
            this.socket = new Socket(this.ipAddress, this.port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect()
    {

    }

    public void disconnect()
    {

    }

    public static void main(String[] args) {
        
        Worker worker = new Worker("127.0.0.1", 1000);

    }

}
