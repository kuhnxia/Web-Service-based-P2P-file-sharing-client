module kun {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires java.desktop;
    opens kun to javafx.fxml;
    exports kun;
}