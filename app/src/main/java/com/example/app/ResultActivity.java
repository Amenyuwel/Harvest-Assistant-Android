package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        TextView tvPestName = findViewById(R.id.tvPestName);
        TextView tvConfidence = findViewById(R.id.tvConfidence);
        TextView tvRecommendation = findViewById(R.id.tvRecommendation);

        Intent intent = getIntent();
        String pestName = intent.getStringExtra("pest_name");
        float confidence = intent.getFloatExtra("confidence", 0);

        tvPestName.setText("Detected Pest: " + pestName);
        tvConfidence.setText("Confidence: " + confidence);

        // Set recommended pesticides based on detected pest
        String recommendation = getRecommendationForPest(pestName);
        tvRecommendation.setText("Recommended Pesticides: " + recommendation);
    }

    private String getRecommendationForPest(String pestName) {
        // This is a simple example, you can have a more sophisticated logic or database lookup here
        switch (pestName.toLowerCase()) {
            case "aphid":
                return "Imidacloprid, Thiamethoxam";
            case "caterpillar":
                return "Bacillus thuringiensis, Spinosad";
            case "mite":
                return "Abamectin, Bifenthrin";
            default:
                return "Consult your local agricultural extension officer.";
        }
    }
}