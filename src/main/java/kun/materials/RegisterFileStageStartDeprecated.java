package kun.materials;

import kun.helpers.ClientSocketHelper;
import kun.helpers.LocalFileHelper;
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


public class RegisterFileStageStartDeprecated {
    private Stage stage;
    // For Register File
    private Button chooseButton;
    private TextArea logArea;

    public RegisterFileStageStartDeprecated(Stage stage) {
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
        logArea = new TextArea("Log info:\n");
        logArea.setEditable(false);
        logArea.setMinWidth(600);

        chooseButton.setOnAction(e -> registerFile());

        registerFileGrid.add(chooseButton, 0, 0);
        registerFileGrid.add(logArea, 0, 1);
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
            logArea.appendText("Selected File Path: " + sourcePath + "\n");

            // Register file
            HttpResponse response = null;
            try {
                response = FileShareService.registerFile(fileName, ClientSocketHelper.getIP(), ClientSocketHelper.getPort());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            logArea.appendText(response.body() + "\n");

            if (response.statusCode()== 200)
                LocalFileHelper.copyFileToSharedFolder(sourcePath);

        } else {
            logArea.appendText("No file selected.\n");
        }
    }

}
