package kun.stages;

import kun.helpers.ClientSocketHelper;
import kun.helpers.LocalFileHelper;
import kun.helpers.StageHelper;
import kun.service.FileShareService;

import jakarta.ws.rs.core.Response;

import javafx.stage.FileChooser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;

/**
 * The RegisterFileStageStart class represents the stage for registering a file in the File Share Client application.
 * It handles the initialization and configuration of the register file UI components.
 *
 * @author Kun Xia
 */
public class RegisterFileStageStart {
    private Stage stage;
    // For Register File
    private Button chooseButton;
    private Label selectMessageLabel;
    private Label responseMessageLabel;
    private Label savedMessageLabel;


    /**
     * Initializes the RegisterFileStageStart with the specified stage.
     *
     * @param stage The stage for the register file UI.
     */
    public RegisterFileStageStart(Stage stage) {
        this.stage = stage;
        StageHelper.addStage(stage);
        initialize();
    }

    /**
     * Initializes the register file UI components and sets up the initial stage properties.
     */
    private void initialize() {
        stage.setTitle("Register File");

        GridPane registerFileGrid = new GridPane();
        configureRegisterFileGrid(registerFileGrid);

        Scene registerFileScene = new Scene(registerFileGrid, 400, 300);
        stage.setScene(registerFileScene);

        stage.show();
    }

    /**
     * Configures the layout and appearance of the register file GridPane.
     *
     * @param registerFileGrid The GridPane for the register file UI.
     */
    private void configureRegisterFileGrid(GridPane registerFileGrid) {
        registerFileGrid.setAlignment(Pos.CENTER);
        registerFileGrid.setPadding(new Insets(40, 40, 40, 40));
        registerFileGrid.setVgap(20);

        // Set column constraints to center the elements
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(Pos.CENTER.getHpos());
        registerFileGrid.getColumnConstraints().add(columnConstraints);

        chooseButton = new Button("Select File to Register..");
        chooseButton.setStyle("-fx-font-size: 24;");
        chooseButton.setMaxWidth(300);

        selectMessageLabel = new Label();
        selectMessageLabel.setStyle("-fx-font-size: 11;");
        selectMessageLabel.setWrapText(true);
        selectMessageLabel.setMaxWidth(300);

        responseMessageLabel = new Label();
        responseMessageLabel.setStyle("-fx-font-size: 11;");
        responseMessageLabel.setWrapText(true);
        responseMessageLabel.setMaxWidth(300);

        savedMessageLabel= new Label();
        savedMessageLabel.setStyle("-fx-font-size: 11;");
        savedMessageLabel.setWrapText(true);
        savedMessageLabel.setMaxWidth(300);

        chooseButton.setOnAction(e -> registerFile());

        registerFileGrid.add(chooseButton, 0, 0);
        registerFileGrid.add(selectMessageLabel, 0, 1);
        registerFileGrid.add(responseMessageLabel, 0, 2);
        registerFileGrid.add(savedMessageLabel, 0, 3);
    }

    /**
     * Handles the event when the user clicks the "Select File to Register" button.
     * Opens a file dialog, registers the selected file, and provides feedback.
     */
    private void registerFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Register");

        // Show the file dialog and get the selected file
        Stage stage = (Stage) chooseButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Process the selected file
        if (selectedFile != null) {
            String sourcePath = selectedFile.getAbsolutePath();
            String fileName =  selectedFile.getName().toString().replace(" ", "_");
            selectMessageLabel.setText("Selected File Path: " + sourcePath);

            // Register file
            Response response = FileShareService .registerFile(fileName, ClientSocketHelper.getIP(), ClientSocketHelper.getPort());
            String message = response.readEntity(String.class);
            int status = response.getStatus();

            if (status == 200) {
                responseMessageLabel.setTextFill(Color.BLUE);
            } else responseMessageLabel.setTextFill(Color.RED);

            responseMessageLabel.setText(status +" " + response.getStatusInfo() + ": " + message);

            if (status == 200) {
                LocalFileHelper.copyFileToSharedFolder(sourcePath);
                savedMessageLabel.setText("The Shared File is Saved!");
            } else savedMessageLabel.setText("");


        } else {
            selectMessageLabel.setText("No File Selected.");
            responseMessageLabel.setText("");
            savedMessageLabel.setText("");
        }
    }

}
