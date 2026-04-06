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
    private Scene mainScene;
    private VBox mainMenuLayout;

    @Override
    public void start(Stage primaryStage) {
        dbManager = new DatabaseManager("test_survey.db");

        mainMenuLayout = createMainMenu();
        mainScene = new Scene(mainMenuLayout, 600, 400);
        
        primaryStage.setTitle("Survey System V1");
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    private VBox createMainMenu() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        
        Label title = new Label("Smart Survey & Feedback Analysis System");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button adminBtn = new Button("Enter as Admin");
        Button userBtn = new Button("Enter as User (Take Survey)");

        adminBtn.setOnAction(e -> showAdminView());
        userBtn.setOnAction(e -> showUserView());

        layout.getChildren().addAll(title, adminBtn, userBtn);
        return layout;
    }

    private void showAdminView() {
        AdminDashboardView adminView = new AdminDashboardView(this::showMainMenuView, dbManager);
        mainScene.setRoot(adminView.getView());
    }

    private void showUserView() {
        UserPortalView userView = new UserPortalView(this::showMainMenuView, dbManager);
        mainScene.setRoot(userView.getView());
    }

    private void showMainMenuView() {
        mainScene.setRoot(mainMenuLayout);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
