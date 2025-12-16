package org.example.zereaab;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
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

    private Tooltip infoTooltip = new Tooltip();
    @FXML
    private javafx.scene.layout.GridPane cardGrid;

    private AssignData assignData;
    @FXML
    private javafx.scene.layout.VBox predictionPanel;


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

    RiverGraph graph;

    @FXML
    public void initialize() {
        try {
            assignData = new AssignData();
            assignData.load();
        } catch (Exception e) {
            e.printStackTrace();
        }

        graph=new RiverGraph(assignData);

        graph.Trimmu.isJunction = true;
        graph.Trimmu.junctionCapacity = graph.Trimmu.MaxPeak;

        graph.Panjnad.isJunction = true;
        graph.Panjnad.junctionCapacity = graph.Panjnad.MaxPeak;

//        System.out.println(graph.Kabul.recentOutflows+"yes this one");
//        System.out.println(graph.Chashma.recentOutflows+"yes this one recent of chasma");
        PredictionSimulator predictionSimulator=new PredictionSimulator(graph);
        predictionSimulator.run();

//        System.out.println(graph.Kabul.predictedOutflows+"yoho here for kabul predictions");
//        System.out.println(graph.Chashma.predictedOutflows+"chasma predicted flow");



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
        //closing toolnip on mouse click
        rootPane.setOnMouseClicked(event -> {

            // If click was on a circle, do nothing
            if (event.getTarget() instanceof Circle) {
                return;
            }

            infoTooltip.hide();
            cardGrid.getChildren().clear();

        });

        //fixing the prediction cards to far right
        predictionPanel.layoutXProperty().bind(
                rootPane.widthProperty().subtract(predictionPanel.prefWidthProperty())
        );

        predictionPanel.layoutYProperty().set(0);
        predictionPanel.prefHeightProperty().bind(rootPane.heightProperty());
        predictionPanel.prefWidthProperty().bind(rootPane.widthProperty().divide(2));


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
    private Pane createDayCard(int day, WaterNode node, double predictedFlow) {
        double utilization = (predictedFlow / node.MaxPeak) * 100;

        String threat;
        javafx.scene.paint.Color color;

        if (utilization < 60) {
            threat = "NORMAL";
            color = javafx.scene.paint.Color.LIGHTGREEN;
        } else if (utilization < 80) {
            threat = "WATCH";
            color = javafx.scene.paint.Color.GOLD;
        } else if (utilization <= 100) {
            threat = "WARNING";
            color = javafx.scene.paint.Color.ORANGE;
        } else {
            threat = "FLOOD";
            color = javafx.scene.paint.Color.RED;
        }

        // Create the label
        javafx.scene.control.Label label = new javafx.scene.control.Label(
                "Station: " + node.stationName + "\n" +
                        "Day: " + day + " of 7\n\n" +
                        "Predicted Flow: " + String.format("%.2f", predictedFlow) + "\n" +
                        "Max Capacity: " + node.MaxPeak + "\n\n" +
                        "Utilization: " + String.format("%.1f", utilization) + "%\n" +
                        "Threat Level: " + threat
        );
        label.setWrapText(true);
        label.setMaxWidth(220);
        label.setStyle("-fx-font-size: 13; -fx-text-fill: black;");

        // VBox holds the label
        javafx.scene.layout.VBox content = new javafx.scene.layout.VBox(label);
        content.setPadding(new javafx.geometry.Insets(10));
        content.setSpacing(5);

        // Set background color **directly on VBox**
        content.setBackground(
                new javafx.scene.layout.Background(
                        new javafx.scene.layout.BackgroundFill(
                                color.deriveColor(1, 1, 1, 0.6), // semi-transparent for readability
                                new javafx.scene.layout.CornerRadii(10),
                                null
                        )
                )
        );

        // Outer Pane for consistent sizing if needed
        Pane card = new Pane(content);
        card.setPrefHeight(110);
        card.setPrefWidth(250);

        // Make VBox fill the Pane
        content.prefWidthProperty().bind(card.widthProperty());
        content.prefHeightProperty().bind(card.heightProperty());

        // Optional: border for clarity
        content.setStyle(content.getStyle() + "-fx-border-radius: 10; -fx-border-color: black; -fx-border-width: 1;");

        return card;
    }



    private void linkCircle(Circle circle, WaterNode node) {
        if (circle == null || node == null) return;

        circle.setOnMouseClicked(event -> {

            // ✅ CLEAR ONLY THE GRID
            cardGrid.getChildren().clear();


            int col = 0;
            int row = 0;

            for (int day = 1; day <= 7; day++) {
                double predictedFlow = node.predictedOutflows.get(day - 1);

                Pane card = createDayCard(day, node, predictedFlow);
                card.setPrefWidth(250);
                card.setPrefHeight(350);

                cardGrid.add(card, col, row);

                if (day == 7) {
                    GridPane.setColumnSpan(card, 2); // take both columns
                    GridPane.setHalignment(card, javafx.geometry.HPos.CENTER);
                }

                col++;
                if (col == 2) {
                    col = 0;
                    row++;
                }

            }

            // -------- Tooltip logic (unchanged) --------
            String info =
                    "Station: " + node.stationName + "\n" +
                            "River: " + node.partOfRiver + "\n" +
                            "Height: " + node.StationHeight + "\n" +
                            "Inflow: " + node.inflow + "\n" +
                            "Outflow: " + node.outflow + "\n" +
                            "Max Peak: " + node.MaxPeak + "\n"+
                            "Last reading at: "+node.lastReadingAt;


            infoTooltip.setText(info);
            infoTooltip.setStyle(
                    "-fx-background-color: rgba(30,30,30,0.9);" +
                            "-fx-text-fill: white;" +
                            "-fx-font-size: 12px;" +
                            "-fx-padding: 8;" +
                            "-fx-background-radius: 8;"
            );

            double x = circle.localToScreen(circle.getBoundsInLocal()).getMinX();
            double y = circle.localToScreen(circle.getBoundsInLocal()).getMinY();

            infoTooltip.show(circle, x, y - 80);
        });
    }





    @FXML
    public void nextScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("eps.fxml"));
            Scene evacScene = new Scene(loader.load(), 600, 400);

            Stage stage = (Stage) evacuationButton.getScene().getWindow();
            stage.setScene(evacScene);

            epsController controller = loader.getController();
            controller.setGraph(graph);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}