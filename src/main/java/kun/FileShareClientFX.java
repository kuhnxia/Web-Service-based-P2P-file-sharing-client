package kun;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import kun.helpers.LocalFileHelper;
import kun.helpers.LocalIPAddressHelper;
import kun.sockets.SocketServerThread;

import java.util.List;

public class FileShareClientFX extends Application {
    private String socketServerAddress;
    private int port;
    private ComboBox<String> ipComboBox;
    private TextField portTextField;
    private TextArea logTextArea;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("File Share Client");

        // Layout setup using GridPane
        GridPane startClientGrid = new GridPane();
        startClientGrid.setAlignment(Pos.CENTER);
        startClientGrid.setPadding(new Insets(80, 80, 80, 80));
        startClientGrid.setVgap(20);

        addComponentsToStartClientGrid(startClientGrid);

        Scene startClientScene = new Scene(startClientGrid, 800, 600);
        primaryStage.setScene(startClientScene);

        primaryStage.show();
    }

    private void addComponentsToStartClientGrid(GridPane startClientGrid) {
        // Create components
        Label ipLabel = new Label("Choose your correct local IP:");
        ipComboBox = new ComboBox<>();
        ipComboBox.setPromptText("Select IP");

        // Populate IP addresses in the combo box
        List<String> ipAddresses = LocalIPAddressHelper.getLocalIPAddresses();
        ipComboBox.setItems(FXCollections.observableArrayList(ipAddresses));

        Label portLabel = new Label("Enter the server port you like:");
        portTextField = new TextField();
        // Set a max width for the port text field
        portTextField.setMaxWidth(200);

        Button startButton = new Button("Start Client");
        startButton.setOnAction(e -> startSocketServer());

        // TextArea for logging
        logTextArea = new TextArea();
        logTextArea.setEditable(false);

        // Set font sizes
        ipLabel.setStyle("-fx-font-size: 32;");
        ipComboBox.setStyle("-fx-font-size: 32;");
        portLabel.setStyle("-fx-font-size: 32;");
        portTextField.setStyle("-fx-font-size: 32;");
        startButton.setStyle("-fx-font-size: 32;");

        // Set column constraints to center the elements
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(Pos.CENTER.getHpos());
        startClientGrid.getColumnConstraints().add(columnConstraints);

        // Add components to GridPane
        startClientGrid.add(ipLabel, 0, 0);
        startClientGrid.add(ipComboBox, 0, 1);
        startClientGrid.add(portLabel, 0, 2);
        startClientGrid.add(portTextField, 0, 3);
        startClientGrid.add(startButton, 0, 4);
        startClientGrid.add(logTextArea, 0, 5);

    }

    private void startSocketServer() {
        try {
            if (ipComboBox.getValue() != null && !ipComboBox.getValue().isEmpty()) {

                socketServerAddress = ipComboBox.getValue();
                port = Integer.parseInt(portTextField.getText());

                if (port >=0 && port <= 65535) {
                    LocalFileHelper.createSharedFileDirectory(socketServerAddress, port);

                    SocketServerThread socketServer = new SocketServerThread(port);
                    socketServer.start();

                    logTextArea.appendText("\nFile Sharing Socket Server started at:\n");
                    logTextArea.appendText(socketServerAddress + ":" + port +"\n");
                } else {
                    logTextArea.appendText("Please enter a valid port from 0 to 65535.\n");
                }


            } else {
                logTextArea.appendText("Please choose a valid IP.\n");
            }

        } catch (NumberFormatException e){
            // Logging to TextArea
            logTextArea.appendText("Invalid input. Please enter a valid port.\n");
        } catch (Exception e) {
            logTextArea.appendText("System error.\n");
        }
    }
}
