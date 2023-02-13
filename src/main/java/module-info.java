module com.example.biblioteca {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;

    opens com.example.biblioteca to javafx.fxml;
    exports com.example.biblioteca;

}