package com.example.gpsspoofdetector;

import android.location.Location;

import java.util.ArrayList;

/* This class analyses the arraylist of locations to determine if there is any spoofing */
public class LocationAnalyser {
    private ArrayList<Location> locations;
    private LocationAnalyserResult results;

    public LocationAnalyser(ArrayList<Location> locations) {
        this.locations = locations;
        this.results = new LocationAnalyserResult();
    }

    public LocationAnalyser analyseLocations() {
        LocationCheckResult result = checkDistanceTravelled();
        results.addCheckResults(Check.DISTANCE_TRAVELLED, result);
        return this;
    }

    public String getResults() {
        return results.toString();
    }

    // This method checks the distance travelled of the locations gathered.
    // If the distance travelled is too large (>100m), it is a sign
    // of location spoofing.
    private LocationCheckResult checkDistanceTravelled() {
        Location prevLocation = locations.get(0);
        boolean isDistanceRealistic = true;
        float greatestDifferenceInDistance = -1;
        for (Location location : locations) {
            float differenceInDistance = location.distanceTo(prevLocation);
            if (differenceInDistance > 100) {
                isDistanceRealistic = false;
                greatestDifferenceInDistance = Math.max(differenceInDistance, greatestDifferenceInDistance);
            }
            prevLocation = location;
        }

        String explanation;
        if (isDistanceRealistic) {
            explanation = "Distances between consecutive collected locations is within tolerable range.";
        } else {
            explanation = String.format("A difference in distance of %.2fm was detected which is outside the tolerable range.", greatestDifferenceInDistance);
        }
        return new LocationCheckResult(isDistanceRealistic, explanation);
    }
}
