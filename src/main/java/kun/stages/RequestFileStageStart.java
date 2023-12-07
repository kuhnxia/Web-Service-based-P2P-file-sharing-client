package kun.stages;

import javafx.concurrent.Task;
import kun.helpers.LocalFileHelper;
import kun.helpers.StageHelper;
import kun.service.FileShareService;

import jakarta.ws.rs.core.Response;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import kun.sockets.SocketClientThread;

import java.util.Arrays;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class RequestFileStageStart {
    private Stage stage;
    private String fileName;
    private Label messageLabel;
    private ProgressBar progressBar;
    public RequestFileStageStart(Stage stage, String fileName) {
        this.stage = stage;
        this.fileName = fileName;
        StageHelper.addStage(stage);
        initialize();
    }

    private void initialize() {
        stage.setTitle("Request File From Other Clients");

        // Set up GridPane.
        GridPane requestFileGrid = new GridPane();
        configureRequestFileGrid(requestFileGrid);

        // Set up the scene
        Scene scene = new Scene(requestFileGrid, 400, 300);
        stage.setScene(scene);

        stage.show();
    }

    private void configureRequestFileGrid(GridPane requestFileGrid) {
        requestFileGrid.setAlignment(Pos.CENTER);
        requestFileGrid.setPadding(new Insets(40, 40, 40, 40));
        requestFileGrid.setVgap(20);

        // Set column constraints to center the elements
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(Pos.CENTER.getHpos());
        requestFileGrid.getColumnConstraints().add(columnConstraints);

        Label headerLabel = new Label("Download from Other Clients...");
        Label reminderLabel = new Label("Available IDs with Target File Name: " + fileName);
        ComboBox<String> fileComboBox = new ComboBox<>();
        fileComboBox.setPromptText("Select ID to Download...");
        messageLabel = new Label();
        progressBar = new ProgressBar();

        headerLabel.setMinWidth(330);
        reminderLabel.setMaxWidth(330);
        messageLabel.setMaxWidth(330);
        progressBar.setMaxWidth(330);

        headerLabel.setStyle("-fx-font-size: 20;");
        reminderLabel.setStyle("-fx-font-size: 14;");
        fileComboBox.setStyle("-fx-font-size: 18;");
        messageLabel.setStyle("-fx-font-size: 11;");

        reminderLabel.setWrapText(true);
        messageLabel.setWrapText(true);

        showTargetFileIds(fileComboBox);


        // Set up the file ComboBox change event
        fileComboBox.setOnAction(event -> {
            // Get the selected file name
            String selectedId = fileComboBox.getSelectionModel().getSelectedItem();
            if (selectedId != null) {
                // Display the selected ID.
                messageLabel.setText("Selected ID: " + selectedId);

                // Perform action.
                showDownloadPopup(selectedId);
            }
        });

        requestFileGrid.add(headerLabel, 0, 0);
        requestFileGrid.add(reminderLabel, 0, 1);
        requestFileGrid.add(fileComboBox, 0, 2);
        requestFileGrid.add(messageLabel, 0, 3);
        requestFileGrid.add(progressBar, 0, 4);
    }

    private void showDownloadPopup(String selectedId) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Download Alert");
        alert.setContentText("Are you sure you want to download target file with ID: " + selectedId);

        // Add an OK button to the pop-up
        ButtonType okButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(okButton);

        // Show the pop-up and wait for the user's response
        alert.showAndWait().ifPresent(response -> {
            getSocketInfoAndRequestFile(selectedId);
        });
    }

    private void showTargetFileIds(ComboBox<String> fileComboBox) {
        Response response = FileShareService.findSharedFiles(fileName);
        int status = response.getStatus();
        if (response.getStatus() != 200) {
            String message = response.readEntity(String.class);
            messageLabel.setText(status +" " + response.getStatusInfo() + ": " + message);
        } else {
            String searchResult = response.readEntity(String.class);
            String[] fileIds = searchResult.split(" ");

            // Clear previous items
            fileComboBox.getItems().clear();
            // Add file names to the ComboBox
            Arrays.stream(fileIds)
                    .forEach(fileComboBox.getItems()::add);
        }
    }

    private void getSocketInfoAndRequestFile(String selectedId) {
        int id = Integer.parseInt(selectedId);

        // Get client information
        Response response = FileShareService.getSocketAddressById(id);
        String responseMessage = response.readEntity(String.class);
        if (response.getStatus() != 200) {
            messageLabel.setText(responseMessage);
            return;
        }

        messageLabel.setText("Downloading from " + responseMessage + "...");
        String[] socketAddress = responseMessage.split(":");
        String clientIP =socketAddress[0];
        int clientPort = Integer.parseInt(socketAddress[1]);

        // Request and send the file.
        SocketClientThread socketClient = new SocketClientThread(fileName, clientIP, clientPort);

        // Set up a task for the progress bar
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                socketClient.start();
                try {
                    socketClient.join();
                } catch (InterruptedException e) {
                    messageLabel.setText(e.getMessage());
                }
                return null;
            }
        };

        // Bind the progress property of the progress bar to the task progress
        progressBar.progressProperty().bind(task.progressProperty());

        // Set up actions to be performed after the task completes
        task.setOnSucceeded(event -> {
            String resultMessage = socketClient.getResultMessage();
            messageLabel.setText(resultMessage);

            if (socketClient.isReceived()) {
                // Open the receivedFilesDirectory in the system's file explorer
                String receivedFilesDirectory = LocalFileHelper.getReceivedFilesDirectory();
                File receivedFile = new File(receivedFilesDirectory + File.separator + fileName);
                try {
                    Desktop.getDesktop().open(receivedFile);
                } catch (IOException e) {
                    messageLabel.setText("Error opening the file directory: " + e.getMessage());
                }
            }

        });
        task.setOnFailed(event -> {
            messageLabel.setText("Error downloading the file.");
        });

        // Start the task in a new thread
        Thread thread = new Thread(task);
        thread.start();

    }
}
