package com.surveyapp.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;
import com.surveyapp.db.DatabaseManager;

public class UserPortalView {

    private final Runnable goBackAction;
    private final DatabaseManager dbManager;

    public UserPortalView(Runnable goBackAction, DatabaseManager dbManager) {
        this.goBackAction = goBackAction;
        this.dbManager = dbManager;
    }

    public VBox getView() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));

        Label title = new Label("User Portal - Take a Survey");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button backBtn = new Button("Back to Main Menu");
        backBtn.setOnAction(e -> {
            if (goBackAction != null) goBackAction.run();
        });

        VBox surveyListContainer = new VBox(10);
        surveyListContainer.setAlignment(Pos.CENTER);
        
        List<String> surveys = dbManager.getAllSurveyTitles();
        if (surveys.isEmpty()) {
            surveyListContainer.getChildren().add(new Label("(No Surveys Available)"));
        } else {
            for (String titleText : surveys) {
                Button surveyBtn = new Button(titleText);
                surveyBtn.setStyle("-fx-min-width: 250px; -fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
                surveyListContainer.getChildren().add(surveyBtn);
            }
        }

        layout.getChildren().addAll(backBtn, title, surveyListContainer);

        return layout;
    }
}
