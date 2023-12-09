package kun.helpers;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Helper class for working with local network-related operations.
 *
 * This class provides methods to retrieve local IP addresses and list ports in use.
 * The method {@code getLocalIPAddresses()} returns a list of available IPv4 addresses.
 * The method {@code listPortsInUse()} returns a set of ports currently in use.
 *
 * Note: The port listing functionality is system-dependent and currently supports Unix-like systems
 * using the 'lsof' command. It may need modification for different operating systems.
 *
 * @author Kun Xia
 */

public class LocalNetworkHelper {

    /**
     * Retrieves a list of available local IPv4 addresses.
     *
     * @return A List containing available local IPv4 addresses.
     */
    public static List<String> getLocalIPAddresses() {
        List<String> inet4Addresses = new ArrayList<>();

        try{
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                if (!networkInterface.isLoopback() && networkInterface.isUp()) {
                    Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

                    while (addresses.hasMoreElements()) {
                        InetAddress address = addresses.nextElement();
                        if (address instanceof Inet4Address) {
                            inet4Addresses.add(address.getHostAddress());
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return inet4Addresses;
    }

    /**
     * Lists ports currently in use on the local system.
     *
     * @return A Set containing ports currently in use.
     */
    public static Set<Integer> listPortsInUse() {
        Set<Integer> inUsePortList = new HashSet<>();
        try {
            Process process = Runtime.getRuntime().exec("lsof -i -P -n");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("LISTEN") && line.contains("TCP *:")) {
                    String[] tokens = line.split("\\s+");
                    String socketAddress = tokens[tokens.length - 2];
                    int port = Integer.parseInt(socketAddress.split(":")[1]);
                    inUsePortList.add(port);
                }
            }
            reader.close();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return inUsePortList;
    }
}
