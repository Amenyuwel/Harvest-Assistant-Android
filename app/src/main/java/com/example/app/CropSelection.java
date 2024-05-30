package com.example.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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
        setContentView(R.layout.activity_crop_selection);


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
