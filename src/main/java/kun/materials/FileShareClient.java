package kun.materials;

import kun.service.FileShareService;
import kun.helpers.LocalFileHelper;
import kun.helpers.LocalNetworkHelper;
import kun.sockets.SocketClientThread;
import kun.sockets.SocketServerThread;

import java.net.http.HttpResponse;
import java.util.*;

/**
 * The FileShareClient class represents the main client application for a CORBA-based P2P file sharing system.
 */
public class FileShareClient {
    private static String socketServerAddress;
    private static int port;
    private static FileShareService fileShare;
    private static Scanner sc = new Scanner(System.in);

    /**
     * The main method of the FileShareClient application.
     *
     * @param args The command-line arguments (not used in this application).
     */
    public static void main(String[] args) {

        System.out.println("Get this computer's IP address in your local network ...");
        socketServerAddress = getSocketServerAddress();
        System.out.println("You socket server IP address is: " + socketServerAddress);

        System.out.println("What is your prefer port to start the file sharing socket server?");
        port = sc.nextInt();

        LocalFileHelper.createSharedFileDirectory(socketServerAddress, port);

        SocketServerThread socketServer = new SocketServerThread(port);
        socketServer.start();

        System.out.println("Get the http file register service...");
        fileShare = new FileShareService();

        while (true){
            try {
                System.out.println("\nPlease choose which option do you want: \n" +
                        "Enter 1 to register a file for sharing!\n" +
                        "Enter 2 to cancel a shared file\n" +
                        "Enter 3 to search and request a shared file from other clients\n");

                operation(sc.nextInt());

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                sc.next(); // consume the invalid input to prevent an infinite loop
            }

        }
    }

    /**
     * Performs the selected operation based on the user's choice.
     *
     * @param choice The user's choice.
     */
    private static void operation(int choice) {
        switch (choice) {
            case 1:
                // Registering a file for sharing.
                registerFile();
                break;
            case 2:
                // Canceling a shared file.
                cancelSharing();
                break;
            case 3:
                // searching and requesting a shared file from other clients.
                searchAndRequestSharedFile();
                break;
            default:
                System.out.println("Invalid choice. Please enter a valid option.");
        }
    }

    /**
     * Registers a file for sharing.
     */
    private static void registerFile() {
        while (true) {
            try {
                System.out.println("Please enter the absolute path of your file ready to share:");
                System.out.println("The path should not contain any space, a wrong example: My folder\\my file.txt");
                System.out.println("Enter 0 to go back!");
                String sourcePath = sc.next();

                if (sourcePath.equals("0")) break;

                String fileName = LocalFileHelper.getFilenameFromPath(sourcePath);
                Boolean copied = LocalFileHelper.copyFileToSharedFolder(sourcePath);
                Thread.sleep(1000);
                if (copied){
                    HttpResponse response = fileShare.registerFile(fileName, socketServerAddress, port);
                    System.out.println(response.body());
                } else {
                    System.out.println("It is not a valid file path");
                }
            } catch (InterruptedException e){
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }

    /**
     * Cancels sharing of a file.
     */
    private static void cancelSharing() {
        while (true) {
            System.out.println("Below is the shared file list: ");
            LocalFileHelper.listSharedFilesInFolder();

            System.out.println("Please enter the file name you want to cancel sharing:");
            System.out.println("Enter 0 to go back!");
            String fileName = sc.next();

            if (fileName.equals("0"))break;

            HttpResponse response = null;
            try {
                response = fileShare.cancelSharing(fileName, socketServerAddress, port);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println(response.body());

            LocalFileHelper.deleteFileFromSharedFolder(fileName);
        }

    }

    /**
     * Searches and requests a shared file from other clients.
     */
    private static void searchAndRequestSharedFile() {
        while (true) {
            System.out.println("Please enter the file name you want to search:");
            System.out.println("Enter 0 to go back!");
            String fileName = sc.next();
            if (fileName.equals("0")) break;

            HttpResponse response = null;
            try {
                response = fileShare.findSharedFiles(fileName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            String searchResult = response.body().toString();
            if (!searchResult.equals("")){
                System.out.printf("\nList all available file ids with the same target name %s: \n", fileName);

                String[] stringIds = searchResult.split(" ");
                List<Integer> fileIds = new ArrayList<>();
                for (String id : stringIds){
                    System.out.println("File id: " + id);
                    fileIds.add(Integer.parseInt(id));
                }

                requestSharedFile(fileIds, fileName);

            } else {
                System.out.println("No match result.");
            }
        }
    }

    /**
     * Requests a shared file from other clients based on the file ID.
     *
     * @param fileIds  The list of available file IDs.
     * @param fileName The name of the file to request.
     */
    private static void requestSharedFile(List<Integer> fileIds, String fileName) {
        while (true) {
            try {
                System.out.printf("\nYour working socket server address is %s:%d", socketServerAddress, port);
                System.out.println("\nPlease enter the correct id of the file you want to request for sharing!");
                System.out.println("Enter 0 to go back!");
                int fileId = sc.nextInt();
                if (fileId == 0) break;

                if (fileIds.contains(fileId)) {
                    // Get client information
                    HttpResponse response = fileShare.getSocketAddressById(fileId);
                    String[] socketAddress = response.body().toString().split(":");
                    String clientIP =socketAddress[0];
                    int clientPort = Integer.parseInt(socketAddress[1]);

                    // Request and send the file.
                    SocketClientThread socketClient = new SocketClientThread(fileName, clientIP, clientPort);
                    socketClient.start();

                    //Waiting the socket ends until 10 seconds later.
                    socketClient.join(10000);
                }


            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                sc.next(); // consume the invalid input to prevent an infinite loop
            } catch (InterruptedException e) {
                System.out.println("Error: " + e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Gets the IP address of the socket server for the client.
     *
     * @return The IP address of the socket server.
     */
    private static String getSocketServerAddress() {
        List<String> inet4Addresses = LocalNetworkHelper.getLocalIPAddresses();

        if (inet4Addresses.size() == 1){
            return inet4Addresses.get(0);
        } else {
            System.out.println("You are using not only one network interfaces, such as Ethernet, WiFi, Cellular, VPN.\n" +
                    "Choose the correct IP that the router assigned to you in your local network: \n");
            int i = 1;
            for (String inet4Address : inet4Addresses) {
                System.out.printf("Enter %d if you will use local IP: %s\n\n", i, inet4Address);
                i++;
            }
            System.out.println("If you do not know which IP you will use to interact with other computers in your local network\n"
                    + "You can try to close Cellular or VPN, and keep only one of Wifi or Ethernet connections,\n"
                    + "then restart this program manually!\n");

            int choice = 0;
            while (choice <= 0 || choice >= i) {
                System.out.println("Please enter a right choice: ");
                try {
                    choice = sc.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    sc.next(); // consume the invalid input to prevent an infinite loop
                }
            }
            return inet4Addresses.get(choice-1);

        }

    }
}