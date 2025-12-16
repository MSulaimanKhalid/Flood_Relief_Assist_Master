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
    requires svgSalamander;


    opens org.example.zereaab to javafx.fxml;
    exports org.example.zereaab;
    exports org.example.zereaab.predictionGraph;
    opens org.example.zereaab.predictionGraph to javafx.fxml;
    exports org.example.zereaab.AssigningDataFromCSV;
    opens org.example.zereaab.AssigningDataFromCSV to javafx.fxml;
    exports org.example.zereaab.DataStructures;
    opens org.example.zereaab.DataStructures to javafx.fxml;
}