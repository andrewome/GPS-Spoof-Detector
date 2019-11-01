package com.example.gpsspoofdetector;

import java.util.HashMap;

enum Check {
    DISTANCE_TRAVELLED
}

/* This class stores the results for all checks done */
class LocationAnalyserResult {
    private HashMap<Check, LocationCheckResult> results = new HashMap<>();
    private HashMap<Check, String> checkNames = new HashMap<>();
    private String DISTANCE_CHECK = "Distance Check: ";
    private String PASSED = "Passed";
    private String FAILED = "Failed";

    public LocationAnalyserResult() {
        checkNames.put(Check.DISTANCE_TRAVELLED, DISTANCE_CHECK);
    }

    public void addCheckResults(Check check, LocationCheckResult locationCheckResult) {
        results.put(check, locationCheckResult);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Check check : Check.values()) {
            if (results.containsKey(check)) {
                String checkStr = checkNames.get(check);
                sb.append(checkStr + (results.get(check).isPassed() ? PASSED : FAILED) +
                    ". " + results.get(check).getExplanation() + '\n');
            }
        }

        return sb.toString();
    }
}
