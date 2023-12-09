package kun.service;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;

/**
 * Service class for interacting with the File Share web service.
 *
 * This class provides methods for registering files, canceling file sharing,
 * finding shared files, and retrieving socket addresses by file sharer ID.
 * It communicates with the web service using JAX-RS client.
 *
 * Note: Change the BASE_URI to the real URL of your web service.
 *
 * @author Kun Xia
 */
public class FileShareService {

    // Change to the real url of your web service.
    private static final String BASE_URI = "http://localhost:8080/share";

    /**
     * Registers a new file for sharing.
     *
     * @param fileName   The name of the file to be registered.
     * @param ipAddress  The IP address of the file sharer.
     * @param port       The port number on which the file is shared.
     * @return Response from the web service indicating the status of the registration.
     */
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

        } catch (ProcessingException e) {
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity("JBoss service not available.")
                    .build();
        }
    }

    /**
     * Cancels sharing of a file.
     *
     * @param fileName   The name of the file to stop sharing.
     * @param ipAddress  The IP address of the file sharer.
     * @param port       The port number on which the file is shared.
     * @return Response from the web service indicating the status of canceling file sharing.
     */
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

    /**
     * Finds shared files by filename.
     *
     * @param fileName   The name of the file to search for.
     * @return Response from the web service containing the status and search results.
     */
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

    /**
     * Gets the socket address of a file sharer by its ID.
     *
     * @param id   The ID of the file sharer.
     * @return Response from the web service containing the status and socket address.
     */
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
