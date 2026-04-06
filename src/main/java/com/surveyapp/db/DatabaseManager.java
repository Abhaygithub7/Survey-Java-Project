package com.surveyapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private final String url;

    public DatabaseManager(String dbPath) {
        this.url = "jdbc:sqlite:" + dbPath;
        initTables();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    private void initTables() {
        String surveysTable = "CREATE TABLE IF NOT EXISTS surveys (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, created_at DATETIME DEFAULT CURRENT_TIMESTAMP);";
        String questionsTable = "CREATE TABLE IF NOT EXISTS questions (id INTEGER PRIMARY KEY AUTOINCREMENT, survey_id INTEGER, question_text TEXT, question_type TEXT, options TEXT, FOREIGN KEY(survey_id) REFERENCES surveys(id));";
        String responsesTable = "CREATE TABLE IF NOT EXISTS responses (id INTEGER PRIMARY KEY AUTOINCREMENT, survey_id INTEGER, question_id INTEGER, response_value TEXT, submitted_at DATETIME DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(survey_id) REFERENCES surveys(id), FOREIGN KEY(question_id) REFERENCES questions(id));";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(surveysTable);
            stmt.execute(questionsTable);
            stmt.execute(responsesTable);
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }
}
