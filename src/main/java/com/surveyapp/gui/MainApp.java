package com.surveyapp.gui;

import com.surveyapp.db.DatabaseManager;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {
    private DatabaseManager dbManager;

    @Override
    public void start(Stage primaryStage) {
        dbManager = new DatabaseManager("test_survey.db");

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        
        Label title = new Label("Smart Survey & Feedback Analysis System");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button adminBtn = new Button("Enter as Admin");
        Button userBtn = new Button("Enter as User (Take Survey)");

        adminBtn.setOnAction(e -> System.out.println("Admin clicked (WIP)"));
        userBtn.setOnAction(e -> System.out.println("User clicked (WIP)"));

        layout.getChildren().addAll(title, adminBtn, userBtn);
        
        Scene scene = new Scene(layout, 600, 400);
        primaryStage.setTitle("Survey System V1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
