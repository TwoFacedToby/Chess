package com.example.chess;

import com.example.chess.logic.LogicController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ChessView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1600, 900);
        LogicController.get().setController(fxmlLoader.getController());
        stage.setTitle("Chess");
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);
    }

    public static void main(String[] args) {
        launch();
    }
}