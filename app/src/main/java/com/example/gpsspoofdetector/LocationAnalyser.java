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
        checkCoordinates();
        return this;
    }

    public String getResults() {
        return results.toString();
    }

    // This method checks the longitude, latitude and altitude.
    private void checkCoordinates() {
        boolean altitudeCheck = checkAltitude();
        results.addCheckResults(Check.ALTITUDE, altitudeCheck);

        boolean altitudeDecimalCheck = checkAltitudeDecimal();
        results.addCheckResults(Check.ALTITUDE_DECIMAL, altitudeDecimalCheck);

        boolean longitudeCheck = checkLongitude();
        results.addCheckResults(Check.LONGITUDE, longitudeCheck);

        boolean latitudeCheck = checkLatitude();
        results.addCheckResults(Check.LATITUDE, latitudeCheck);

        boolean isDistanceRealistic = checkDistanceTravelled();
        results.addCheckResults(Check.DISTANCE_TRAVELLED, isDistanceRealistic);
    }

    // This method checks the altitudes of the locations gathered.
    // If the altitudes gathered do not change at all, it is a sign
    // of location spoofing. Returns false if check failed.
    private boolean checkAltitude() {
        double prevAltitude = locations.get(0).getAltitude();
        boolean hasChanged = false;
        for (Location location : locations) {
            double altitude = location.getAltitude();
            int prevAltitudeNumber = (int) prevAltitude;
            double  prevAltitudeDecimal = prevAltitude - (double)prevAltitudeNumber;
            int altitudeNumber = (int) altitude;
            double  altitudeDecimal = altitude - (double)altitudeNumber;
            if (altitude != prevAltitude) hasChanged = true;
            if (prevAltitudeDecimal == altitudeDecimal) {
                hasChanged = false;
            }
            prevAltitude = altitude;
        }
        return hasChanged;
    }

    // This method checks the decimal places of the altitudes of the locations gathered.
    // If the decimal of the altitudes gathered do not change at all, it is a sign
    // of location spoofing. Returns false if check failed.
    private boolean checkAltitudeDecimal() {
        double prevAltitude = locations.get(0).getAltitude();
        boolean hasChanged = false;
        for (Location location : locations) {
            double altitude = location.getAltitude();
            int prevAltitudeNumber = (int) prevAltitude;
            double  prevAltitudeDecimal = prevAltitude - (double)prevAltitudeNumber;
            int altitudeNumber = (int) altitude;
            double  altitudeDecimal = altitude - (double)altitudeNumber;
            if (prevAltitudeDecimal != altitudeDecimal) {
                hasChanged = true;
            }
            prevAltitude = altitude;
        }
        return hasChanged;
    }

    // This method checks the latitude of the locations gathered.
    // If the altitudes gathered do not change at all, it is a sign
    // of location spoofing.
    private boolean checkLatitude() {
        double prevLatitude = locations.get(0).getLatitude();
        boolean hasChanged = false;
        for (Location location : locations) {
            double latitude = location.getLatitude();
            if (latitude != prevLatitude) hasChanged = true;
            prevLatitude = latitude;
        }
        return hasChanged;
    }

    // This method checks the longitude of the locations gathered.
    // If the altitudes gathered do not change at all, it is a sign
    // of location spoofing.
    private boolean checkLongitude() {
        double prevLongitude = locations.get(0).getLongitude();
        boolean hasChanged = false;
        for (Location location : locations) {
            double longitude = location.getLongitude();
            if (longitude != prevLongitude) hasChanged = true;
            prevLongitude = longitude;
        }
        return hasChanged;
    }

    // This method checks the distance travelled of the locations gathered.
    // If the distance travelled is too large (>100m), it is a sign
    // of location spoofing.
    private boolean checkDistanceTravelled() {
        Location prevLocation = locations.get(0);
        boolean isDistanceRealistic = true;
        for (Location location : locations) {
            if (location.distanceTo(prevLocation) > 100) isDistanceRealistic = false;
        }
        return isDistanceRealistic;
    }
}
