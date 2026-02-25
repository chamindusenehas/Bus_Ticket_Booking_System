package com.busticket.util;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class SceneManager {

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static FXMLLoader switchScene(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(
                SceneManager.class.getResource("/com/busticket/fxml/" + fxmlPath)
            );
            Parent root = loader.load();

            // Fade-in transition
            FadeTransition fade = new FadeTransition(Duration.millis(300), root);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(
                SceneManager.class.getResource("/com/busticket/css/style.css").toExternalForm()
            );

            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();
            fade.play();

            return loader; // caller can get controller via loader.getController()
        } catch (IOException e) {
            throw new RuntimeException("Failed to load FXML: " + fxmlPath, e);
        }
    }


    public static void navigateTo(String fxmlPath) {
        switchScene(fxmlPath);
    }
}
