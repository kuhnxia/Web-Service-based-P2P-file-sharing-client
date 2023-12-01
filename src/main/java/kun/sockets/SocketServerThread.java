package kun.sockets;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * The SocketServerThread class represents a server thread that listens for incoming socket connections
 * and handles sharing requests from clients.
 */
public class SocketServerThread extends Thread{
    private int port;

    /**
     * Constructs a new SocketServerThread with the specified port.
     *
     * @param port The port number to listen on.
     */
    public SocketServerThread(int port) {
        this.port = port;
    }

    /**
     * Runs the server thread, listening for incoming socket connections and handling sharing requests.
     */
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread clientThread = new Thread(new SharingRequestHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
