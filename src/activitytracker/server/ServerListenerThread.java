package activitytracker.server;

import java.io.*;
import java.net.Socket;

public class ServerListenerThread extends Thread {

    private Socket socket;

    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    public ServerListenerThread(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(
                    new OutputStreamWriter(
                            this.socket.getOutputStream()));

            this.bufferedReader = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()
                    )
            );

        } catch(IOException e) {

        }
    }

    @Override
    public void run() {
        try {
            this.bufferedWriter.flush();
            this.bufferedWriter.write("Connected to server");
        } catch (IOException e) {
            e.printStackTrace();
        }




    }





}
