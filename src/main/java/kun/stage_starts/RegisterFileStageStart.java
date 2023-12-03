package kun.stage_starts;

import kun.helpers.ClientSocketHelper;
import kun.helpers.LocalFileHelper;
import kun.service.FileShareService;

import jakarta.ws.rs.core.Response;
import javafx.stage.FileChooser;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.*;


public class RegisterFileStageStart {
    private Stage stage;
    // For Register File
    private Button chooseButton;
    private ListView<String> logListView;

    public RegisterFileStageStart(Stage stage) {
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        stage.setTitle("Register File");

        GridPane registerFileGrid = new GridPane();
        configureRegisterFileGrid(registerFileGrid);

        Scene registerFileScene = new Scene(registerFileGrid, 800, 600);
        stage.setScene(registerFileScene);

        stage.show();
    }

    private void configureRegisterFileGrid(GridPane registerFileGrid) {
        registerFileGrid.setAlignment(Pos.CENTER);
        registerFileGrid.setPadding(new Insets(80, 80, 80, 80));
        registerFileGrid.setVgap(30);

        // Set column constraints to center the elements
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(Pos.CENTER.getHpos());
        registerFileGrid.getColumnConstraints().add(columnConstraints);

        chooseButton = new Button("Choose..");
        chooseButton.setStyle("-fx-font-size: 32;");
        logListView = new ListView<>();
        logListView.setMinWidth(600);

        chooseButton.setOnAction(e -> registerFile());

        registerFileGrid.add(chooseButton, 0, 0);
        registerFileGrid.add(logListView, 0, 1);
    }

    private void registerFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Register");

        // Set the initial directory (optional)
        // fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        // Show the file dialog and get the selected file
        Stage stage = (Stage) chooseButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Process the selected file (you can add your logic here)
        if (selectedFile != null) {
            String sourcePath = selectedFile.getAbsolutePath();
            String fileName =  selectedFile.getName().toString().replace(" ", "_");
            logListView.getItems().add(0,"Selected File Path: " + sourcePath);

            // Register file
            Response response = FileShareService .registerFile(fileName, ClientSocketHelper.getIP(), ClientSocketHelper.getPort());
            logListView.getItems().add(0,response.readEntity(String.class));

            if (response.getStatus() == 200) {
                LocalFileHelper.copyFileToSharedFolder(sourcePath);
                logListView.getItems().add(0,"File registered successfully.");
            }


        } else {
            logListView.getItems().add(0,"No file selected.");
        }
        logListView.getItems().add(0,"\n");
    }

}
