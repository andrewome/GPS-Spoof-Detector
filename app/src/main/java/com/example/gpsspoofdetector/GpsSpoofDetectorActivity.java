package com.example.gpsspoofdetector;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
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

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class GpsSpoofDetectorActivity extends AppCompatActivity {
    private long UPDATE_INTERVAL = 500;  /* 0.5 secs */
    private long FASTEST_INTERVAL = 500; /* 0.5 secs */
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView locationValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gps_spoof_detector);

        // Start location services
        fusedLocationProviderClient = new FusedLocationProviderClient(this);
        locationValue = findViewById(R.id.locationValue);
        locationValue.setText("Fetching GPS Long/Lat Values...");
        GpsSpoofDetectorActivityPermissionsDispatcher.startLocationUpdatesWithPermissionCheck(this);
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

        // Request updates
        fusedLocationProviderClient.requestLocationUpdates(
            mLocationRequest,
            new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        locationValue.setText("Error fetching GPS Long/Lat Values...");
                        return;
                    }
                    Log.d("Location fetching", "Number of locations fetched: " + locationResult.getLocations().size());
                    onLocationChanged(locationResult.getLastLocation());
                }
            },
            Looper.getMainLooper()
        );
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location:\n" + location.toString();
        Toast.makeText(this, "Updated Location!", Toast.LENGTH_SHORT).show();
        locationValue.setText(msg);
    }
}
