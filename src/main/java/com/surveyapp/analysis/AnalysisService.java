package com.surveyapp.analysis;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class AnalysisService {
    private final Set<String> positiveWords = new HashSet<>(Arrays.asList("good", "great", "excellent", "fast", "amazing", "love"));
    private final Set<String> negativeWords = new HashSet<>(Arrays.asList("bad", "poor", "slow", "terrible", "crash", "hate"));

    public String analyzeSentiment(String input) {
        if (input == null || input.trim().isEmpty()) return "Neutral";
        
        String[] words = input.toLowerCase().replaceAll("[^a-zA-Z\\s]", "").split("\\s+");
        int posCount = 0;
        int negCount = 0;

        for (String word : words) {
            if (positiveWords.contains(word)) posCount++;
            if (negativeWords.contains(word)) negCount++;
        }

        if (posCount > negCount) return "Positive";
        if (negCount > posCount) return "Negative";
        return "Neutral";
    }
}
