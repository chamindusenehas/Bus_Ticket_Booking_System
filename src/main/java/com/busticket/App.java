package com.busticket;

import com.busticket.util.SceneManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager.setPrimaryStage(primaryStage);

        primaryStage.setTitle("Bus Ticket Reservation System");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.setResizable(true);

        SceneManager.navigateTo("Home.fxml");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
