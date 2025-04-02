package com.example.jeevanjyotandroidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jeevanjyotandroidapplication.databinding.ActivityDashboardBinding;

public class PatientFormActivity extends DrawerBaseForPatient {
    ActivityDashboardBinding activityDashboardBinding;
    private EditText nameInput,s,age,address,mob;
    private Button submitButton;
    private String doctorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());
        // Inflate Doctor's layout inside the drawer
        getLayoutInflater().inflate(R.layout.add_info_of_patient, activityDashboardBinding.contentFrame, true);

        nameInput = findViewById(R.id.name);
        age = findViewById(R.id.age);
        address=findViewById(R.id.Address);
        mob=findViewById(R.id.Mob);
        s=findViewById(R.id.Symptoms);
        submitButton = findViewById(R.id.submitButton);
        doctorName = getIntent().getStringExtra("doctorName");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String symptoms = s.getText().toString();

                if (name.isEmpty() || symptoms.isEmpty()) {
                    Toast.makeText(PatientFormActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(PatientFormActivity.this, "Data sent successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}