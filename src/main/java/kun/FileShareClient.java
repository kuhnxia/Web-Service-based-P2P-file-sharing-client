package kun;

import javafx.application.Application;
import javafx.stage.Stage;

import kun.stages.MainMenuStageStart;

public class FileShareClient extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new MainMenuStageStart(primaryStage);
    }
}
