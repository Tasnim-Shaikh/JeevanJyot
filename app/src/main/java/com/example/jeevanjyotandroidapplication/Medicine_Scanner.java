package com.example.jeevanjyotandroidapplication;
import android.text.method.ScrollingMovementMethod;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.jeevanjyotandroidapplication.databinding.ActivityDashboardBinding;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Medicine_Scanner extends DrawerBaseForPatient {
    ActivityDashboardBinding activityDashboardBinding;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final String GEMINI_API_KEY = "AIzaSyC68fD23A05xMuFamdVA78QKgcBje9diFk"; // Replace with your API Key

    private ImageView imageView;
    private TextView textViewResult;
    private Bitmap imageBitmap;
    private String extractedText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());

        getLayoutInflater().inflate(R.layout.medicine_scanner, activityDashboardBinding.contentFrame, true);

        imageView = findViewById(R.id.imageView);
        textViewResult = findViewById(R.id.textViewResult);
        Button btnCapture = findViewById(R.id.btnCapture);
        Button btnSelect = findViewById(R.id.btnSelect);
        Button btnExtractText = findViewById(R.id.btnExtractText);

        btnCapture.setOnClickListener(v -> captureImage());
        btnSelect.setOnClickListener(v -> selectImageFromGallery());
        btnExtractText.setOnClickListener(v -> extractTextFromImage());
        textViewResult.setMovementMethod(new ScrollingMovementMethod());
    }

    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
            } else if (requestCode == REQUEST_IMAGE_PICK) {
                Uri imageUri = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            imageView.setImageBitmap(imageBitmap);
        }
    }

    private void extractTextFromImage() {
        if (imageBitmap != null) {
            InputImage image = InputImage.fromBitmap(imageBitmap, 0);
            TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
            recognizer.process(image)
                    .addOnSuccessListener(text -> {
                        extractedText = text.getText();
                       // textViewResult.setText("Extracted Text:\n" + extractedText);
                        // Send extracted text to Gemini API
                        new ProcessTextTask().execute(extractedText);
                    })
                    .addOnFailureListener(e -> textViewResult.setText("Failed to extract text"));
        }
    }

    private class ProcessTextTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String extractedText = params[0];
            return callGeminiAPI(extractedText);
        }

        @Override
        protected void onPostExecute(String result) {
            textViewResult.setText(result);
        }
    }

    private String callGeminiAPI(String inputText) {
        try {
            URL url = new URL("https://generativelanguage.googleapis.com/v1/models/gemini-1.5-pro:generateContent?key=" + GEMINI_API_KEY);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String requestBody = "{ \"contents\": [ { \"role\": \"user\", \"parts\": [ { \"text\": \"Extract medicine names and their uses from the following prescription:\\n\\n" + inputText + "\" } ] } ] }";

            OutputStream os = conn.getOutputStream();
            os.write(requestBody.getBytes());
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                return parseGeminiResponse(response.toString());
            } else {
                return "Error: " + responseCode;
            }
        } catch (Exception e) {
            return "API Request Failed: " + e.getMessage();
        }
    }

    private String parseGeminiResponse(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONArray candidates = jsonObject.getJSONArray("candidates");
            if (candidates.length() > 0) {
                JSONObject firstCandidate = candidates.getJSONObject(0);
                JSONObject content = firstCandidate.getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");

                if (parts.length() > 0) {
                    return parts.getJSONObject(0).getString("text");
                }
            }
            return "No response from Gemini AI";
        } catch (Exception e) {
            return "Error parsing response";
        }
    }
}
