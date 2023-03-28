package activitytracker.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerWorkerThread extends Thread{

    Socket socket;
    ClientData clientData;

    private ObjectOutputStream out;
    private ObjectInputStream is;

    public ServerWorkerThread(Socket socket, ClientData client_data)
    {
        this.socket = socket;
        this.clientData = client_data;
    }
    
    @Override
    public void run()
    {
        try {

            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.is = new ObjectInputStream(socket.getInputStream());

            String username = "";
            GpxFile gpx_file = null;
    
            username = (String) this.is.readObject();
            this.clientData.setUsername(username);
    
            gpx_file = (GpxFile) this.is.readObject();
            this.clientData.setGpxFile(gpx_file);
            System.out.println("** Client#" + this.clientData.getID() + ": Received GPX File");
    
            // simulate work
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // System.out.println("exit work thread");
    
            out.close();
            is.close();
            
        } catch(IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
