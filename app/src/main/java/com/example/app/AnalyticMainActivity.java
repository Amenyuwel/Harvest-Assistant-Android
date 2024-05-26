package com.example.app;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class AnalyticMainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analytics_layout);

        // To remove ActionBar Title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }

        ImageView backButton = findViewById(R.id.analyticsBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to handle back button click
                onBackButtonClick();
            }
        });
    }

    private void onBackButtonClick() {
        // Handle back button click, for example:
        finish();
    }
}
