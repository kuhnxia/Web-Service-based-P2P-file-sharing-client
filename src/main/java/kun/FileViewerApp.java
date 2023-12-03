package kun;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class FileViewerApp extends Application {

    private static String sharedFilesDirectory = "/Users/rocky/Web-Service-based-P2P-file-sharing-application/shared_files/198_18_0_1_7878/";

    @Override
    public void start(Stage primaryStage) {
        // Create the main layout
        HBox root = new HBox();
        root.setPadding(new Insets(10));
        root.setSpacing(10);

        // Create a label to display the selected file path
        Label selectedFileLabel = new Label("Selected File: ");

        // Create a ComboBox to display file names
        ComboBox<String> fileComboBox = new ComboBox<>();

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
        javafx.scene.control.Button openFolderButton = new javafx.scene.control.Button("Open Folder");
        openFolderButton.setOnAction(event -> chooseFolder(fileComboBox));

        // Add components to the root layout
        root.getChildren().addAll(openFolderButton, fileComboBox, selectedFileLabel);

        // Set up the scene
        Scene scene = new Scene(root, 400, 100);

        // Set up the stage
        primaryStage.setTitle("File Viewer App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to handle file click (customize this for your specific action)
    private void handleFileClick(String fileName) {
        System.out.println("File clicked: " + fileName);
        // Add your logic to perform actions with the selected file
    }

    // Method to open the sharedFilesDirectory folder and populate the file ComboBox
    private void chooseFolder(ComboBox<String> fileComboBox) {
        // Open the sharedFilesDirectory folder using the default file manager
        try {
            File sharedFolder = new File(sharedFilesDirectory);
            Desktop.getDesktop().open(sharedFolder);

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
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}