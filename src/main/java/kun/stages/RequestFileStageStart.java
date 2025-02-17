package kun.stages;

import kun.helpers.LocalFileHelper;
import kun.helpers.StageHelper;
import kun.service.FileShareService;
import kun.sockets.SocketClientThread;

import jakarta.ws.rs.core.Response;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.Arrays;

/**
 * The RequestFileStageStart class represents the stage for requesting a file from other clients
 * in the File Share Client application. It handles the initialization and configuration of the
 * request file UI components.
 *
 * @author Kun Xia
 */
public class RequestFileStageStart {
    private Stage stage;
    private String fileName;
    private Label messageLabel;
    private ProgressBar progressBar;
    private double  progress = 0.0;

    /**
     * Initializes the RequestFileStageStart with the specified stage and file name.
     *
     * @param stage    The stage for the request file UI.
     * @param fileName The name of the file to request.
     */
    public RequestFileStageStart(Stage stage, String fileName) {
        this.stage = stage;
        this.fileName = fileName;
        StageHelper.addStage(stage);
        initialize();
    }

    /**
     * Initializes the request file UI components and sets up the initial stage properties.
     */
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

    /**
     * Configures the layout and appearance of the request file GridPane.
     *
     * @param requestFileGrid The GridPane for the request file UI.
     */
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
        progressBar.setProgress(progress);

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
                showDownloadPopup(selectedId);
            }
        });

        requestFileGrid.add(headerLabel, 0, 0);
        requestFileGrid.add(reminderLabel, 0, 1);
        requestFileGrid.add(fileComboBox, 0, 2);
        requestFileGrid.add(messageLabel, 0, 3);
        requestFileGrid.add(progressBar, 0, 4);
    }

    /**
     * Displays a pop-up confirmation dialog for downloading the file with the selected ID.
     *
     * @param selectedId The selected ID for downloading the file.
     */
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

    /**
     * Retrieves the list of available file IDs for the given file name and populates the ComboBox.
     *
     * @param fileComboBox The ComboBox for selecting the target file ID.
     */
    private void showTargetFileIds(ComboBox<String> fileComboBox) {
        Response response = FileShareService.findSharedFiles(fileName);
        int status = response.getStatus();
        if (response.getStatus() != 200) {
            String message = response.readEntity(String.class);
            messageLabel.setTextFill(Color.RED);
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

    /**
     * Retrieves socket information for the selected client ID, initiates file download,
     * and updates the UI accordingly.
     *
     * @param selectedId The selected client ID for file download.
     */
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

        socketClient.start();

        try {
            socketClient.join();
        } catch (InterruptedException e) {
            messageLabel.setText(e.getMessage());
        }

        String resultMessage = socketClient.getResultMessage();
        messageLabel.setText(resultMessage);

        if (socketClient.isReceived()) {
            //Indicate Download completed.
            progressBar.setProgress(1);
            // Open the receivedFilesDirectory in the system's file explorer
            String openFileMessage = LocalFileHelper.openReceivedFileDirectory(fileName);
            messageLabel.setText(openFileMessage);
        }

    }
}
