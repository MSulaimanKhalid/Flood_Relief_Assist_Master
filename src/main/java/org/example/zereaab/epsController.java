package org.example.zereaab;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Rotation;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.layer.cache.InMemoryTileCache;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.model.MapViewPosition;
import org.mapsforge.map.model.Model;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.scalebar.MapScaleBar;
import org.mapsforge.map.util.MapViewProjection;
import org.mapsforge.map.view.FpsCounter;
import org.mapsforge.map.view.FrameBuffer;
import org.mapsforge.map.view.MapView;
import org.mapsforge.map.awt.graphics.AwtGraphicFactory;
import org.mapsforge.map.rendertheme.internal.MapsforgeThemes;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Label;

public class epsController implements Initializable {

    @FXML private SwingNode mapNodeSwing;
    @FXML private BorderPane root;
    @FXML private Label errorLabel;
    MapView mapView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                GraphicFactory graphicFactory = AwtGraphicFactory.INSTANCE;

                mapView = new org.mapsforge.map.awt.view.MapView();
                mapView.getMapScaleBar().setVisible(true);

                BorderLayout borderLayout = new BorderLayout();
                JPanel panel = new JPanel(borderLayout);

                panel.add((Component) mapView, BorderLayout.CENTER);

                File  file = new File("src/main/resources/pakistan.map");
                if (!file.exists()){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            errorLabel.setText("pakistan.map file not found!");
                        }
                    });
                    return;
                }

                MapFile mapFile = new MapFile(file);

                TileCache tileCache = new InMemoryTileCache(1024);

                MapViewPosition mapViewPosition = mapView.getModel().mapViewPosition;

                TileRendererLayer renderer = new TileRendererLayer(tileCache,mapFile,mapViewPosition, false,true,true,graphicFactory);

                renderer.setXmlRenderTheme(MapsforgeThemes.MOTORIDER);
                mapView.getLayerManager().getLayers().add(renderer);

                mapView.setCenter(new LatLong(31.5204,74.3587));
                mapView.setZoomLevel((byte) 9);
                mapView.repaint();

                mapNodeSwing.setContent(panel);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        errorLabel.setText("map loaded successfully");
                    }
                });

            }
        });
    }
}
