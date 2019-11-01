package com.example.gpsspoofdetector;

public class LocationCheckResult {
    private boolean passed;
    private String explanation;

    public LocationCheckResult(boolean passed, String explanation) {
        this.passed = passed;
        this.explanation = explanation;
    }

    public boolean isPassed() {
        return passed;
    }

    public String getExplanation() {
        return explanation;
    }
}
