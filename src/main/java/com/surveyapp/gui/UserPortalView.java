package com.surveyapp.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.surveyapp.db.DatabaseManager;
import com.surveyapp.model.Question;

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

        VBox contentContainer = new VBox(10);
        contentContainer.setAlignment(Pos.CENTER);
        
        loadSurveyList(contentContainer);

        layout.getChildren().addAll(backBtn, title, contentContainer);

        return layout;
    }

    private void loadSurveyList(VBox container) {
        container.getChildren().clear();
        List<String> surveys = dbManager.getAllSurveyTitles();
        if (surveys.isEmpty()) {
            container.getChildren().add(new Label("(No Surveys Available)"));
        } else {
            for (String titleText : surveys) {
                Button surveyBtn = new Button(titleText);
                surveyBtn.setStyle("-fx-min-width: 250px; -fx-background-color: #2ecc71; -fx-text-fill: white; -fx-font-weight: bold;");
                surveyBtn.setOnAction(e -> loadSurveyQuestions(container, titleText));
                container.getChildren().add(surveyBtn);
            }
        }
    }

    private void loadSurveyQuestions(VBox container, String titleText) {
        container.getChildren().clear();
        int surveyId = dbManager.getSurveyIdByTitle(titleText);
        List<Question> questions = dbManager.getQuestionsForSurvey(surveyId);

        Label header = new Label("Taking Survey: " + titleText);
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        container.getChildren().add(header);

        Map<Question, TextField> inputs = new HashMap<>();

        for (Question q : questions) {
            Label qLabel = new Label(q.getText());
            TextField qInput = new TextField();
            if (q.getType().equals("RATING")) {
                qInput.setPromptText("Enter a rating (e.g. 1 to 5)");
            } else {
                qInput.setPromptText("Enter your text response");
            }
            inputs.put(q, qInput);
            container.getChildren().addAll(qLabel, qInput);
        }

        Button submitBtn = new Button("Submit Survey");
        submitBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        submitBtn.setOnAction(e -> {
            for (Map.Entry<Question, TextField> entry : inputs.entrySet()) {
                dbManager.saveResponse(surveyId, entry.getKey().getId(), entry.getValue().getText());
            }
            container.getChildren().clear();
            Label thanks = new Label("Thank you for your submission!");
            thanks.setStyle("-fx-font-size: 16px; -fx-text-fill: green;");
            Button backToSurveys = new Button("Take Another");
            backToSurveys.setOnAction(evt -> loadSurveyList(container));
            container.getChildren().addAll(thanks, backToSurveys);
        });

        container.getChildren().add(submitBtn);
    }
}
