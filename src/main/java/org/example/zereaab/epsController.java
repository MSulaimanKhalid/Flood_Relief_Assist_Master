package org.example.zereaab;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.mapsforge.map.view.MapView;

import java.net.URL;
import java.util.ResourceBundle;

public class epsController implements Initializable {
    @FXML private BorderPane root;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        MapsforgeMap map = new MapsforgeMap();
    }
}
