package com.example.jeevanjyotandroidapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.jeevanjyotandroidapplication.databinding.ActivityDashboardBinding;

public class Appointment extends DrawerBaseActivity {
    ActivityDashboardBinding activityDashboardBinding;

    private static final int FILE_PICKER_REQUEST_CODE = 100;
    private TextView fileNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());

        // Inflate Doctor's layout inside the drawer
        getLayoutInflater().inflate(R.layout.appointment, activityDashboardBinding.contentFrame, true);

        // Initialize UI elements
        ImageButton callButton = findViewById(R.id.call);
        ImageButton msgButton = findViewById(R.id.msg);
        ImageButton videoButton = findViewById(R.id.meet);
        ImageButton mailButton = findViewById(R.id.mail);
        Button chooseFileButton = findViewById(R.id.choose_file_button);
        Button bookAppointmentButton = findViewById(R.id.book);
        fileNameText = findViewById(R.id.file_name_text);

        // Call Button: Open Dialer with Default Number
        callButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:0123456789"));
            startActivity(intent);
        });

        // Message Button: Open SMS App with Default Message
        msgButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("smsto:0123456789"));
            intent.putExtra("sms_body", "Hello patient");
            startActivity(intent);
        });

        // Video Call Button: Open WhatsApp/Google Meet (Example: WhatsApp)
        videoButton.setOnClickListener(view -> {
            Uri uri = Uri.parse("https://meet.google.com/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        // Mail Button: Open Email Client with Default Message
        mailButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:doctor@example.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Appointment Inquiry");
            intent.putExtra(Intent.EXTRA_TEXT, "Hello Doctor, I want to book an appointment.");
            startActivity(Intent.createChooser(intent, "Send Email"));
        });

        // File Chooser: Open File Picker
        chooseFileButton.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
        });

        // Book Appointment Button: Show Success Message
        bookAppointmentButton.setOnClickListener(view -> {
            Toast.makeText(Appointment.this, "Appointment Scheduled Successfully!", Toast.LENGTH_LONG).show();
        });
    }

    // Handle File Selection Result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedFileUri = data.getData();
            if (selectedFileUri != null) {
                fileNameText.setText(selectedFileUri.getLastPathSegment());
            }
        }
    }
}
