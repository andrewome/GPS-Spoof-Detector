package com.example.gpsspoofdetector;

import java.util.HashMap;

enum Check {
    ALTITUDE,
    ALTITUDE_DECIMAL,
    LATITUDE,
    LONGITUDE,
    DISTANCE_TRAVELLED
}

/* This class stores the results for all checks done */
class LocationAnalyserResult {
    private HashMap<Check, Boolean> results = new HashMap<>();
    private HashMap<Check, String> checkNames = new HashMap<>();
    private String ALTITUDE_CHECK = "Altitude Check: ";
    private String ALTITUDE_DECIMAL_CHECK = "Altitude Decimal Check: ";
    private String LATITUDE_CHECK = "Latitude Check: ";
    private String LONGITUDE_CHECK = "Longitude Check: ";
    private String DISTANCE_CHECK = "Distance Check: ";
    private String PASSED = "Passed";
    private String FAILED = "Failed";

    public LocationAnalyserResult() {
        checkNames.put(Check.ALTITUDE, ALTITUDE_CHECK);
        checkNames.put(Check.ALTITUDE_DECIMAL, ALTITUDE_DECIMAL_CHECK);
        checkNames.put(Check.LONGITUDE, LONGITUDE_CHECK);
        checkNames.put(Check.LATITUDE, LATITUDE_CHECK);
        checkNames.put(Check.DISTANCE_TRAVELLED, DISTANCE_CHECK);
    }

    public void addCheckResults(Check check, Boolean passed) {
        results.put(check, passed);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Check check : Check.values()) {
            if (results.containsKey(check)) {
                String checkStr = checkNames.get(check);
                sb.append(checkStr + (results.get(check) ? PASSED : FAILED) + '\n');
            }
        }

        return sb.toString();
    }
}
