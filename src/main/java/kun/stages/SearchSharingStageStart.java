package kun.stages;

import jakarta.ws.rs.core.Response;
import javafx.application.Application;
import kun.helpers.StageHelper;
import kun.service.FileShareService;
import kun.stages.RequestFileStageStart;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class SearchSharingStageStart{
    private Stage stage;
    private TextField nameTextField;
    private Label messageLabel;
    private String fileName;

    SearchSharingStageStart(Stage stage) {
        this.stage = stage;
        StageHelper.addStage(stage);
        initialize();
    }

    private void initialize() {
        stage.setTitle("Search Sharing");

        GridPane SearchSharingGrid = new GridPane();
        configureSearchSharingGrid(SearchSharingGrid);

        Scene searchSharingScene = new Scene(SearchSharingGrid, 500, 375);
        stage.setScene(searchSharingScene);

        stage.show();
    }

    private void configureSearchSharingGrid(GridPane searchSharingGrid) {
        searchSharingGrid.setAlignment(Pos.CENTER);
        searchSharingGrid.setPadding(new Insets(40, 40, 40, 40));
        searchSharingGrid.setVgap(25);

        // Set column constraints to center the elements
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(Pos.CENTER.getHpos());
        searchSharingGrid.getColumnConstraints().add(columnConstraints);

        Label reminderLabel = new Label("Enter a File Name:");
        nameTextField = new TextField();
        Button searchButton = new Button("Click to Search");
        messageLabel = new Label();

        reminderLabel.setStyle("-fx-font-size: 24;");
        nameTextField.setStyle("-fx-font-size: 18;");
        searchButton.setStyle("-fx-font-size: 24;");
        messageLabel.setStyle("-fx-font-size: 12;");
        reminderLabel.setMaxWidth(375);
        nameTextField.setMaxWidth(375);
        searchButton.setMaxWidth(375);
        messageLabel.setMaxWidth(375);
        messageLabel.setWrapText(true);

        searchButton.setOnAction(e -> {
            if (searchSharing()) {
                Stage requestFileStage = new Stage();
                new RequestFileStageStart(requestFileStage, fileName);
            }
        });

        searchSharingGrid.add(reminderLabel, 0, 0);
        searchSharingGrid.add(nameTextField, 0, 1);
        searchSharingGrid.add(searchButton, 0, 2);
        searchSharingGrid.add(messageLabel, 0, 3);


    }

    private boolean searchSharing() {
        boolean found =false;

        fileName = nameTextField.getText();
        if (fileName.equals("")) {
            messageLabel.setText("Nothing Enter.");
        } else {
            fileName = fileName.replace(" ", "_");
            Response response = FileShareService.findSharedFiles(fileName);
            String message = response.readEntity(String.class);
            int status = response.getStatus();
            if (response.getStatus() != 200) {
                messageLabel.setText(status +" " + response.getStatusInfo() + ": " + message);
            } else {
                found = true;
            }

        }
        return found;
    }
}
