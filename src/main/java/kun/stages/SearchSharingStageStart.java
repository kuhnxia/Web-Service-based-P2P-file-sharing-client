package kun.stages;

import kun.helpers.StageHelper;
import kun.service.FileShareService;

import jakarta.ws.rs.core.Response;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * The SearchSharingStageStart class represents the stage for searching shared files
 * in the File Share Client application. It handles the initialization and configuration of the
 * search sharing UI components.
 *
 * @author Kun Xia
 */
public class SearchSharingStageStart{
    private Stage stage;
    private TextField nameTextField;
    private Label messageLabel;
    private String fileName;

    /**
     * Initializes the SearchSharingStageStart with the specified stage.
     *
     * @param stage The stage for the search sharing UI.
     */
    SearchSharingStageStart(Stage stage) {
        this.stage = stage;
        StageHelper.addStage(stage);
        initialize();
    }

    /**
     * Initializes the search sharing UI components and sets up the initial stage properties.
     */
    private void initialize() {
        stage.setTitle("Search Sharing");

        GridPane SearchSharingGrid = new GridPane();
        configureSearchSharingGrid(SearchSharingGrid);

        Scene searchSharingScene = new Scene(SearchSharingGrid, 500, 375);
        stage.setScene(searchSharingScene);

        stage.show();
    }

    /**
     * Configures the layout and appearance of the search sharing GridPane.
     *
     * @param searchSharingGrid The GridPane for the search sharing UI.
     */
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

    /**
     * Performs a search for shared files based on the entered file name.
     * If files are found, it opens a new RequestFileStageStart for further action.
     *
     * @return True if shared files are found; otherwise, false.
     */
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
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText(status +" " + response.getStatusInfo() + ": " + message);
            } else {
                found = true;
                messageLabel.setTextFill(Color.WHITE);
                messageLabel.setText("");
            }

        }
        return found;
    }
}
