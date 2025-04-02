package com.example.jeevanjyotandroidapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.jeevanjyotandroidapplication.databinding.ActivityDashboardBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PrescriptionActivity extends DrawerBaseForPatient {
    ActivityDashboardBinding activityDashboardBinding;
    private TextView doctorInfo, patientInfo, prescriptionDetails, additionalNotes;
    private ScrollView scrollView;
    private static final String CUSTOM_DIR = "JivanJyot_Prescriptions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());
        // Inflate Doctor's layout inside the drawer
        getLayoutInflater().inflate(R.layout.prescription, activityDashboardBinding.contentFrame, true);

        doctorInfo = findViewById(R.id.doctorInfo);
        patientInfo = findViewById(R.id.patientInfo);
        prescriptionDetails = findViewById(R.id.prescriptionDetails);
        additionalNotes = findViewById(R.id.additionalNotes);
        scrollView = findViewById(R.id.scrollView);

        Button downloadPngButton = findViewById(R.id.downloadPngButton);
        Button downloadPdfButton = findViewById(R.id.downloadPdfButton);

        doctorInfo.setText("Doctor: Dr. A. Sharma\nSpecialization: Cardiologist\nContact: +91 9876543210");
        patientInfo.setText("Patient: Rahul Verma\nAge: 30\nGender: Male\nDate: 16/03/2025");
        prescriptionDetails.setText("Prescription Details:\n1. Paracetamol - 500mg - Twice daily\n2. Amoxicillin - 250mg - Thrice daily");
        additionalNotes.setText("Additional Notes: Drink plenty of water and take rest.");

        downloadPngButton.setOnClickListener(v -> downloadPrescriptionAsPNG());
        downloadPdfButton.setOnClickListener(v -> downloadPrescriptionAsPDF());

        requestPermission();
    }

    private void requestPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    private void downloadPrescriptionAsPNG() {
        Bitmap bitmap = getScrollViewBitmap();
        saveBitmapAsFile(bitmap, "png");
    }

    private void downloadPrescriptionAsPDF() {
        Bitmap bitmap = getScrollViewBitmap();
        saveBitmapAsPDF(bitmap);
    }

    private Bitmap getScrollViewBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(scrollView.getWidth(), scrollView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        scrollView.draw(canvas);
        return bitmap;
    }

    private void saveBitmapAsFile(Bitmap bitmap, String format) {
        String fileName = "Prescription_" + System.currentTimeMillis() + "." + format;

        // Custom Directory Path
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), CUSTOM_DIR);

        if (!directory.exists()) {
            directory.mkdirs(); // Create directory if it doesn't exist
        }

        File file = new File(directory, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            if (format.equals("png")) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            }
            Toast.makeText(this, "File saved at: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save file", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveBitmapAsPDF(Bitmap bitmap) {
        String fileName = "Prescription_" + System.currentTimeMillis() + ".pdf";

        // Custom Directory Path
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), CUSTOM_DIR);

        if (!directory.exists()) {
            directory.mkdirs(); // Create directory if it doesn't exist
        }

        File file = new File(directory, fileName);

        try (FileOutputStream fos = new FileOutputStream(file)) {
            PdfDocument pdfDocument = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            Canvas pdfCanvas = page.getCanvas();
            pdfCanvas.drawBitmap(bitmap, 0, 0, null);

            pdfDocument.finishPage(page);
            pdfDocument.writeTo(fos);
            pdfDocument.close();

            Toast.makeText(this, "PDF saved at: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save PDF", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission granted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
        }
    }
}