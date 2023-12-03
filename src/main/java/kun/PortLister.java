package kun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class PortLister {

    public static void main(String[] args) {
        listPortsInUse();
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
        for (int port : inUsePortList) System.out.println(port);
        return inUsePortList;
    }
}
