package kun.sockets;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Threaded server for handling sharing requests over sockets.
 *
 * This class listens for incoming socket connections on a specified port
 * and creates a separate thread for each client connection to handle sharing requests.
 * It can be stopped gracefully by calling the {@code stopServer()} method.
 *
 * @author Kun Xia
 */
public class SocketServerThread extends Thread{
    private int port;
    private boolean isRunning;
    private ExecutorService threadPool;

    /**
     * Constructs a new SocketServerThread with the specified port and thread pool size.
     *
     * @param port The port number to listen on.
     */
    public SocketServerThread(int port, int corePoolSize, int maxPoolSize) {
        this.port = port;
        this.isRunning = true;
        this.threadPool = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>()
        );

    }

    /**
     * Runs the server thread, listening for incoming socket connections and handling sharing requests.
     */
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(1000);
            System.out.println("Server is listening on port " + port);

            while (isRunning) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.execute(new SharingRequestHandler(clientSocket));
                } catch (SocketTimeoutException ignored) {
                    // SocketTimeoutException will be thrown when the accept() times out
                    // Periodically check isRunning to gracefully handle server shutdown
                    if (!isRunning) {
                        break;
                    }
                }
            }

            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

    /**
     * Stops the server gracefully.
     */
    public void stopServer() {
        isRunning = false;
        threadPool.shutdown();
    }
}
