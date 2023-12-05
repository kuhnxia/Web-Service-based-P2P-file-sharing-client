package kun.materials;

import javafx.application.Application;
import kun.helpers.ClientSocketHelper;
import kun.helpers.LocalFileHelper;
import kun.helpers.LocalNetworkHelper;
import kun.helpers.StageHelper;
import kun.sockets.SocketServerThread;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;

import java.util.List;
import java.util.Set;


public class SearchSharingApp extends Application {
    private Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        SearchSharingStageStart(primaryStage);
    }

    private void SearchSharingStageStart(Stage stage) {
        this.stage = stage;
        StageHelper.addStage(stage);
        initialize();
    }

    private void initialize() {
        stage.setTitle("Search Sharing");

        GridPane SearchSharingGrid = new GridPane();
        configureSearchSharingGrid(SearchSharingGrid);

        Scene searchSharingScene = new Scene(SearchSharingGrid, 600, 400);
        stage.setScene(searchSharingScene);

        stage.show();
    }

    private void configureSearchSharingGrid(GridPane searchSharingGrid) {
        searchSharingGrid.setAlignment(Pos.CENTER);
        searchSharingGrid.setPadding(new Insets(80, 80, 80, 80));
        searchSharingGrid.setVgap(20);

        Label reminderLabel = new Label("Enter a File Name to Search:");
        TextField nameTextField = new TextField();
        Button searchButton = new Button("Click to Search");
        Label messageLabel = new Label();


        // Morning to continue!!!


    }
}
