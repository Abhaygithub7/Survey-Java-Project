package com.surveyapp.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;
import com.surveyapp.db.DatabaseManager;
import com.surveyapp.analysis.AnalysisService;
import com.surveyapp.model.Question;

public class AdminDashboardView {

    private final Runnable goBackAction;
    private final DatabaseManager dbManager;

    public AdminDashboardView(Runnable goBackAction, DatabaseManager dbManager) {
        this.goBackAction = goBackAction;
        this.dbManager = dbManager;
    }

    public VBox getView() {
        VBox layout = new VBox(20);
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setPadding(new Insets(20));

        Label title = new Label("Admin Dashboard - Survey Management");
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
                Button surveyBtn = new Button("View Analytics: " + titleText);
                surveyBtn.setStyle("-fx-min-width: 250px; -fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
                surveyBtn.setOnAction(e -> loadSurveyAnalytics(container, titleText));
                container.getChildren().add(surveyBtn);
            }
        }
    }

    private void loadSurveyAnalytics(VBox container, String titleText) {
        container.getChildren().clear();
        int surveyId = dbManager.getSurveyIdByTitle(titleText);
        List<Question> questions = dbManager.getQuestionsForSurvey(surveyId);
        AnalysisService analyzer = new AnalysisService();

        Label header = new Label("Analytics for: " + titleText);
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        container.getChildren().add(header);

        for (Question q : questions) {
            Label qLabel = new Label("Q: " + q.getText());
            qLabel.setStyle("-fx-font-weight: bold;");
            
            List<String> responses = dbManager.getResponsesForQuestion(q.getId());
            Label analyticsLabel = new Label();
            
            if (responses.isEmpty()) {
                analyticsLabel.setText("No responses yet.");
            } else if (q.getType().equals("RATING")) {
                double avg = analyzer.calculateAverageRating(responses);
                analyticsLabel.setText("Average Rating: " + String.format("%.1f", avg) + " / 5.0 (" + responses.size() + " responses)");
            } else {
                String joinedResponses = String.join(" ", responses);
                String sentiment = analyzer.analyzeSentiment(joinedResponses);
                analyticsLabel.setText("Sentiment Analysis: " + sentiment + " (" + responses.size() + " responses)");
            }
            analyticsLabel.setStyle("-fx-text-fill: #555; -fx-padding: 0 0 10 0;");
            
            container.getChildren().addAll(qLabel, analyticsLabel);
        }

        Button backToSurveys = new Button("View Other Surveys");
        backToSurveys.setOnAction(evt -> loadSurveyList(container));
        container.getChildren().add(backToSurveys);
    }
}
