module org.example.zereaab {
    requires javafx.fxml;
    requires com.gluonhq.maps;
    requires javafx.controls;
    requires mapsforge.map;
    requires mapsforge.core;
    requires mapsforge.map.awt;
    requires mapsforge.themes;
    requires mapsforge.map.reader;
    requires javafx.swing;


    opens org.example.zereaab to javafx.fxml;
    exports org.example.zereaab;
}