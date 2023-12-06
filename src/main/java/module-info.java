module kun {
    requires javafx.controls;
    requires javafx.fxml;

    opens kun to javafx.fxml;
    exports kun;
}