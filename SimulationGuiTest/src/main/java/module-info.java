module org.example.simulationguitest {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.simulationguitest to javafx.fxml;
    exports org.example.simulationguitest;
}