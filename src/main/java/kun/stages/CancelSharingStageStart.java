package kun.stages;

import kun.helpers.ClientSocketHelper;
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

import java.io.File;
import java.util.Arrays;


public class CancelSharingStageStart {
    private Stage stage;
    private Label messageLabel;

    public CancelSharingStageStart(Stage stage) {
        this.stage = stage;
        StageHelper.addStage(stage);
        initialize();
    }

    private void initialize() {
        stage.setTitle("Cancel Sharing");

        // Set up GridPane.
        GridPane cancelSharingGrid = new GridPane();
        configureCancelSharingGrid(cancelSharingGrid);

        // Set up the scene
        Scene scene = new Scene(cancelSharingGrid, 400, 300);
        stage.setScene(scene);

        stage.show();
    }

    private void configureCancelSharingGrid(GridPane cancelSharingGrid) {
        cancelSharingGrid.setAlignment(Pos.CENTER);
        cancelSharingGrid.setPadding(new Insets(40, 40, 40, 40));
        cancelSharingGrid.setVgap(20);

        // Set column constraints to center the elements
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(Pos.CENTER.getHpos());
        cancelSharingGrid.getColumnConstraints().add(columnConstraints);

        Button refreshButton = new Button("Refresh Current Sharing");
        ComboBox<String> fileComboBox = new ComboBox<>();
        fileComboBox.setPromptText("Select File to Cancel");
        messageLabel = new Label();

        refreshButton.setMinWidth(330);
        refreshButton.setStyle("-fx-font-size: 20;");
        fileComboBox.setStyle("-fx-font-size: 18;");
        messageLabel.setStyle("-fx-font-size: 11;");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(330);

        showAllSharedFiles(fileComboBox);
        refreshButton.setOnAction(refreshEvent -> {
            showAllSharedFiles(fileComboBox);
            fileComboBox.setPromptText("Select File to Cancel");
            messageLabel.setText("Current Sharing Files Refreshed!");
        });

        // Set up the file ComboBox change event
        fileComboBox.setOnAction(event -> {
            // Get the selected file name
            String selectedFileName = fileComboBox.getSelectionModel().getSelectedItem();
            if (selectedFileName != null) {
                // Display the selected file
                messageLabel.setText("Selected File: " + selectedFileName);

                // Perform action.
                showInformationPopup(selectedFileName);

            }
        });

        cancelSharingGrid.add(refreshButton, 0, 0);
        cancelSharingGrid.add(fileComboBox, 0, 1);
        cancelSharingGrid.add(messageLabel, 0, 2);
    }

    private void showInformationPopup(String fileName) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cancel Sharing Alert");
        alert.setContentText("Are you sure you want to cancel sharing of the file:\n" + fileName);

        // Add an OK button to the pop-up
        ButtonType okButton = new ButtonType("OK");
        alert.getButtonTypes().setAll(okButton);

        // Show the pop-up and wait for the user's response
        alert.showAndWait().ifPresent(response -> {
                handleFileClick(fileName);
        });
    }

    // Method to open the sharedFilesDirectory folder and populate the file ComboBox
    private void showAllSharedFiles(ComboBox<String> fileComboBox) {

        // Populate the file ComboBox with file names from the selected folder.
        File[] files = LocalFileHelper.getFilesFromSharedFileDirectory();
        if (files != null) {
            // Clear previous items
            fileComboBox.getItems().clear();

            // Add file names to the ComboBox
            Arrays.stream(files)
                    .filter(File::isFile)
                    .map(File::getName)
                    .forEach(fileComboBox.getItems()::add);
        }
    }

    private void handleFileClick(String fileName) {
        Response response = FileShareService.cancelSharing(
                fileName, ClientSocketHelper.getIP(), ClientSocketHelper.getPort());

        String message = response.readEntity(String.class);
        int status = response.getStatus();
        messageLabel.setText(status +" " + response.getStatusInfo() + ": " + message);
        if (status == 200)
            LocalFileHelper.deleteFileFromSharedFolder(fileName);
    }
}
