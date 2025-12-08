package org.example.zereaab;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Sulaiman_Test_Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Sulaiman_Test_Main.class.getResource("eps.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        stage.setTitle("Evacuation Path Recommender");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}