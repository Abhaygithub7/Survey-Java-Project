package com.surveyapp.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {
    private DatabaseManager dbManager;
    private final String dbPath = "test_survey.db";

    @BeforeEach
    public void setup() {
        dbManager = new DatabaseManager(dbPath);
    }

    @AfterEach
    public void teardown() {
        new File(dbPath).delete();
    }

    @Test
    public void testConnectionAndTableCreation() throws SQLException {
        try (Connection conn = dbManager.getConnection()) {
            assertNotNull(conn);
            assertFalse(conn.isClosed());
        }
    }
}
