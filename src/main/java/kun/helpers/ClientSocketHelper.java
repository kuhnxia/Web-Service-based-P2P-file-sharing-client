package kun.helpers;

public class ClientSocketHelper {
    private static String IP;
    private static int port;

    public static String getIP() {
        return IP;
    }

    public static int getPort() {
        return port;
    }

    public static void setIP(String IP) {
        ClientSocketHelper.IP = IP;
    }

    public static void setPort(int port) {
        ClientSocketHelper.port = port;
    }
}
