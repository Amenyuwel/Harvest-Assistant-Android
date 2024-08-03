package com.example.app;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CropSelection extends AppCompatActivity {


    private CardView cardViewCorn;
    private CardView cardViewRice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_crop_selection);

        // Set the ActionBar background color
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the ActionBar background color
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.matcha)));
            // Remove ActionBar Title
            actionBar.setTitle("");
        }


            cardViewCorn = findViewById(R.id.cardViewCorn);
            cardViewRice = findViewById(R.id.cardViewRice);

            cardViewCorn.setOnClickListener(v -> {
                Intent intent = new Intent(CropSelection.this, GrowthStageActivity.class);
                startActivity(intent);
            });

            cardViewRice.setOnClickListener(v -> {
                Intent intent = new Intent(CropSelection.this, GrowthStageActivity2.class);
                startActivity(intent);
            });
        }
    }
