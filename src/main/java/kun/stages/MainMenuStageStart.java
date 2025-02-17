package kun.stages;

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

import java.util.List;
import java.util.Set;

/**
 * The MainMenuStageStart class represents the main menu stage of the File Share Client application.
 * It handles the initialization and configuration of the main menu UI components.
 *
 * @author Kun Xia
 */
public class MainMenuStageStart {
    private Stage stage;

    // For Start Client Scene.
    private String socketServerAddress;
    private int port;
    private SocketServerThread socketServer;
    private ComboBox<String> ipComboBox;
    private TextField portTextField;
    private Button startClientButton;
    private Label socketMessageLabel;

    // For Main Menu Scene
    private Button registerFileButton;
    private Button cancelSharingButton;
    private Button searchSharingFilesButton;
    private Button stopClientButton;


    /**
     * Initializes the MainMenuStageStart with the specified stage.
     *
     * @param stage The stage for the main menu UI.
     */
    public MainMenuStageStart(Stage stage) {
        this.stage = stage;
        StageHelper.addStage(stage);
        initialize();
    }

    /**
     * Initializes the main menu UI components and sets up the initial stage properties.
     */
    private void initialize() {
        stage.setTitle("File Share Client");

        GridPane startClientGrid = new GridPane();
        configureStartClientGrid(startClientGrid);

        Scene startClientScene = new Scene(startClientGrid, 600, 450);
        stage.setScene(startClientScene);

        stage.show();
    }

    /**
     * Configures the layout and appearance of the start client GridPane.
     *
     * @param startClientGrid The GridPane for the start client UI.
     */
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

                Scene mainMenuScene = new Scene(mainMenuGrid, 600, 450);
                stage.setScene(mainMenuScene);

            }
        });

        socketMessageLabel = new Label();

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
        startClientGrid.add(socketMessageLabel, 0, 5);

    }

    /**
     * Starts the socket server and directs to the main menu scene if successful.
     *
     * @return True if the socket server starts successfully, false otherwise.
     */
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
                            return startSocketThread();
                        } else {
                            socketMessageLabel.setText("Port " + port + " is already in use.\n"
                                    + "Please enter a valid port.\n");
                        }
                    } else {
                        return startSocketThread();
                    }

                } else {
                    socketMessageLabel.setText("Please enter a valid port from 0 to 65535.\n");
                }


            } else {
                socketMessageLabel.setText("Please choose a valid IP.\n");
            }

        } catch (NumberFormatException e){
            socketMessageLabel.setText("Invalid input. Please enter a valid port.\n");
        } catch (Exception e) {
            socketMessageLabel.setText("System error.\n");
        }

        return false;
    }

    /**
     * Starts the socket server thread and creates a shared file folder for the specified IP and port.
     *
     * @return True if the socket server thread starts successfully, false otherwise.
     */
    private boolean startSocketThread() {
        // Create shared file folder for specific IP and port.
        LocalFileHelper.createSharedFileDirectory(socketServerAddress, port);

        // Create server socket
        socketServer = new SocketServerThread(port);
        socketServer.start();
        return true;
    }

    /**
     * Configures the layout and appearance of the main menu GridPane.
     *
     * @param mainMenuGrid The GridPane for the main menu UI.
     */
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
        searchSharingFilesButton.setOnAction(e -> {
            Stage searchSharingFileStage = new Stage();
            new SearchSharingStageStart(searchSharingFileStage);
        });
        stopClientButton.setOnAction(e -> {
            handleStopClient();
        });

        // Add components to main menu GridPane
        mainMenuGrid.add(registerFileButton, 0, 0);
        mainMenuGrid.add(cancelSharingButton, 0, 1);
        mainMenuGrid.add(searchSharingFilesButton, 0, 2);
        mainMenuGrid.add(stopClientButton, 0, 3);
    }

    /**
     * Handles the event when the user clicks the "Stop Client" button.
     * Closes all stages and stops the socket server.
     */
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
