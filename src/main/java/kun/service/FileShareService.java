package kun.service;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;

public class FileShareService {

    // Change to the real url of your web service.
    private static final String BASE_URI = "http://localhost:8080/share";

    public static Response registerFile(String fileName, String ipAddress, int port) {
        try (Client client = ClientBuilder.newClient()) {
            Form form = new Form();
            form.param("fileName", fileName);
            form.param("ipAddress", ipAddress);
            form.param("port", Integer.toString(port));

            return client
                    .target(BASE_URI)
                    .path("/registerFile")
                    .request()
                    .post(Entity.form(form));

            //message = response.readEntity(String.class);
        } catch (ProcessingException e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("JBoss service not available.")
                    .build();
        }
    }

    public static Response cancelSharing(String fileName, String ipAddress, int port) {
        try (Client client = ClientBuilder.newClient()) {
            Form form = new Form();
            form.param("fileName", fileName);
            form.param("ipAddress", ipAddress);
            form.param("port", Integer.toString(port));

            return client
                    .target(BASE_URI)
                    .path("/cancelSharing")
                    .request()
                    .put(Entity.form(form));
        } catch (ProcessingException e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("JBoss service not available.")
                    .build();
        }
    }

    public static Response findSharedFiles(String fileName) {

        try (Client client = ClientBuilder.newClient()) {
            return client
                    .target(BASE_URI)
                    .path("/findSharedFiles/" + fileName)
                    .request()
                    .get();

        } catch (ProcessingException e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("JBoss service not available.")
                    .build();
        }
    }

    public static Response getSocketAddressById(int id) {
        String message = "Bad request!";
        try (Client client = ClientBuilder.newClient()) {
            return client
                    .target(BASE_URI)
                    .path("/getSocketAddressById/" + id)
                    .request()
                    .get();

        } catch (ProcessingException e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("JBoss service not available.")
                    .build();
        }
    }
}
