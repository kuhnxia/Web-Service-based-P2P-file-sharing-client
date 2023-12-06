package kun.stages;

import kun.helpers.ClientSocketHelper;
import kun.helpers.LocalFileHelper;
import kun.helpers.StageHelper;
import kun.service.FileShareService;

import javafx.stage.FileChooser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.net.http.HttpResponse;


public class RegisterFileStageStart {
    private Stage stage;
    // For Register File
    private Button chooseButton;
    private Label messageLabel;

    public RegisterFileStageStart(Stage stage) {
        this.stage = stage;
        StageHelper.addStage(stage);
        initialize();
    }

    private void initialize() {
        stage.setTitle("Register File");

        GridPane registerFileGrid = new GridPane();
        configureRegisterFileGrid(registerFileGrid);

        Scene registerFileScene = new Scene(registerFileGrid, 400, 300);
        stage.setScene(registerFileScene);

        stage.show();
    }

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
        messageLabel = new Label();
        messageLabel.setStyle("-fx-font-size: 11;");
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(300);

        chooseButton.setOnAction(e -> registerFile());

        registerFileGrid.add(chooseButton, 0, 0);
        registerFileGrid.add(messageLabel, 0, 1);
    }

    private void registerFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Register");

        // Set the initial directory (optional)
        String registerMessage = "";

        // Show the file dialog and get the selected file
        Stage stage = (Stage) chooseButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Process the selected file (you can add your logic here)
        if (selectedFile != null) {
            String sourcePath = selectedFile.getAbsolutePath();
            String fileName =  selectedFile.getName().toString().replace(" ", "_");
            registerMessage += "Selected File Path: " + sourcePath + "\n";

            // Register file
            HttpResponse response = null;
            try {
                response = FileShareService.registerFile(fileName, ClientSocketHelper.getIP(), ClientSocketHelper.getPort());
                registerMessage += response.body() + "\n";
                if (response.statusCode() == 200) {
                    LocalFileHelper.copyFileToSharedFolder(sourcePath);
                    registerMessage += "The Shared File is Saved!\n";
                }
            } catch (Exception e) {
                registerMessage += e.getMessage() + "\n";
            }

        } else {
            registerMessage += "No File Selected.\n";
        }

        messageLabel.setText(registerMessage);
    }

}
