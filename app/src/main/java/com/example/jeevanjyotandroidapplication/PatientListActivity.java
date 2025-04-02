package com.example.jeevanjyotandroidapplication;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jeevanjyotandroidapplication.databinding.ActivityDashboardBinding;

import java.util.ArrayList;
import java.util.List;

public class PatientListActivity extends DrawerBaseActivity {
    private RecyclerView recyclerView;
    private PatientAdapter patientAdapter;
    private List<Patient> patientList;
    ActivityDashboardBinding activityDashboardBinding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());

        // Inflate Doctor's layout inside the drawer
        getLayoutInflater().inflate(R.layout.activity_patient_list, activityDashboardBinding.contentFrame, true);

        recyclerView = findViewById(R.id.recyclerViewPatients);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Dummy patient data
        patientList = new ArrayList<>();
        patientList.add(new Patient("Anushka Gurav", "0123456789"));
        patientList.add(new Patient("Tasnim Shaikh", "0123456789"));
        patientList.add(new Patient("Aishwarya Anekar", "0123456789"));
        patientList.add(new Patient("Shamal Jadhav", "0123456789"));

        patientAdapter = new PatientAdapter(this, patientList);
        recyclerView.setAdapter(patientAdapter);
    }
}
