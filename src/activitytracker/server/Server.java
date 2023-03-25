package activitytracker.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket serverSocket;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void startServer() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                ServerListenerThread listener_td = new ServerListenerThread(socket);
                listener_td.start();

                System.out.println("A new client has connected");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        System.out.println("Starting Server...");

        Server server = new Server(1234);
        server.startServer();
    }
}
