package kun;

import javafx.application.Application;
import javafx.stage.Stage;

import kun.stages.MainMenuStageStart;

/**
 * The main entry point for the FileShareClient application.
 *
 * This class extends the JavaFX Application class and initializes the main stage
 * for the FileShareClient. It launches the application by calling the launch() method.
 * The start() method is overridden to create and display the initial main menu stage.
 *
 * @author Kun Xia
 */
public class FileShareClient extends Application {

    /**
     * The main method to launch the FileShareClient application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Overrides the start method of the Application class to initialize the main stage.
     *
     * @param primaryStage The primary stage for the application.
     */
    @Override
    public void start(Stage primaryStage) {
        new MainMenuStageStart(primaryStage);
    }
}
