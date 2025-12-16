package org.example.zereaab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Sulaiman_Test_Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(Sulaiman_Test_Main.class.getResource("PredictionScreen.fxml"));
        Scene scene = new Scene(loader.load(), 600, 400);
        stage.setTitle("Prediction Screen");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}