package com.surveyapp.model;

public class Question {
    private int id;
    private int surveyId;
    private String text;
    private String type;

    public Question(int id, int surveyId, String text, String type) {
        this.id = id;
        this.surveyId = surveyId;
        this.text = text;
        this.type = type;
    }

    public int getId() { return id; }
    public int getSurveyId() { return surveyId; }
    public String getText() { return text; }
    public String getType() { return type; }
}
