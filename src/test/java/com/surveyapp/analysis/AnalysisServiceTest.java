package com.surveyapp.analysis;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AnalysisServiceTest {
    
    @Test
    public void testAnalyzeSentiment() {
        AnalysisService service = new AnalysisService();
        assertEquals("Positive", service.analyzeSentiment("This app is good, fast, and great!"));
        assertEquals("Negative", service.analyzeSentiment("Terrible experience, very bad and slow."));
        assertEquals("Neutral", service.analyzeSentiment("I used the app today."));
    }

    @Test
    public void testCalculateAverageRating() {
        AnalysisService service = new AnalysisService();
        java.util.List<String> ratings = java.util.Arrays.asList("4", "5", "3");
        assertEquals(4.0, service.calculateAverageRating(ratings), 0.01);
        
        java.util.List<String> empty = java.util.Collections.emptyList();
        assertEquals(0.0, service.calculateAverageRating(empty), 0.01);
    }
}
