package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DisplayActivity extends AppCompatActivity {

    private TextView tvSelectedDate, tvCrop, tvVariant, tvHectares, tvEstimatedProduce, tvEstimatedIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvCrop = findViewById(R.id.tvCrop);
        tvVariant = findViewById(R.id.tvVariant);
        tvHectares = findViewById(R.id.tvHectares);
        tvEstimatedProduce = findViewById(R.id.tvEstimatedProduce);
        tvEstimatedIncome = findViewById(R.id.tvEstimatedIncome);

        Intent intent = getIntent();
        String selectedDate = intent.getStringExtra("selectedDate");
        String crop = intent.getStringExtra("crop");
        String variant = intent.getStringExtra("variant");
        String hectares = intent.getStringExtra("hectares");
        double estimatedProduce = intent.getDoubleExtra("estimatedProduce", 0);
        double estimatedIncome = intent.getDoubleExtra("estimatedIncome", 0);

        tvSelectedDate.setText(selectedDate);
        tvCrop.setText(crop);
        tvVariant.setText(variant);
        tvHectares.setText(hectares);
        tvEstimatedProduce.setText(String.valueOf(estimatedProduce));
        tvEstimatedIncome.setText(String.format("%.2f", estimatedIncome));
    }
}