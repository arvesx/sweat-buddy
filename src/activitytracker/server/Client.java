package activitytracker.server;

import java.io.*;
import java.net.Socket;

public class Client {

    private String username;
    private GpxFile gpxFile;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream is;

    public Client(String username, String host_address, int port)
    {
        this.username = username;
        this.gpxFile = new GpxFile("src/activitytracker/server/gpxfile.xml");

        try {
            this.socket = new Socket(host_address, port);

            this.out = new ObjectOutputStream(socket.getOutputStream());

            this.is = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendClientInfo()
    {
        try {

            System.out.println("username: " + this.username);

            this.out.writeObject(this.username);
            this.out.flush();

            this.out.writeObject(this.gpxFile);
            this.out.flush();
            

        } catch (IOException e) {
            System.out.println("error");
        }
        
    }

    public static void main(String[] args) {
        Client cl = new Client("Dim", "127.0.0.1", 1234);

        cl.sendClientInfo();

        while (true)
        {

        }
    }


}
