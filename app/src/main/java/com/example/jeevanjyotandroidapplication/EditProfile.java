package com.example.jeevanjyotandroidapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jeevanjyotandroidapplication.databinding.ActivityDashboardBinding;

public class EditProfile extends DrawerBaseActivity {
    ActivityDashboardBinding activityDashboardBinding;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());

        // Inflate Doctor's layout inside the drawer
        getLayoutInflater().inflate(R.layout.editprofile, activityDashboardBinding.contentFrame, true);
    }
}
