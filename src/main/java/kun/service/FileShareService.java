package kun.service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class FileShareService {

    // Change to the real URL of your web service.
    private static final String BASE_URI = "http://localhost:8080/share";

    public static HttpResponse<String> registerFile(String fileName, String ipAddress, int port) throws Exception {
        // Build the form data
        Map<String, String> formData = Map.of(
                "fileName", fileName,
                "ipAddress", ipAddress,
                "port", Integer.toString(port)
        );

        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URI + "/registerFile"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(buildFormDataFromMap(formData))
                .build();

        // Send the request and handle the response
        return HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> cancelSharing(String fileName, String ipAddress, int port) throws Exception {
        // Build the form data
        Map<String, String> formData = Map.of(
                "fileName", fileName,
                "ipAddress", ipAddress,
                "port", Integer.toString(port)
        );

        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URI + "/cancelSharing"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(buildFormDataFromMap(formData))
                .build();

        // Send the request and handle the response
        return HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> findSharedFiles(String fileName) throws Exception {
        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URI + "/findSharedFiles/" + fileName))
                .GET()
                .build();

        // Send the request and handle the response
        return HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> getSocketAddressById(int id) throws Exception {
        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(BASE_URI + "/getSocketAddressById/" + id))
                .GET()
                .build();

        // Send the request and handle the response
        return HttpClient.newHttpClient()
                .send(request, HttpResponse.BodyHandlers.ofString());
    }

    // Helper method to convert a Map to a URL-encoded form data string
    private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<String, String> data) {
        var builder = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }
}