package kun.helpers;

/**
 * Helper class for managing client socket connection details.
 *
 * This class provides methods to get and set the IP address and port number
 * for a client socket connection.
 *
 * Note: Ensure to set the IP address and port number before using them in the application.
 *
 * @author Kun Xia
 */
public class ClientSocketHelper {
    private static String IP;
    private static int port;

    /**
     * Gets the set IP address for client socket connections.
     *
     * @return The set IP address as a String.
     */
    public static String getIP() {
        return IP;
    }

    /**
     * Gets the set port number for client socket connections.
     *
     * @return The set port number as an integer.
     */
    public static int getPort() {
        return port;
    }

    /**
     * Sets the IP address for client socket connections.
     *
     * @param IP The IP address to be set.
     */
    public static void setIP(String IP) {
        ClientSocketHelper.IP = IP;
    }

    /**
     * Sets the port number for client socket connections.
     *
     * @param port The port number to be set.
     */
    public static void setPort(int port) {
        ClientSocketHelper.port = port;
    }
}
