package activitytracker.server;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Client(String host_address, int port)
    {
        try {
            this.socket = new Socket(host_address, port);

            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader( new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client cl = new Client("127.0.0.1", 1234);

        GpxFile gpxFile = new GpxFile("src/activitytracker/server/gpxfile.xml");
        System.out.println(gpxFile);



        while (true)
        {

        }
    }


}
