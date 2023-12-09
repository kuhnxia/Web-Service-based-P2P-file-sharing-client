package kun.sockets;

import kun.helpers.LocalFileHelper;

import java.io.*;
import java.net.Socket;

/**
 * The SharingRequestHandler class represents a runnable thread that handles sharing requests from clients.
 * @author Kun Xia
 */
public class SharingRequestHandler implements Runnable {
    private Socket clientSocket;
    private String serverIP;
    private int serverPort;

    /**
     * Constructs a new SharingRequestHandler with the specified client socket.
     *
     * @param clientSocket The client socket for handling sharing requests.
     */
    public SharingRequestHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.serverIP = clientSocket.getLocalAddress().getHostAddress();
        this.serverPort = clientSocket.getLocalPort();
    }

    /**
     * Runs the sharing request handler thread, processing file requests from clients.
     */
    @Override
    public void run() {
        try {
            DataInputStream reader = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream writer = new DataOutputStream(clientSocket.getOutputStream());

            // Read the requested file name from the client
            String fileName = reader.readUTF();
            System.out.printf("A client requested file: %s by %s:%d\n",
                    fileName, serverIP, serverPort);

            // Send the requested file to the client
            sendFile(fileName, writer);

            // Close the connection
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the requested file to the client.
     *
     * @param fileName The name of the requested file.
     * @param writer   The DataOutputStream for writing to the client.
     * @throws IOException If an I/O error occurs while sending the file.
     */
    private void sendFile(String fileName, DataOutputStream writer) throws IOException {
        File file = LocalFileHelper.createNewFileForSending(fileName,
                serverIP, serverPort);

        try {
            if (file.exists()) {
                long length = file.length();
                writer.writeLong(length);

                FileInputStream fis = new FileInputStream(file);

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    writer.write(buffer, 0, bytesRead);
                }

            } else {
                // If the file does not exist, send an error message
                writer.writeLong(-1);
            }
        } catch (FileNotFoundException e) {
            writer.writeLong(-1);
        } catch (IOException e) {
            writer.writeLong(-1);
        }
    }
}
