package kun.stage_starts;

import kun.helpers.LocalFileHelper;
import kun.helpers.LocalNetworkHelper;
import kun.sockets.SocketServerThread;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Set;

public class MainMenuStageStart {
    private Stage stage;

    // For Start Client Scene.
    private String socketServerAddress;
    private int port;
    private ComboBox<String> ipComboBox;
    private TextField portTextField;
    private Button startClientButton;
    private TextArea logTextArea;

    // For Main Menu Scene
    private Button registerFileButton;
    private Button cancelSharingButton;
    private Button searchSharingFilesButton;
    private Button stopClientButton;

    public MainMenuStageStart(Stage stage) {
        this.stage = stage;
        initialize();
    }

    private void initialize() {
        stage.setTitle("File Share Client");

        GridPane startClientGrid = new GridPane();
        configureStartClientGrid(startClientGrid);

        Scene startClientScene = new Scene(startClientGrid, 800, 600);
        stage.setScene(startClientScene);

        stage.show();
    }

    private void configureStartClientGrid(GridPane startClientGrid) {

        startClientGrid.setAlignment(Pos.CENTER);
        startClientGrid.setPadding(new Insets(80, 80, 80, 80));
        startClientGrid.setVgap(20);

        // Create components
        Label ipLabel = new Label("Choose your correct local IP:");
        ipComboBox = new ComboBox<>();
        ipComboBox.setPromptText("Select IP");

        // Populate IP addresses in the combo box
        List<String> ipAddresses = LocalNetworkHelper.getLocalIPAddresses();
        ipComboBox.setItems(FXCollections.observableArrayList(ipAddresses));

        Label portLabel = new Label("Enter the server port you like:");
        portTextField = new TextField();
        // Set a max width for the port text field
        portTextField.setMaxWidth(200);

        startClientButton = new Button("Start Client");
        // Start server socket and direct to main menu scene.
        startClientButton.setOnAction(e -> {
            if (startSocketServer()) {
                GridPane mainMenuGrid = new GridPane();
                configureMainMenuGrid(mainMenuGrid);

                Scene mainMenuScene = new Scene(mainMenuGrid, 800, 600);
                stage.setScene(mainMenuScene);
            }
        });

        logTextArea = new TextArea();
        logTextArea.setEditable(false);

        // Set font sizes
        ipLabel.setStyle("-fx-font-size: 32;");
        ipComboBox.setStyle("-fx-font-size: 32;");
        portLabel.setStyle("-fx-font-size: 32;");
        portTextField.setStyle("-fx-font-size: 32;");
        startClientButton.setStyle("-fx-font-size: 32;");

        // Set column constraints to center the elements
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(Pos.CENTER.getHpos());
        startClientGrid.getColumnConstraints().add(columnConstraints);

        // Add components to GridPane
        startClientGrid.add(ipLabel, 0, 0);
        startClientGrid.add(ipComboBox, 0, 1);
        startClientGrid.add(portLabel, 0, 2);
        startClientGrid.add(portTextField, 0, 3);
        startClientGrid.add(startClientButton, 0, 4);
        startClientGrid.add(logTextArea, 0, 5);

    }

    private boolean startSocketServer(){
        try {
            if (ipComboBox.getValue() != null && !ipComboBox.getValue().isEmpty()) {

                socketServerAddress = ipComboBox.getValue();
                port = Integer.parseInt(portTextField.getText());

                if (port >=0 && port <= 65535) {
                    // Find all in use ports
                    Set<Integer> inUsePortList = LocalNetworkHelper.listPortsInUse();

                    if (!inUsePortList.contains(port)) {
                        // Create shared file folder for specific IP and port.
                        LocalFileHelper.createSharedFileDirectory(socketServerAddress, port);

                        // Create server socket
                        SocketServerThread socketServer = new SocketServerThread(port);
                        socketServer.start();
                        return true;
                    } else {
                        logTextArea.appendText("Port " + port + " is already in use.\n"
                                + "Please enter a valid port.\n");
                    }


                } else {
                    logTextArea.appendText("Please enter a valid port from 0 to 65535.\n");
                }


            } else {
                logTextArea.appendText("Please choose a valid IP.\n");
            }

        } catch (NumberFormatException e){
            logTextArea.appendText("Invalid input. Please enter a valid port.\n");
        } catch (Exception e) {
            logTextArea.appendText("System error.\n");
        }

        return false;
    }

    private void configureMainMenuGrid(GridPane mainMenuGrid) {
        // Create components for the main menu
        mainMenuGrid.setAlignment(Pos.CENTER);
        mainMenuGrid.setPadding(new Insets(80, 80, 80, 80));
        mainMenuGrid.setVgap(20);

        registerFileButton = new Button("Register File");
        cancelSharingButton = new Button("Cancel Sharing");
        searchSharingFilesButton = new Button("Search Sharing Files");
        stopClientButton = new Button("Stop Client");

        // Set font sizes for main menu buttons
        registerFileButton.setStyle("-fx-font-size: 24;");
        cancelSharingButton.setStyle("-fx-font-size: 24;");
        searchSharingFilesButton.setStyle("-fx-font-size: 24;");
        stopClientButton.setStyle("-fx-font-size: 24;");

        // Add event handlers for main menu buttons
        registerFileButton.setOnAction(e -> handleRegisterFile());
        cancelSharingButton.setOnAction(e -> handleCancelSharing());
        searchSharingFilesButton.setOnAction(e -> handleSearchSharingFiles());
        stopClientButton.setOnAction(e -> handleStopClient());

        // Add components to main menu GridPane
        mainMenuGrid.add(registerFileButton, 0, 0);
        mainMenuGrid.add(cancelSharingButton, 0, 1);
        mainMenuGrid.add(searchSharingFilesButton, 0, 2);
        mainMenuGrid.add(stopClientButton, 0, 3);
    }

    private void handleRegisterFile() {
        // Implement logic for handling "Register File" button click
        // Add code to navigate to the corresponding scene or perform the desired action
        //logTextArea.appendText("Register File button clicked.\n");
    }

    private void handleCancelSharing() {
        // Implement logic for handling "Cancel Sharing" button click
        // Add code to navigate to the corresponding scene or perform the desired action
        //logTextArea.appendText("Cancel Sharing button clicked.\n");
    }

    private void handleSearchSharingFiles() {
        // Implement logic for handling "Search Sharing Files" button click
        // Add code to navigate to the corresponding scene or perform the desired action
        //logTextArea.appendText("Search Sharing Files button clicked.\n");
    }

    private void handleStopClient() {
        // Implement logic for handling "Stop Client" button click
        // Add code to navigate to the corresponding scene or perform the desired action
        //logTextArea.appendText("Stop Client button clicked.\n");
    }
}
