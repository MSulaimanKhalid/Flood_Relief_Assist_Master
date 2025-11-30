module org.example.zereaab {
    requires javafx.fxml;
    requires com.gluonhq.maps;
    requires javafx.controls;
    requires javafx.graphics;
    requires mapsforge.map;


    opens org.example.zereaab to javafx.fxml;
    exports org.example.zereaab;
}