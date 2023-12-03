package kun.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

/**
 * The LocalIPAddressHelper class provides methods for retrieving local IP addresses.
 */
public class LocalNetworkHelper {

    /**
     * Gets a list of local IPv4 addresses.
     *
     * @return A List of InetAddress objects representing local IPv4 addresses.
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
