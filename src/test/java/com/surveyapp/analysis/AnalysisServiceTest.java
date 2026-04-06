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
}
