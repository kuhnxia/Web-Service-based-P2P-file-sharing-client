package kun;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.Response;

public class FileShareClient {

    private static final String BASE_URI = "http://localhost:8080/share";

    public static void main(String[] args) {
        testSayHelloEndpoint();
        testRegisterFileEndpoint();
        testCancelSharingEndpoint();
        testFindSharedFilesEndpoint();
        testGetSocketAddressByIdEndpoint();
    }

    private static void testSayHelloEndpoint() {
        try (Client client = ClientBuilder.newClient()) {
            Response response = client
                    .target(BASE_URI)
                    .path("/World")
                    .request()
                    .get();

            handleResponse(response);
        }
    }

    private static void testRegisterFileEndpoint() {
        try (Client client = ClientBuilder.newClient()) {
            Form form = new Form();
            form.param("fileName", "test.jpg");
            form.param("ipAddress", "192.168.0.99");
            form.param("port", "7070");

            Response response = client
                    .target(BASE_URI)
                    .path("/registerFile")
                    .request()
                    .post(Entity.form(form));

            handleResponse(response);
        }
    }

    private static void testCancelSharingEndpoint() {
        try (Client client = ClientBuilder.newClient()) {
            Form form = new Form();
            form.param("fileName", "test.jpg");
            form.param("ipAddress", "192.168.0.99");
            form.param("port", "7070");

            Response response = client
                    .target(BASE_URI)
                    .path("/cancelSharing")
                    .request()
                    .build("DELETE", Entity.form(form))
                    .invoke();

            handleResponse(response);
        }
    }

    private static void testFindSharedFilesEndpoint() {
        try (Client client = ClientBuilder.newClient()) {
            Response response = client
                    .target(BASE_URI)
                    .path("/findSharedFiles/testFile")
                    .request()
                    .get();

            handleResponse(response);
        }
    }

    private static void testGetSocketAddressByIdEndpoint() {
        try (Client client = ClientBuilder.newClient()) {
            Response response = client
                    .target(BASE_URI)
                    .path("/getSocketAddressById/1")
                    .request()
                    .get();

            handleResponse(response);
        }
    }

    private static void handleResponse(Response response) {
        System.out.println("Response Status: " + response.getStatus());
        System.out.println("Response Body: " + response.readEntity(String.class));
    }
}