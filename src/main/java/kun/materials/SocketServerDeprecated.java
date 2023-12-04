package kun.materials;

import kun.sockets.SharingRequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServerDeprecated {
    private ServerSocket serverSocket;
    private ExecutorService executorService;

    public SocketServerDeprecated(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);
            executorService = Executors.newFixedThreadPool(10); // Adjust the pool size as needed
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        while (!Thread.interrupted()) {
            try {
                Socket clientSocket = serverSocket.accept();
                executorService.execute(new SharingRequestHandler(clientSocket));
            } catch (IOException e) {
                // Handle IOException, if needed
                e.printStackTrace();
            }
        }
    }

    public void stopServer() {
        executorService.shutdownNow();
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 9090; // Change this to your desired port
        SocketServerDeprecated socketServer = new SocketServerDeprecated(port);
        // Start the server in a separate thread
        Thread serverThread = new Thread(() -> socketServer.startServer());
        serverThread.start();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        socketServer.stopServer();
    }
}
