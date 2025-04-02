package com.example.jeevanjyotandroidapplication;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class GeminiApiService {
    private static final String API_KEY = "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"; // Store securely, replace with secrets.xml
    private static final String API_URL = "https://openrouter.ai/api/v1/chat/completions";
    private final OkHttpClient client = new OkHttpClient();

    public String getChatbotResponse(String userMessage) throws IOException {
        try {
            // Build request JSON
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", "google/gemini-2.0-pro-exp-02-05:free");

            JSONArray messagesArray = new JSONArray();
            JSONObject userMessageObj = new JSONObject();
            userMessageObj.put("role", "user");
            userMessageObj.put("content", userMessage);
            messagesArray.put(userMessageObj);

            jsonBody.put("messages", messagesArray);
            jsonBody.put("max_tokens", 200);

            // Create HTTP request
            RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.get("application/json"));
            Request request = new Request.Builder()
                    .url(API_URL)
                    .post(body)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .build();

            // Execute API request
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    JSONObject jsonResponse = new JSONObject(response.body().string());
                    JSONArray choices = jsonResponse.optJSONArray("choices");

                    if (choices != null && choices.length() > 0) {
                        JSONObject messageObj = choices.getJSONObject(0).optJSONObject("message");
                        if (messageObj != null) {
                            return messageObj.getString("content");
                        }
                    }
                    return "No response from Gemini.";
                } else {
                    return "Error " + response.code() + ": " + response.message();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
