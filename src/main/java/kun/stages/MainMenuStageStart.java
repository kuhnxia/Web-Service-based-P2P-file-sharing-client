package kun.stages;

import kun.helpers.ClientSocketHelper;
import kun.helpers.LocalFileHelper;
import kun.helpers.LocalNetworkHelper;
import kun.helpers.StageHelper;

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
import kun.sockets.SocketServerThread;

import java.util.List;
import java.util.Set;

public class MainMenuStageStart {
    private Stage stage;

    // For Start Client Scene.
    private String socketServerAddress;
    private int port;
    private SocketServerThread socketServer;
    private ComboBox<String> ipComboBox;
    private TextField portTextField;
    private Button startClientButton;
    private ListView<String> logListView;

    // For Main Menu Scene

    // For Register File
    private Button registerFileButton;

    // For Cancel
    private Button cancelSharingButton;
    private HBox root;
    private ComboBox<String> fileComboBox;

    // For Search
    private Button searchSharingFilesButton;
    private Button stopClientButton;



    public MainMenuStageStart(Stage stage) {
        this.stage = stage;
        StageHelper.addStage(stage);
        initialize();
    }

    private void initialize() {
        stage.setTitle("File Share Client");

        GridPane startClientGrid = new GridPane();
        configureStartClientGrid(startClientGrid);

        Scene startClientScene = new Scene(startClientGrid, 800, 600);
        stage.setScene(startClientScene);

        // Set the event handler for the stage close request
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                // Remove the closed stage from the set
                StageHelper.removeStage(stage);
            }
        });

        stage.show();
    }

    private void configureStartClientGrid(GridPane startClientGrid) {

        startClientGrid.setAlignment(Pos.CENTER);
        startClientGrid.setPadding(new Insets(80, 80, 80, 80));
        startClientGrid.setVgap(23);

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

        logListView = new ListView<>();

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
        startClientGrid.add(logListView, 0, 5);

    }

    private boolean startSocketServer(){
        try {
            if (ipComboBox.getValue() != null && !ipComboBox.getValue().isEmpty()) {

                socketServerAddress = ipComboBox.getValue();
                ClientSocketHelper.setIP(socketServerAddress);
                port = Integer.parseInt(portTextField.getText());
                ClientSocketHelper.setPort(port);

                if (port >=0 && port <= 65535) {

                    // Find all in use ports, only available on macOS.
                    Boolean isMacOS = System.getProperty("os.name").toLowerCase().contains("mac");
                    if (isMacOS) {
                        Set<Integer> inUsePortList = LocalNetworkHelper.listPortsInUse();
                        if (!inUsePortList.contains(port)) {
                            // Create shared file folder for specific IP and port.
                            LocalFileHelper.createSharedFileDirectory(socketServerAddress, port);

                            // Create server socket
                            socketServer = new SocketServerThread(port);
                            socketServer.start();
                            return true;
                        } else {
                            logListView.getItems().add(0,"Port " + port + " is already in use.\n"
                                    + "Please enter a valid port.\n");
                        }
                    } else {
                        // Create shared file folder for specific IP and port.
                        LocalFileHelper.createSharedFileDirectory(socketServerAddress, port);

                        // Create server socket
                        socketServer = new SocketServerThread(port);
                        socketServer.start();
                        return true;
                    }




                } else {
                    logListView.getItems().add(0,"Please enter a valid port from 0 to 65535.\n");
                }


            } else {
                logListView.getItems().add(0,"Please choose a valid IP.\n");
            }

        } catch (NumberFormatException e){
            logListView.getItems().add(0,"Invalid input. Please enter a valid port.\n");
        } catch (Exception e) {
            logListView.getItems().add(0,"System error.\n");
        }

        return false;
    }

    private void configureMainMenuGrid(GridPane mainMenuGrid) {
        // Set grid property
        mainMenuGrid.setAlignment(Pos.CENTER);
        mainMenuGrid.setPadding(new Insets(80, 80, 80, 80));
        mainMenuGrid.setVgap(34);

        // Set column constraints to center the elements
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(Pos.CENTER.getHpos());
        mainMenuGrid.getColumnConstraints().add(columnConstraints);

        registerFileButton = new Button("Register File");
        cancelSharingButton = new Button("Cancel Sharing");
        searchSharingFilesButton = new Button("Search Sharing");
        stopClientButton = new Button("Stop Client");

        // Set font sizes for main menu buttons
        registerFileButton.setStyle("-fx-font-size: 32;");
        cancelSharingButton.setStyle("-fx-font-size: 32;");
        searchSharingFilesButton.setStyle("-fx-font-size: 32;");
        stopClientButton.setStyle("-fx-font-size: 32;");

        // Set fixed width for buttons
        registerFileButton.setMinWidth(400);
        cancelSharingButton.setMinWidth(400);
        searchSharingFilesButton.setMinWidth(400);
        stopClientButton.setMinWidth(400);

        // Add event handlers for main menu buttons
        registerFileButton.setOnAction(e -> {
            Stage registerStage = new Stage();
            new RegisterFileStageStart(registerStage);
        });
        cancelSharingButton.setOnAction(e -> {
            Stage cancelSharingStage = new Stage();
            new CancelSharingStageStart(cancelSharingStage);
        });
        searchSharingFilesButton.setOnAction(e -> handleSearchSharingFiles());
        stopClientButton.setOnAction(e -> handleStopClient());

        // Add components to main menu GridPane
        mainMenuGrid.add(registerFileButton, 0, 0);
        mainMenuGrid.add(cancelSharingButton, 0, 1);
        mainMenuGrid.add(searchSharingFilesButton, 0, 2);
        mainMenuGrid.add(stopClientButton, 0, 3);
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
        try {
            // Close all stages
            StageHelper.getStages().forEach(Stage::close);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        socketServer.stopServer();
    }
}
