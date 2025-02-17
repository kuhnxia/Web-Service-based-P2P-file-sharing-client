package kun.helpers;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 * The LocalFileHelper class provides methods for managing local files, including file creation,
 * copying, deletion, and listing within a shared folder structure.
 *
 * This class assumes a shared folder structure for file management.
 * The shared folder structure is based on the socket server address and port.
 * It includes methods for creating, copying, and deleting files within the shared structure.
 * Additionally, it provides methods for listing files and opening directories.
 *
 * Note: Ensure that the shared folder structure is properly set before using other methods.
 * The shared folder structure is created using the setSharedFilesDirectory method.
 *
 * @author Kun Xia
 */
public class LocalFileHelper {
    private static String sharedFilesDirectory;
    private static String receivedFilesDirectory;
    private static final String APPLICATION_CACHE_DIRECTORY = "Web-Service-based-P2P-file-sharing-client";
    private static final String SHARED_FILES_DIRECTORY_PART = "shared_files";
    private static final String RECEIVED_FILES_DIRECTORY_PART = "received_files";

    /**
     * Creates the shared file directory based on the provided socket server address and port.
     *
     * @param socketServerAddress The socket server address.
     * @param port                The port number.
     */
    public static void createSharedFileDirectory(String socketServerAddress, int port) {
        setSharedFilesDirectory(socketServerAddress, port);
        // Create Path object for the destination folder
        Path sharedFolderPath = Paths.get(sharedFilesDirectory);

        // Check if the destination folder exists, and create it if not
        if (!Files.exists(sharedFolderPath)) {
            try {
                Files.createDirectories(sharedFolderPath);
                System.out.println("Shared folder created: " + sharedFolderPath);
            } catch (IOException e) {
                System.err.println("Error creating destination folder: " + e.getMessage());
            }
        }

    }

    /**
     * Creates a new file for sending based on the file name, server address, and port.
     *
     * @param fileName      The name of the file.
     * @param serverAddress The server address.
     * @param port          The port number.
     * @return A File object representing the new file for sending.
     */
    public static File createNewFileForSending(String fileName, String serverAddress, int port) {
        /*
        One socket server can map to more than one server address!

        In case, there is more than one available network interface,
        and the socket server is created with the same port more than one time,
        and each time, this client may choose a different IP (interface)
        to interact with other clients in its identical local network.

        This method can handle the file share request from a local network
        other than the local network where this client is registering, cancelling, searching and sharing files
        if the port is the same.
        */

        String alterSharedFilesDirectory = getAlterSharedFilesDirectory(serverAddress, port);
        return new File(alterSharedFilesDirectory + File.separator + fileName);

    }

    /**
     * Creates a new file for receiving based on the file name.
     *
     * @param fileName The name of the file.
     * @return A File object representing the new file for receiving.
     */
    public static File createNewFileForReceiving(String fileName) {
        setReceivedFilesDirectory();

        File file = new File(receivedFilesDirectory + File.separator + fileName);

        // Ensure that the directories leading to the file exist
        File parentDirectory = file.getParentFile();
        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }

        return file;
    }

    /**
     * Copies a file to the shared folder.
     *
     * @param sourceFilePath The path of the source file.
     * @return True if the file is copied successfully, false otherwise.
     */
    public static boolean copyFileToSharedFolder(String sourceFilePath) {
        try {
            // Create Path object for the source file
            Path sourcePath = Paths.get(sourceFilePath);

            // Extract file name from the source path and replace spaces with underscores
            String fileName = sourcePath.getFileName().toString().replace(" ", "_");

            // Create Path object for the destination folder
            Path sharedFolderPath = Paths.get(sharedFilesDirectory);

            // Create Path object for the destination file
            Path destinationFilePath = sharedFolderPath.resolve(fileName);

            // Check if the destination file already exists
            if (Files.exists(destinationFilePath)) return true;

            // Copy the file to the destination folder
            Files.copy(sourcePath, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

            System.out.println("File copied successfully to: " + destinationFilePath);

            return true;
        } catch (IOException e) {
            System.err.println("Error copying the file: " + e.getMessage());
        }
        return false;
    }

    /**
     * Deletes a file from the shared folder.
     *
     * @param fileName The name of the file to be deleted.
     */
    public static void deleteFileFromSharedFolder(String fileName) {
        // Construct the file path
        String filePath = sharedFilesDirectory + File.separator + fileName;

        // Create a File object for the file and delete the file.
        File fileToDelete = new File(filePath);
        if (fileToDelete.exists()) {
            fileToDelete.delete();
            System.out.println("File deleted from the shared folder");
        }

    }

    /**
     * Retrieves an array of files from the shared file directory.
     *
     * @return An array of File objects representing files in the shared file directory.
     */
    public static File[] getFilesFromSharedFileDirectory() {
        File folder = new File(sharedFilesDirectory);

        // List all files in the directory.
        File[] files = folder.listFiles();

        return files;
    }

    /**
     * Opens the directory containing the received file.
     *
     * @param fileName The name of the received file.
     * @return A message indicating the result of the operation.
     */
    public static String openReceivedFileDirectory(String fileName) {
        File receivedFile = new File(receivedFilesDirectory + File.separator + fileName);
        String message = "";
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    // Open the file explorer at the directory
                    desktop.open(receivedFile.getParentFile());
                }
            } else message = "System doesn't support open directories.";

        } catch (IOException e) {
            message = "Error opening the file directory: " + e.getMessage();
        }
        return message;
    }


    private static void setSharedFilesDirectory(String socketServerAddress, int port) {
        String userHome = System.getProperty("user.home");
        
        sharedFilesDirectory = userHome + File.separator
                + APPLICATION_CACHE_DIRECTORY + File.separator
                + SHARED_FILES_DIRECTORY_PART + File.separator
                + socketServerAddress.replace(".", "_")
                + "_" + port;
    }

    private static String getAlterSharedFilesDirectory(String socketServerAddress, int port) {
        String userHome = System.getProperty("user.home");

        return userHome + File.separator
                + APPLICATION_CACHE_DIRECTORY + File.separator
                + SHARED_FILES_DIRECTORY_PART + File.separator
                + socketServerAddress.replace(".", "_")
                + "_" + port;
    }

    private static void setReceivedFilesDirectory() {
        String userHome = System.getProperty("user.home");

        receivedFilesDirectory = userHome + File.separator
                + APPLICATION_CACHE_DIRECTORY + File.separator
                + RECEIVED_FILES_DIRECTORY_PART;
    }

}