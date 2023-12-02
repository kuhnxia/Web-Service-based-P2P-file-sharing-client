package kun;

import kun.service.FileShareService;
import kun.helpers.LocalFileHelper;
import kun.helpers.LocalIPAddressHelper;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.TextInputDialog;

import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

public class FileShareClientFXCopy extends Application {

    private FileShareService fileShare;
    private TextField filePathField;
    private TextField portField;
    private TextArea logTextArea;
    private String socketServerAddress;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        fileShare = new FileShareService();

        primaryStage.setTitle("File Share Client");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(40, 40, 40, 40));
        grid.setVgap(20);
        grid.setHgap(20);

        addComponentsToGrid(grid);

        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void addComponentsToGrid(GridPane grid) {
        Label filePathLabel = new Label("File Path:");
        filePathField = new TextField();
        Label portLabel = new Label("Port:");
        portField = new TextField();
        Button registerButton = new Button("Register File");
        logTextArea = new TextArea();

        registerButton.setOnAction(e -> registerFile());

        grid.add(filePathLabel, 0, 0);
        grid.add(filePathField, 1, 0);
        grid.add(portLabel, 0, 1);
        grid.add(portField, 1, 1);
        grid.add(registerButton, 1, 2);
        grid.add(logTextArea, 0, 3, 2, 1);
    }

    private void registerFile() {
        // Get the port from user input
        int port;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            logTextArea.appendText("Invalid port. Please enter a valid integer.\n");
            return;
        }

        // Get the socket server address using the provided method
        try {
            socketServerAddress = getSocketServerAddress();
        } catch (Exception ex) {
            logTextArea.appendText("Error getting socket server address: " + ex.getMessage() + "\n");
            return;
        }

        String sourcePath = filePathField.getText();
        String fileName = LocalFileHelper.getFilenameFromPath(sourcePath);
        Boolean copied = LocalFileHelper.copyFileToSharedFolder(sourcePath);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (copied) {
            String message = fileShare.registerFile(fileName, socketServerAddress, port);
            logTextArea.appendText(message + "\n");
        } else {
            logTextArea.appendText("Invalid file path.\n");
        }
    }

    private String getSocketServerAddress() {
        List<String> inet4Addresses = LocalIPAddressHelper.getLocalIPAddresses();

        if (inet4Addresses.size() == 1) {
            return inet4Addresses.get(0);
        } else {
            System.out.println("You are using not only one network interface, such as Ethernet, WiFi, Cellular, VPN.\n" +
                    "Choose the correct IP that the router assigned to you in your local network: \n");
            int i = 1;
            for (String inet4Address : inet4Addresses) {
                System.out.printf("Enter %d if you will use local IP: %s\n\n", i, inet4Address);
                i++;
            }

            Optional<String> result = showInputDialog();
            if (result.isPresent()) {
                int choice = Integer.parseInt(result.get());
                if (choice > 0 && choice < i) {
                    return inet4Addresses.get(choice - 1);
                }
            }

            throw new RuntimeException("Invalid user input or canceled.");
        }
    }

    private Optional<String> showInputDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Network Interface Selection");
        dialog.setHeaderText("Choose the correct IP address");

        return dialog.showAndWait();
    }
}