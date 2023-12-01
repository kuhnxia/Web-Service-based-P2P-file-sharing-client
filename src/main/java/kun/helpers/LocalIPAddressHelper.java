package kun.helpers;

import java.net.*;
import java.util.*;

/**
 * The LocalIPAddressHelper class provides methods for retrieving local IP addresses.
 */
public class LocalIPAddressHelper {

    /**
     * Gets a list of local IPv4 addresses.
     *
     * @return A List of InetAddress objects representing local IPv4 addresses.
     */
    public static List<InetAddress> getLocalIPAddresses() {
        List<InetAddress> inet4Addresses = new ArrayList<>();

        try{
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                if (!networkInterface.isLoopback() && networkInterface.isUp()) {
                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (address instanceof Inet4Address) {
                            inet4Addresses.add(address);
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return inet4Addresses;
    }
}
