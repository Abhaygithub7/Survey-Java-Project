package com.surveyapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import com.surveyapp.model.Question;

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
            seedInitialData(conn);
        } catch (SQLException e) {
            System.err.println("Database initialization failed: " + e.getMessage());
        }
    }

    private void seedInitialData(Connection conn) {
        String countCheck = "SELECT COUNT(*) AS total FROM surveys;";
        try (Statement stmt = conn.createStatement()) {
            java.sql.ResultSet rs = stmt.executeQuery(countCheck);
            if (rs.next() && rs.getInt("total") == 0) {
                // Seed UEFA
                stmt.execute("INSERT INTO surveys (id, title, description) VALUES (1, 'UEFA Champions League Feedback', 'Tell us about your favorite teams and matches.');");
                stmt.execute("INSERT INTO questions (survey_id, question_text, question_type, options) VALUES (1, 'How excited are you for the knockout stages?', 'RATING', null);");
                stmt.execute("INSERT INTO questions (survey_id, question_text, question_type, options) VALUES (1, 'Which team do you think will win?', 'TEXT', null);");
                
                // Seed Tech Clubs
                stmt.execute("INSERT INTO surveys (id, title, description) VALUES (2, 'College Tech Club Survey', 'Help us improve the computer science and robotics clubs.');");
                stmt.execute("INSERT INTO questions (survey_id, question_text, question_type, options) VALUES (2, 'How would you rate the current workshops?', 'RATING', null);");
                stmt.execute("INSERT INTO questions (survey_id, question_text, question_type, options) VALUES (2, 'What other topics should we cover?', 'TEXT', null);");
                System.out.println("Seeded database with sample surveys.");
            }
        } catch (SQLException e) {
            System.err.println("Database seeding failed: " + e.getMessage());
        }
    }

    public java.util.List<String> getAllSurveyTitles() {
        java.util.List<String> titles = new java.util.ArrayList<>();
        String query = "SELECT title FROM surveys ORDER BY id ASC";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            java.sql.ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                titles.add(rs.getString("title"));
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch surveys: " + e.getMessage());
        }
        return titles;
    }

    public int getSurveyIdByTitle(String title) {
        String query = "SELECT id FROM surveys WHERE title = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, title);
            java.sql.ResultSet rs = pstmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            System.err.println("Failed to fetch survey ID: " + e.getMessage());
        }
        return -1;
    }

    public java.util.List<Question> getQuestionsForSurvey(int surveyId) {
        java.util.List<Question> questions = new java.util.ArrayList<>();
        String query = "SELECT id, question_text, question_type FROM questions WHERE survey_id = ? ORDER BY id ASC";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, surveyId);
            java.sql.ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                questions.add(new Question(
                    rs.getInt("id"), surveyId, 
                    rs.getString("question_text"), rs.getString("question_type")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch questions: " + e.getMessage());
        }
        return questions;
    }

    public void saveResponse(int surveyId, int questionId, String responseValue) {
        String query = "INSERT INTO responses (survey_id, question_id, response_value) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, surveyId);
            pstmt.setInt(2, questionId);
            pstmt.setString(3, responseValue);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to save response: " + e.getMessage());
        }
    }

    public java.util.List<String> getResponsesForQuestion(int questionId) {
        java.util.List<String> responses = new java.util.ArrayList<>();
        String query = "SELECT response_value FROM responses WHERE question_id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, questionId);
            java.sql.ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                responses.add(rs.getString("response_value"));
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch responses: " + e.getMessage());
        }
        return responses;
    }

    public void addQuestion(int surveyId, String questionText, String questionType) {
        String query = "INSERT INTO questions (survey_id, question_text, question_type, options) VALUES (?, ?, ?, null)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, surveyId);
            pstmt.setString(2, questionText);
            pstmt.setString(3, questionType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to add question: " + e.getMessage());
        }
    }
}
