package com.example.jeevanjyotandroidapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.example.jeevanjyotandroidapplication.databinding.ActivityDashboardBinding;
import com.example.jeevanjyotandroidapplication.BuildConfig;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Arrays;
import java.util.List;

public class ChatBot extends DrawerBaseForPatient {

    ActivityDashboardBinding activityDashboardBinding;
    private EditText userInput;
    private Button sendButton;
    private LinearLayout chatLayout;
    private ScrollView scrollView;
    private GenerativeModelFutures model;

    // List of keywords to allow health-related queries
    private final List<String> healthKeywords = Arrays.asList(
            "fever", "cough", "cold", "flu", "headache", "pain", "remedy",
            "symptom", "treatment", "infection", "medicine", "doctor", "hospital",
            "emergency", "first aid", "blood pressure", "diabetes", "health"
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityDashboardBinding.getRoot());

        getLayoutInflater().inflate(R.layout.userchat, activityDashboardBinding.contentFrame, true);

        userInput = findViewById(R.id.userInput);
        sendButton = findViewById(R.id.sendButton);
        chatLayout = findViewById(R.id.chatLayout);
        scrollView = findViewById(R.id.scrollView);

        // Initialize Gemini API model
        GenerativeModel gm = new GenerativeModel("models/gemini-1.5-pro",
                BuildConfig.API_KEY); // Replace with your API key
        model = GenerativeModelFutures.from(gm);

        sendButton.setOnClickListener(view -> {
            String query = userInput.getText().toString().trim();
            if (!query.isEmpty()) {
                addMessage("You: " + query, true);

                if (isHealthRelated(query)) {
                    fetchGeminiResponse(query);
                } else {
                    addMessage("Gemini: Sorry, I can only assist with health-related queries.", false);
                }
                userInput.setText("");
                userInput.setTextColor(Color.BLACK);
            }
        });
    }

    // Check if the query is related to health
    private boolean isHealthRelated(String query) {
        String lowerCaseQuery = query.toLowerCase();
        for (String keyword : healthKeywords) {
            if (lowerCaseQuery.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    // Fetch response from Gemini API
    private void fetchGeminiResponse(String query) {
        Content content = new Content.Builder().addText(query).build();
        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    String resultText = result.getText();
                    String finalResponse = resultText + "\n\n**This is just a remedy. Please consult a specialized doctor.**";
                    runOnUiThread(() -> addMessage("Gemini: " + finalResponse, false));
                }

                @Override
                public void onFailure(Throwable t) {
                    Log.e("GeminiAPI", "Error: " + t.toString());
                    runOnUiThread(() -> addMessage("Gemini: Error fetching response", false));
                }
            }, getMainExecutor());
        }
    }

    // Function to add a message to the chat layout
    private void addMessage(String message, boolean isUser) {
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setPadding(16, 8, 16, 8);

        if (isUser) {
            textView.setBackgroundResource(R.drawable.rounded_bg);
        } else {
            textView.setBackgroundResource(R.drawable.rounded_bg);
        }

        chatLayout.addView(textView);
        scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN)); // Auto-scroll to the latest message
    }
}
