package org.example.zereaab;

import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.mapsforge.core.graphics.Bitmap;
import org.mapsforge.core.graphics.GraphicFactory;
import org.mapsforge.core.graphics.ResourceBitmap;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Dimension;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Rotation;
import org.mapsforge.map.awt.graphics.AwtBitmap;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.LayerManager;
import org.mapsforge.map.layer.cache.InMemoryTileCache;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.overlay.Marker;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class epsController implements Initializable {

    public Label zoomCheckBoxHeading;
    public VBox zoomLevelVBox;
    public VBox evacuationAlgoControlsVBox;
    public ToggleButton enablePlacingMarkersButton;
    public VBox placedMarkersListVBox;
    public Label markersListHeading;
    public VBox placedMarkersListVBoxInsideScrollPane;
    public Button convexHullMakingButton;
    @FXML private SwingNode mapNodeSwing;
    @FXML private BorderPane root;
    @FXML private Label errorLabel;
    MapView mapView;
    SingleEndedQueue<LatLong> markersPLacedQueue = new SingleEndedQueue<>();
    MouseListener getCoordinatesMouseAdapter;
    Label entryInMarkerList = new Label("");
    ResourceBitmap markerBitMap;
    Bitmap circleBitmap;
    Bitmap convexPointBitmap;
    double latRef = 0;
    double longRef = 0;
    final static double radiusOfEarth = 6371000;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GraphicFactory graphicFactory = AwtGraphicFactory.INSTANCE;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                mapView = new org.mapsforge.map.awt.view.MapView();
                mapView.getMapScaleBar().setVisible(true);

                BorderLayout borderLayout = new BorderLayout();
                JPanel panel = new JPanel(borderLayout);

                panel.add((Component) mapView, BorderLayout.CENTER);

                File file = new File("src/main/resources/pakistan.map");
                if (!file.exists()) {
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

                TileRendererLayer renderer = new TileRendererLayer(tileCache, mapFile, mapViewPosition, false, true, true, graphicFactory);

                renderer.setXmlRenderTheme(MapsforgeThemes.BIKER);
                mapView.getLayerManager().getLayers().add(renderer);

                mapView.setCenter(new LatLong(31.5204, 74.3587));
                mapView.setZoomLevel((byte) 9);
                mapView.repaint();

                mapNodeSwing.setContent(panel);
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        errorLabel.setText("map loaded successfully");
                    }
                });

                /*
                //Bitmap markerBitMap;
                try (InputStream markerFileInputStream = getClass().getResourceAsStream("/map_marker.jpg")){
                    Platform.runLater(() -> {
                        errorLabel.setText(errorLabel.getText() + "\nInputStream object initialised successfully for marker file");
                    });

                    try {
                        markerBitMap = graphicFactory.createResourceBitmap(markerFileInputStream, 1.0f,2,2,2,2);
                        Platform.runLater(() -> {
                            errorLabel.setText(errorLabel.getText() + "\nBitMap for marker image made successfully");
                        });
                    } catch (IOException e) {
                        Platform.runLater(() -> {
                            errorLabel.setText(errorLabel.getText() + "\nBitMap for marker image not initialised");
                        });
                        throw new RuntimeException(e);
                    }



                } catch (IOException e) {
                    Platform.runLater(() -> {
                        errorLabel.setText(errorLabel.getText() + "\nInputStream object not initialised for marker file");
                    });
                    throw new RuntimeException(e);
                }*/

            }
        });
        zoomCheckBoxHeading.setText("Set map zoom level");

        RadioButton zoom6 = new RadioButton("6");
        RadioButton zoom8 = new RadioButton("8");
        RadioButton zoom10 = new RadioButton("10");
        RadioButton zoom12 = new RadioButton("12");
        RadioButton zoom14 = new RadioButton("14");
        RadioButton zoom16 = new RadioButton("16");

        ToggleGroup zoomLevelGroup = new ToggleGroup();
        zoom6.setToggleGroup(zoomLevelGroup);
        zoom8.setToggleGroup(zoomLevelGroup);
        zoom10.setToggleGroup(zoomLevelGroup);
        zoom12.setToggleGroup(zoomLevelGroup);
        zoom14.setToggleGroup(zoomLevelGroup);
        zoom16.setToggleGroup(zoomLevelGroup);

        zoomLevelGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                RadioButton rb = (RadioButton) zoomLevelGroup.getSelectedToggle();

                if (rb == zoom6) {
                    mapView.setZoomLevel((byte) 6);
                } else if (rb == zoom8) {
                    mapView.setZoomLevel((byte) 8);
                } else if (rb == zoom10) {
                    mapView.setZoomLevel((byte) 10);
                } else if (rb == zoom12) {
                    mapView.setZoomLevel((byte) 12);
                } else if (rb == zoom14) {
                    mapView.setZoomLevel((byte) 14);
                } else if (rb == zoom16) {
                    mapView.setZoomLevel((byte) 16);
                }
            }
        });
        zoomLevelVBox.getChildren().add(zoom6);
        zoomLevelVBox.getChildren().add(zoom8);
        zoomLevelVBox.getChildren().add(zoom10);
        zoomLevelVBox.getChildren().add(zoom12);
        zoomLevelVBox.getChildren().add(zoom14);
        zoomLevelVBox.getChildren().add(zoom16);

        placedMarkersListVBoxInsideScrollPane.getChildren().add(entryInMarkerList);

        enablePlacingMarkersButton.selectedProperty().addListener((obs,oldState,newState) -> {
            if(enablePlacingMarkersButton.isSelected()){
                ((Component)mapView).addMouseListener(getCoordinatesMouseAdapter);
            }
            else{
                ((Component)mapView).removeMouseListener(getCoordinatesMouseAdapter);
            }
        });

        /*File markerFile = new File("src/main/resources/map_marker.jpg");
        if (!markerFile.exists()){
            Platform.runLater(() -> {
                errorLabel.setText(errorLabel.getText() + "\nMarker image file not found");
            });
        }
        else{
            Platform.runLater(() -> {
                errorLabel.setText(errorLabel.getText() + "\nMarker image file found successfully");
            });
        }*/

        /*Bitmap markerBitMap;
        try (InputStream markerFileInputStream = getClass().getResourceAsStream("/map_marker.jpg")){
            Platform.runLater(() -> {
                errorLabel.setText(errorLabel.getText() + "\nInputStream object initialised successfully for marker file");
            });

            try {
                markerBitMap = graphicFactory.createResourceBitmap(markerFileInputStream, 1.0f, 0,0,0,0);
                Platform.runLater(() -> {
                    errorLabel.setText(errorLabel.getText() + "\nBitMap for marker image made successfully");
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    errorLabel.setText(errorLabel.getText() + "\nBitMap for marker image not initialised");
                });
                throw new RuntimeException(e);
            }

        } catch (IOException e) {
            Platform.runLater(() -> {
                errorLabel.setText(errorLabel.getText() + "\nInputStream object not initialised for marker file");
            });
            throw new RuntimeException(e);
        }*/

        BufferedImage circleImage = new BufferedImage(10,10,BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D circleGraphics = circleImage.createGraphics();
        circleGraphics.setColor(Color.RED);
        circleGraphics.fillOval(0,0,10,10);
        circleGraphics.dispose();

        circleBitmap = new AwtBitmap(circleImage);

        getCoordinatesMouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                MapViewProjection mapViewProjection = new MapViewProjection(mapView);
                LatLong latLong = mapViewProjection.fromPixels(e.getX(), e.getY());

                markersPLacedQueue.enqueue(latLong);

                latRef = latRef + latLong.getLatitude();
                longRef = longRef + latLong.getLongitude();

                if(markersPLacedQueue.getSize()!=1){
                    latRef = latRef/2;
                    longRef = longRef/2;
                }

                Platform.runLater(() -> {
                    entryInMarkerList.setText(entryInMarkerList.getText() + "-> Lat: " + latLong.getLatitude() + " Long: " + latLong.getLongitude() + "\n");
                });

                Marker circle = new Marker(latLong, circleBitmap, -(circleBitmap.getWidth() / 2), -(circleBitmap.getHeight() / 2));
                mapView.getLayerManager().getLayers().add(circle);

            }
        };

        convexHullMakingButton.setOnAction((obs) -> {
            if (markersPLacedQueue.getSize() < 3){
                Platform.runLater(() -> {
                    errorLabel.setText(errorLabel.getText() + "\nAdd atleast 3 markers to map for making convex hull");
                });
                return;
            }

            SingleEndedQueue<Point2D.Double> projectedMarkersQueue = ConvexHull.generateHullList(markersPLacedQueue, latRef,longRef);

            ArrayList<Point2D.Double> hullList = ConvexHull.generate(projectedMarkersQueue);
            if (hullList == null){
                Platform.runLater(() -> {
                    errorLabel.setText(errorLabel.getText() + "\nConvex hull is invalid, it cotains less than 3 points");
                });
                return;
            }

            BufferedImage convexPoint = new BufferedImage(10,10,BufferedImage.TYPE_3BYTE_BGR);
            Graphics2D convexpointGraphics = convexPoint.createGraphics();
            convexpointGraphics.setColor(Color.GREEN);
            convexpointGraphics.fillOval(0,0,10,10);
            convexpointGraphics.dispose();

            convexPointBitmap = new AwtBitmap(convexPoint);
            Marker greenCircle;
            LatLong polygonVertex;
            Point2D.Double p;
            for (int i=0; i<hullList.size(); i++){
                p = hullList.get(i);
                polygonVertex = new LatLong(latRef + Math.toDegrees(p.getY() / radiusOfEarth)  ,  longRef + Math.toDegrees(p.getX() / (radiusOfEarth * Math.cos(Math.toRadians(latRef)))));
                System.out.println(polygonVertex.getLatitude()+"    "+polygonVertex.getLongitude());
                greenCircle = new Marker(polygonVertex, convexPointBitmap, -(convexPointBitmap.getWidth() / 2), -(convexPointBitmap.getHeight() / 2));
                mapView.getLayerManager().getLayers().add(greenCircle);
            }

        });

    }
}
