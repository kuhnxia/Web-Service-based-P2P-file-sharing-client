module kun {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jakarta.ws.rs;
    requires jakarta.activation;
    opens kun to javafx.fxml;
    exports kun;
}