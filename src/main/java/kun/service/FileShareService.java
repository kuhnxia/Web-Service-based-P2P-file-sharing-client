package kun.service;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;

public class FileShareService {

    // Change to the real url of your web service.
    private final String BASE_URI = "http://localhost:8080/share";

    public String registerFile(String fileName, String ipAddress, int port) {
        String message = "Bad request!";

        try (Client client = ClientBuilder.newClient()) {
            Form form = new Form();
            form.param("fileName", fileName);
            form.param("ipAddress", ipAddress);
            form.param("port", Integer.toString(port));

            Response response = client
                    .target(BASE_URI)
                    .path("/registerFile")
                    .request()
                    .post(Entity.form(form));

            message = response.readEntity(String.class);
        }
        return message;
    }

    public String cancelSharing(String fileName, String ipAddress, int port) {
        String message = "Bad request!";

        try (Client client = ClientBuilder.newClient()) {
            Form form = new Form();
            form.param("fileName", fileName);
            form.param("ipAddress", ipAddress);
            form.param("port", Integer.toString(port));

            Response response = client
                    .target(BASE_URI)
                    .path("/cancelSharing")
                    .request()
                    .build("DELETE", Entity.form(form))
                    .invoke();

            message = response.readEntity(String.class);
        }

        return message;
    }

    public String findSharedFiles(String fileName) {
        String message = "Bad request!";

        try (Client client = ClientBuilder.newClient()) {
            Response response = client
                    .target(BASE_URI)
                    .path("/findSharedFiles/" + fileName)
                    .request()
                    .get();

            message = response.readEntity(String.class);
        }

        return message;
    }

    public String getSocketAddressById(int id) {
        String message = "Bad request!";
        try (Client client = ClientBuilder.newClient()) {
            Response response = client
                    .target(BASE_URI)
                    .path("/getSocketAddressById/" + id)
                    .request()
                    .get();

            message = response.readEntity(String.class);
        }

        return message;
    }
}
