package com.example.gpsspoofdetector;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* This class analyses the arraylist of locations to determine if there is any spoofing */
public class LocationAnalyser {
    private ArrayList<Location> locations;
    private LocationAnalyserResult results;

    public LocationAnalyser(ArrayList<Location> locations) {
        this.locations = locations;
        this.results = new LocationAnalyserResult();
    }

    public LocationAnalyser analyseLocations() {
        checkCoordinates();
        return this;
    }

    public String getResults() {
        return results.toString();
    }

    // This method checks the longitude, latitude and altitude.
    private void checkCoordinates() {
        boolean isDistanceRealistic = checkDistanceTravelled();
        results.addCheckResults(Check.DISTANCE_TRAVELLED, isDistanceRealistic);
    }

    // This method checks the distance travelled of the locations gathered.
    // If the distance travelled is too large (>100m), it is a sign
    // of location spoofing.
    private boolean checkDistanceTravelled() {
        Location prevLocation = locations.get(0);
        boolean isDistanceRealistic = true;
        for (Location location : locations) {
            if (location.distanceTo(prevLocation) > 100) isDistanceRealistic = false;
            prevLocation = location;
        }

        return isDistanceRealistic;
    }
}
