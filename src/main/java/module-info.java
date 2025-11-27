module org.example.zereaab {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.zereaab to javafx.fxml;
    exports org.example.zereaab;
}