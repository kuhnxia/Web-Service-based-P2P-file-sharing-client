package kun;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.Arrays;

public class FileViewerApp extends Application {

    private static String sharedFilesDirectory = "/Users/rocky/Desktop/学期记录/Important Courses/CSCI 2122/CSCI2122Code/Lab10/kun/Lab10/deusex";

    @Override
    public void start(Stage primaryStage) {
        // Create the main layout using GridPane
        GridPane cancelSharingGrid = new GridPane();
        cancelSharingGrid.setPadding(new Insets(10));
        cancelSharingGrid.setHgap(10);
        cancelSharingGrid.setVgap(10);

        // Create a label to display the selected file path
        Label selectedFileLabel = new Label("Select file to cancel: ");
        GridPane.setConstraints(selectedFileLabel, 0, 0);

        // Create a ComboBox to display file names
        ComboBox<String> fileComboBox = new ComboBox<>();
        GridPane.setConstraints(fileComboBox, 0, 1);

        showAllSharedFiles(fileComboBox);

        // Set up the file ComboBox change event
        fileComboBox.setOnAction(event -> {
            // Get the selected file name
            String selectedFileName = fileComboBox.getSelectionModel().getSelectedItem();
            if (selectedFileName != null) {
                // Display the selected file path in the label
                selectedFileLabel.setText("Selected File: " + selectedFileName);

                // Perform your desired action with the selected file
                handleFileClick(selectedFileName);
            }
        });

        // Create a button to open the folder
        javafx.scene.control.Button refreshButton = new javafx.scene.control.Button("Refresh");
        refreshButton.setOnAction(event -> showAllSharedFiles(fileComboBox));
        GridPane.setConstraints(refreshButton, 1, 1);

        // Add components to the cancelSharingGrid layout
        cancelSharingGrid.getChildren().addAll(selectedFileLabel, fileComboBox, refreshButton);

        // Set up the scene
        Scene scene = new Scene(cancelSharingGrid, 500, 400);

        // Set up the stage
        primaryStage.setTitle("Cancel Sharing");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to handle file click (customize this for your specific action)
    private void handleFileClick(String fileName) {
        System.out.println("File clicked: " + fileName);
        // Add your logic to perform actions with the selected file
    }

    // Method to open the sharedFilesDirectory folder and populate the file ComboBox
    private void showAllSharedFiles(ComboBox<String> fileComboBox) {
        // Open the sharedFilesDirectory folder using the default file manager
        File sharedFolder = new File(sharedFilesDirectory);
        //Desktop.getDesktop().open(sharedFolder);

        // Populate the file ComboBox with file names from the selected folder
        File[] files = sharedFolder.listFiles();
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

    public static void main(String[] args) {
        launch(args);
    }
}