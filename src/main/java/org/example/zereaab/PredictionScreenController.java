package org.example.zereaab;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import org.example.zereaab.AssigningDataFromCSV.AssignData;
import org.example.zereaab.AssigningDataFromCSV.WaterNode;
import org.example.zereaab.predictionGraph.PredictionSimulator;
import org.example.zereaab.predictionGraph.RiverGraph;

public class PredictionScreenController {

    private AssignData assignData;

    @FXML
    private Button evacuationButton;

    @FXML
    private Pane rootPane;

    @FXML
    private ImageView mapView;

    @FXML
    private ScrollPane scrollPane;

    // Add circle references
    @FXML
    private Circle tarbelaCircle;
    @FXML
    private Circle kabulCircle;
    @FXML
    private Circle kalabaghCircle;
    @FXML
    private Circle chashmaCircle;
    @FXML
    private Circle taunsaCircle;
    @FXML
    private Circle maralaCircle;
    @FXML
    private Circle trimmuCircle;
    @FXML
    private Circle panjnadCircle;
    @FXML
    private Circle manglaCircle;
    @FXML
    private Circle gudduCircle;
    @FXML
    private Circle sukkurCircle;
    @FXML
    private Circle kotriCircle;

    @FXML
    public void initialize() {
        try {
            assignData = new AssignData();
            assignData.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        RiverGraph graph=new RiverGraph(assignData);

        graph.Trimmu.isJunction = true;
        graph.Trimmu.junctionCapacity = graph.Trimmu.MaxPeak;

        graph.Panjnad.isJunction = true;
        graph.Panjnad.junctionCapacity = graph.Panjnad.MaxPeak;

        System.out.println(graph.Kabul.recentOutflows+"yes this one");
        System.out.println(graph.Chashma.recentOutflows+"yes this one recent of chasma");
        PredictionSimulator predictionSimulator=new PredictionSimulator(graph);
        predictionSimulator.run();

        System.out.println(graph.Kabul.predictedOutflows+"yoho here for kabul predictions");
        System.out.println(graph.Chashma.predictedOutflows+"chasma predicted flow");




        // Load the map image
        Image image = new Image(
                getClass().getResource("/mapForPrediction.jpg").toExternalForm()
        );
        mapView.setImage(image);

        mapView.fitWidthProperty().bind(rootPane.widthProperty().divide(2)); // half screen coverage
        mapView.fitHeightProperty().bind(rootPane.heightProperty());
        mapView.setPreserveRatio(false);

        // Simple scaling of circle positions
        rootPane.boundsInLocalProperty().addListener((obs, oldBounds, newBounds) -> {
            if (newBounds.getWidth() > 0 && newBounds.getHeight() > 0) {
                scaleCirclePositions(newBounds.getWidth(), newBounds.getHeight());
            }
        });

//        scrollPane.setPannable(true);
//        mapView.setOnScroll(event -> {
//            double zoomFactor = 1.05;
//
//            if (event.getDeltaY() > 0) { // zoom in
//                mapView.setScaleX(mapView.getScaleX() * zoomFactor);
//                mapView.setScaleY(mapView.getScaleY() * zoomFactor);
//            } else { // zoom out
//                mapView.setScaleX(mapView.getScaleX() / zoomFactor);
//                mapView.setScaleY(mapView.getScaleY() / zoomFactor);
//            }
//
//            // REMOVED: scaleCirclesWithMap(); // Circles no longer scale with zoom
//        });

        // Bind button position to bottom-right
        evacuationButton.layoutXProperty().bind(
                rootPane.widthProperty()
                        .subtract(evacuationButton.widthProperty())
                        .subtract(10)
        );

        evacuationButton.layoutYProperty().bind(
                rootPane.heightProperty()
                        .subtract(evacuationButton.heightProperty())
                        .subtract(10)
        );

        // Initial circle setup
        setupCircles();
        linkCirclesToData();
    }

    private void scaleCirclePositions(double currentWidth, double currentHeight) {
        // Original map dimensions (from your prefWidth/prefHeight)
        double originalWidth = 2385.0;
        double originalHeight = 2224.0;

        // Map takes half width, full height
        double mapWidth = currentWidth / 2;
        double mapHeight = currentHeight;

        // Scale factors
        double scaleX = mapWidth / originalWidth;
        double scaleY = mapHeight / originalHeight;

        // Apply scaling to all circles
        Circle[] circles = {
                tarbelaCircle, kabulCircle, kalabaghCircle, chashmaCircle,
                taunsaCircle, maralaCircle, trimmuCircle, panjnadCircle,
                manglaCircle, gudduCircle, sukkurCircle, kotriCircle
        };

        // Original positions from your FXML
        double[][] originalPositions = {
                {1723.0, 606.0},   // tarbelaCircle
                {1627.0, 616.0},   // kabulCircle
                {1627.0, 765.0},   // kalabaghCircle
                {1546.0, 865.0},   // chashmaCircle
                {1470.0, 1139.0},  // taunsaCircle
                {1958.0, 830.0},   // maralaCircle
                {1647.0, 1073.0},  // trimmuCircle
                {1503.0, 1340.0},  // panjnadCircle
                {1870.0, 745.0},   // manglaCircle
                {1315.0, 1502.0},  // gudduCircle
                {1198.0, 1599.0},  // sukkurCircle
                {1076.0, 1799.0}   // kotriCircle
        };

        for (int i = 0; i < circles.length; i++) {
            if (circles[i] != null) {
                double originalX = originalPositions[i][0];
                double originalY = originalPositions[i][1];

                circles[i].setLayoutX(originalX * scaleX);
                circles[i].setLayoutY(originalY * scaleY);
            }
        }
    }

    private void setupCircles() {
        // Set opacity so map is visible through circles
        Circle[] allCircles = {
                tarbelaCircle, kabulCircle, kalabaghCircle, chashmaCircle,
                taunsaCircle, maralaCircle, trimmuCircle, panjnadCircle,
                manglaCircle, gudduCircle, sukkurCircle, kotriCircle
        };

        for (Circle circle : allCircles) {
            if (circle != null) {

                circle.setOpacity(0.7);
                circle.setStroke(javafx.scene.paint.Color.BLACK);
                circle.setStrokeWidth(1.5);
            }
        }
    }

    // REMOVED: private void scaleCirclesWithMap() method entirely
    // Circles will now maintain their size regardless of map zoom

    private void linkCirclesToData() {

        linkCircle(tarbelaCircle, assignData.TarbelaDam);
        linkCircle(kabulCircle, assignData.Kabul);
        linkCircle(kalabaghCircle, assignData.Kalabagh);
        linkCircle(chashmaCircle, assignData.Chashma);
        linkCircle(taunsaCircle, assignData.Taunsa);
        linkCircle(maralaCircle, assignData.Marala);
        linkCircle(trimmuCircle, assignData.Trimmu);
        linkCircle(panjnadCircle, assignData.Panjnad);
        linkCircle(manglaCircle, assignData.ManglaDam);
        linkCircle(gudduCircle, assignData.Guddu);
        linkCircle(sukkurCircle, assignData.Sukkur);
        linkCircle(kotriCircle, assignData.Kotri);
    }
    private void linkCircle(Circle circle, WaterNode node) {
        if (circle == null || node == null) return;

        circle.setOnMouseClicked(event -> {
            showWaterNodeInfo(node);
        });
    }
    private void showWaterNodeInfo(WaterNode node) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(node.stationName);

        String info =
                "Station: " + node.stationName + "\n" +
                        "River: " + node.partOfRiver + "\n" +
                        "Height: " + node.StationHeight + "\n" +
                        "Inflow: " + node.inflow + "\n" +
                        "Outflow: " + node.outflow + "\n" +
                        "Max Peak: " + node.MaxPeak + "\n" +
                        "Last Reading: " + node.lastReadingAt + "\n";

        alert.setContentText(info);
        alert.show();
    }


    @FXML
    public void nextScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("eps.fxml"));
            Scene evacScene = new Scene(loader.load(), 600, 400);

            Stage stage = (Stage) evacuationButton.getScene().getWindow();
            stage.setScene(evacScene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}