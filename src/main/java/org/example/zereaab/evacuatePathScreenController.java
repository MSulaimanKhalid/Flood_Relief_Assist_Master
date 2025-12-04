package org.example.zereaab;

import com.gluonhq.maps.MapView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class evacuatePathScreenController implements Initializable {
    @FXML private BorderPane rootNode;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MapView map = new MapView();
        map.setZoom(3);
        rootNode.setCenter(map);
    }
}
