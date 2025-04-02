package com.example.jeevanjyotandroidapplication;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jeevanjyotandroidapplication.AlarmReceiver;
import com.example.jeevanjyotandroidapplication.R;
import com.example.jeevanjyotandroidapplication.databinding.ActivityDashboardBinding;

import java.util.Calendar;

public class Medicine_Remainder extends DrawerBaseForPatient {
    ActivityDashboardBinding activityDashboardBinding;
    private TimePicker timePicker;
    private EditText medicineName, durationDays;
    private Button saveButton;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());

        getLayoutInflater().inflate(R.layout.medicine_remainder, activityDashboardBinding.contentFrame, true);

        timePicker = findViewById(R.id.timePicker);
        medicineName = findViewById(R.id.medicineName);
        durationDays = findViewById(R.id.durationDays);
        saveButton = findViewById(R.id.saveButton);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        createNotificationChannel();

        saveButton.setOnClickListener(v -> {
            String name = medicineName.getText().toString();
            String durationText = durationDays.getText().toString();
            if (name.isEmpty() || durationText.isEmpty()) {
                Toast.makeText(this, "Please enter medicine name and duration", Toast.LENGTH_SHORT).show();
                return;
            }

            int duration = Integer.parseInt(durationText);
            setAlarm(name, duration);
            Toast.makeText(this, "Reminder Set Successfully", Toast.LENGTH_SHORT).show();
        });
    }

    private void setAlarm(String name, int duration) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
        calendar.set(Calendar.MINUTE, timePicker.getMinute());
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(Medicine_Remainder.this, AlarmReceiver.class);
        intent.putExtra("medicineName", name);

        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        long intervalMillis = AlarmManager.INTERVAL_DAY; // Repeat every day
        long endTime = System.currentTimeMillis() + (duration * 24 * 60 * 60 * 1000L); // Duration in days

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalMillis, pendingIntent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MedicineReminderChannel";
            String description = "Channel for Medicine Reminder";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("notifyMedicine", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}