package com.example.gpsspoofdetector;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import java.util.ArrayList;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class GpsSpoofDetectorActivity extends AppCompatActivity {
    private long UPDATE_INTERVAL = 500;  /* 0.5 secs */
    private long FASTEST_INTERVAL = 500; /* 0.5 secs */
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallBack;
    private TextView locationValue;
    private Button buttonStartStop;
    private boolean started = false;
    private ArrayList<Location> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_spoof_detector_activity);
        fusedLocationProviderClient = new FusedLocationProviderClient(this);
        locationValue = findViewById(R.id.locationValue);

        // Get start button
        buttonStartStop = findViewById(R.id.buttonStartStop);

        // Add event listener for the start/stop button
        buttonStartStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!started) {
                    // Reset locations arraylist
                    locations = new ArrayList<>();

                    // Start location services
                    buttonStartStop.setText("Stop Collection and Analyse");
                    locationValue.setText("Fetching Location data...");
                    GpsSpoofDetectorActivityPermissionsDispatcher.startLocationUpdatesWithPermissionCheck(GpsSpoofDetectorActivity.this);

                    // Change start bool to true to indicated started
                    started = true;
                } else {
                    // Stop location services
                    buttonStartStop.setText("Start Detection");
                    locationValue.setText("Currently not fetching location data.");
                    fusedLocationProviderClient.removeLocationUpdates(locationCallBack);

                    // Change start bool to false to indicate not started
                    started = false;

                    // Analyse results
                    LocationAnalyser locationAnalyser = new LocationAnalyser(locations);
                    String results = locationAnalyser.analyseLocations().getResults();
                    Log.d("Results", results);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        GpsSpoofDetectorActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    // Trigger new location updates at interval
    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    protected void startLocationUpdates() {
        // Create the location request to start receiving updates
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // Set location call back
        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    locationValue.setText("Error fetching GPS Long/Lat Values...");
                    return;
                }
                Log.d("Location fetching", "Number of locations fetched: " + locationResult.getLocations().size());
                onLocationChanged(locationResult.getLastLocation());
            }
        };

        // Request updates
        fusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest,
            locationCallBack,
            Looper.getMainLooper()
        );
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        Toast.makeText(this, "Location updated!", Toast.LENGTH_SHORT).show();
        locations.add(location);
        String msg = locations.size() + " location(s) collected.";
        locationValue.setText(msg);
    }
}
