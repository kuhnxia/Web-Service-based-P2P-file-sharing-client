package kun.helpers;

import javafx.stage.Stage;
import java.util.HashSet;
import java.util.Set;

/**
 * Helper class for managing JavaFX Stages.
 *
 * This class provides methods to add, remove, and retrieve JavaFX Stages.
 * It ensures that the set of Stages is maintained without duplicates.
 * It is useful for tracking and managing multiple stages in a JavaFX application.
 *
 * @author Kun Xia
 */
public class StageHelper {
    private static final Set<Stage> stages = new HashSet<>();

    /**
     * Adds a JavaFX Stage to the set of managed stages.
     *
     * @param stage The Stage to be added.
     */
    public static void addStage(Stage stage) {
        stages.add(stage);
    }

    /**
     * Removes a JavaFX Stage from the set of managed stages.
     *
     * @param stage The Stage to be removed.
     */
    public static void removeStage(Stage stage) {
        stages.remove(stage);
    }

    /**
     * Retrieves a copy of the current set of managed JavaFX Stages.
     *
     * @return A Set containing the currently managed Stages.
     */
    public static Set<Stage> getStages() {
        return new HashSet<>(stages);
    }
}