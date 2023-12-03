package kun;

import javafx.application.Application;
import javafx.stage.Stage;

import kun.stage_starts.MainMenuStageStart;

public class FileShareClientFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new MainMenuStageStart(primaryStage);
    }
}
