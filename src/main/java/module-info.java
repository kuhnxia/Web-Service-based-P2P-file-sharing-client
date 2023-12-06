module kun {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jakarta.ws.rs;
    opens kun to javafx.fxml;
    exports kun;
}