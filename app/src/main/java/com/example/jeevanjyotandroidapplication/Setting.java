package com.example.jeevanjyotandroidapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jeevanjyotandroidapplication.databinding.ActivityDashboardBinding;

public class Setting extends DrawerBaseForPatient {

    private EditText etEmergencyMessage;
    private Switch switchLocation;
    private Button btnSaveSettings;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "SOS_Prefs";
    ActivityDashboardBinding activityDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());

        // Inflate Doctor's layout inside the drawer
        getLayoutInflater().inflate(R.layout.setting_activity, activityDashboardBinding.contentFrame, true);

        etEmergencyMessage = findViewById(R.id.etEmergencyMessage);
        switchLocation = findViewById(R.id.switchLocation);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);

        // SharedPreferences for temporary data storage
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        loadSavedData();

        btnSaveSettings.setOnClickListener(v -> saveSettings());
    }

    private void saveSettings() {
        String emergencyMessage = etEmergencyMessage.getText().toString().trim();
        boolean isLocationEnabled = switchLocation.isChecked();

        if (emergencyMessage.isEmpty()) {
            etEmergencyMessage.setError("Emergency message cannot be empty");
            return;
        }

        // Save in SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("EmergencyMessage", emergencyMessage);
        editor.putBoolean("LocationSharing", isLocationEnabled);
        editor.apply();

        Toast.makeText(this, "Settings Saved Successfully", Toast.LENGTH_SHORT).show();
    }

    private void loadSavedData() {
        String savedMessage = sharedPreferences.getString("EmergencyMessage", "");
        boolean isLocationEnabled = sharedPreferences.getBoolean("LocationSharing", false);

        etEmergencyMessage.setText(savedMessage);
        switchLocation.setChecked(isLocationEnabled);
    }
}
