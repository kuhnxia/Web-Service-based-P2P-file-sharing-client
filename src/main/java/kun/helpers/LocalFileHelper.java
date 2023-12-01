package kun.helpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 * The LocalFileHelper class provides methods for managing local files, including file creation,
 * copying, deletion, and listing within a shared folder structure.
 */
public class LocalFileHelper {
    private static String sharedFilesDirectory;
    private static String receivedFilesDirectory;
    private static final String APPLICATION_CACHE_DIRECTORY = "CORBA-based-P2P-file-sharing-application";
    public static final String SHARED_FILES_DIRECTORY_PART = "shared_files";
    public static final String RECEIVED_FILES_DIRECTORY_PART = "received_files";

    /**
     * Creates the shared file directory based on the provided socket server address and port.
     *
     * @param socketServerAddress The socket server address.
     * @param port                The port number.
     */
    public static void createSharedFileDirectory(String socketServerAddress, int port) {
        //Get the shared file directory;
        sharedFilesDirectory = getSharedFilesDirectory(socketServerAddress, port);

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

        String alterSharedFilesDirectory = getSharedFilesDirectory(serverAddress, port);
        return new File(alterSharedFilesDirectory + File.separator + fileName);

    }

    /**
     * Creates a new file for receiving based on the file name.
     *
     * @param fileName The name of the file.
     * @return A File object representing the new file for receiving.
     */
    public static File createNewFileForReceiving(String fileName) {

        receivedFilesDirectory = getReceivedFilesDirectory();
        File file = new File(receivedFilesDirectory + File.separator + fileName);

        // Ensure that the directories leading to the file exist
        File parentDirectory = file.getParentFile();
        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }

        return file;
    }

    /**
     * Extracts the filename from the given source file path.
     *
     * @param sourceFilePath The source file path.
     * @return The filename.
     */
    public static String getFilenameFromPath(String sourceFilePath) {
        // Create Path object for the source file
        Path sourcePath = Paths.get(sourceFilePath);

        // Extract file name from the source path
        String fileName = sourcePath.getFileName().toString();

        return fileName;
    }

    /**
     * Copies a file to the shared folder.
     *
     * @param sourceFilePath The path of the source file.
     * @return True if the file is copied successfully, false otherwise.
     */
    public static boolean copyFileToSharedFolder(String sourceFilePath) {
        // Create Path object for the source file
        Path sourcePath = Paths.get(sourceFilePath);

        // Extract file name from the source path
        String fileName = sourcePath.getFileName().toString();

        // Create Path object for the destination folder
        Path sharedFolderPath = Paths.get(sharedFilesDirectory);

        // Create Path object for the destination file
        Path destinationFilePath = sharedFolderPath.resolve(fileName);

        // Check if the destination file already exists
        if (Files.exists(destinationFilePath)) return true;

        try {
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
     * Lists the shared files in the shared folder.
     */
    public static void listSharedFilesInFolder() {
        File folder = new File(sharedFilesDirectory);

        // Check if the provided path points to a directory
        if (!folder.isDirectory()) {
            System.out.println("Not a valid directory path.");
            return;
        }
        // List all files in the directory
        File[] files = folder.listFiles();

        // Check if there are any files in the directory
        if (files.length > 0) {
            // Print the names of all files in the directory
            for (File file : files) {
                if (file.isFile()) {
                    System.out.println(file.getName());
                }
            }
        } else {
            System.out.println("No shared file in the folder");
        }
    }

    private static String getSharedFilesDirectory(String socketServerAddress, int port) {
        // Get the user's home directory
        String userHome = System.getProperty("user.home");
        
        return userHome + File.separator
                + APPLICATION_CACHE_DIRECTORY + File.separator
                + SHARED_FILES_DIRECTORY_PART + File.separator
                + socketServerAddress.replace(".", "_")
                + "_" + port;
    }

    private static String getReceivedFilesDirectory() {
        // Get the user's home directory
        String userHome = System.getProperty("user.home");

        return userHome + File.separator
                + APPLICATION_CACHE_DIRECTORY + File.separator
                + RECEIVED_FILES_DIRECTORY_PART;
    }

}