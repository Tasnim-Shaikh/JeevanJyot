package com.example.jeevanjyotandroidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jeevanjyotandroidapplication.databinding.ActivityDashboardBinding;

public class DoctorHome extends DrawerBaseActivity {

    ActivityDashboardBinding activityDashboardBinding;
    LinearLayout linearLayoutForProfile,myPatient,appoint,chat;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_for_doctor);

        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());

        // Inflate Doctor's layout inside the drawer
        getLayoutInflater().inflate(R.layout.activity_main_for_doctor, activityDashboardBinding.contentFrame, true);
        linearLayoutForProfile=findViewById(R.id.profileId);
        myPatient=findViewById(R.id.myPatient);
        appoint=findViewById(R.id.appoint);
        chat=findViewById(R.id.chat);
        linearLayoutForProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  Intent it=new Intent(DoctorHome.this,EditProfile.class);
                  startActivity(it);
                }
            });
        myPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(DoctorHome.this,PatientListActivity.class);
                startActivity(it);
            }
        });
        appoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(DoctorHome.this, Appointment.class);
                startActivity(it);
            }
        });
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it=new Intent(DoctorHome.this, ChatBot.class);
                startActivity(it);
            }
        });
    }
}
