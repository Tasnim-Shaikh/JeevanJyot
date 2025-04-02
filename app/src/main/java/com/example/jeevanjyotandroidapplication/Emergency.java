package com.example.jeevanjyotandroidapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.jeevanjyotandroidapplication.databinding.ActivityDashboardBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class Emergency extends DrawerBaseForPatient {

    private static final int REQUEST_CALL = 1;
    private static final int REQUEST_SMS = 2;
    private static final int REQUEST_LOCATION = 3;

    private String emergencyNumber = "102"; // Ambulance Number
    private String emergencyContact = "9689409536"; // User-defined Contact
    private String userLocation = "Location Not Available";

    private FusedLocationProviderClient fusedLocationProviderClient;
    ActivityDashboardBinding activityDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());

        // Inflate Doctor's layout inside the drawer
        getLayoutInflater().inflate(R.layout.emergency, activityDashboardBinding.contentFrame, true);

        Button btnSOS = findViewById(R.id.btnSOS);
        Button btnSettings = findViewById(R.id.btnSettings);

        // Initialize Fused Location Provider
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Get current location
        requestLocation();

        // SOS Button Click
        btnSOS.setOnClickListener(view -> {
            makeEmergencyCall();
            sendEmergencySMS();
        });

        // Redirect to Settings Page
        btnSettings.setOnClickListener(view -> {
            Intent intent = new Intent(Emergency.this, Setting.class);
            startActivity(intent);
        });
    }

    // Method to make an emergency call
    private void makeEmergencyCall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + emergencyNumber));
            startActivity(callIntent);
        }
    }

    // Method to send emergency SMS
    private void sendEmergencySMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS);
        } else {
            SmsManager smsManager = SmsManager.getDefault();
            String message = "🚨 Emergency! Need help. My Location: " + userLocation;
            smsManager.sendTextMessage(emergencyContact, null, message, null, null);
            Toast.makeText(this, "Emergency SMS Sent", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to get current location
    private void requestLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    userLocation = "https://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();
                } else {
                    userLocation = "Location Not Available";
                }
            });
        }
    }

    // Handle Permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            makeEmergencyCall();
        }
        if (requestCode == REQUEST_SMS && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            sendEmergencySMS();
        }
        if (requestCode == REQUEST_LOCATION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            requestLocation();
        }
    }
}
