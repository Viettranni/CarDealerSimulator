module org.example.guitest {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.guitest to javafx.fxml;
    exports org.example.guitest;
}