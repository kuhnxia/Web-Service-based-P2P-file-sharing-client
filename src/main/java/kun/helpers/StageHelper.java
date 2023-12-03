package kun.helpers;

import javafx.stage.Stage;
import java.util.HashSet;
import java.util.Set;

public class StageHelper {
    private static final Set<Stage> stages = new HashSet<>();

    public static void addStage(Stage stage) {
        stages.add(stage);
    }

    public static void removeStage(Stage stage) {
        stages.remove(stage);
    }

    public static Set<Stage> getStages() {
        return new HashSet<>(stages);
    }
}