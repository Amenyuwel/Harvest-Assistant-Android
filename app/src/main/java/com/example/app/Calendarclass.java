package com.example.app;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import androidx.core.content.ContextCompat;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Calendarclass extends AppCompatActivity {

    ImageButton ivDate, calendarBackButton;
    private TextView tvSelectedDate, tvCrop, tvVariant, tvHectares, tvEstimatedProduce, tvEstimatedIncome;

    private TextView tvGrowthStage, tvStageDescription, tvStageSuggestions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.darker_matcha));
        setContentView(R.layout.calendar_activity);

        // Set the ActionBar background color
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the ActionBar background color
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.darker_matcha)));
            // Remove ActionBar Title
            actionBar.setTitle("");
            // Show the back button in action bar
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ivDate = findViewById(R.id.ivDate);
        calendarBackButton = findViewById(R.id.calendarBackButton);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
        tvCrop = findViewById(R.id.tvCrop);
        tvVariant = findViewById(R.id.tvVariant);
        tvHectares = findViewById(R.id.tvHectares);
        tvEstimatedProduce = findViewById(R.id.tvEstimatedProduce);
        tvEstimatedIncome = findViewById(R.id.tvEstimatedIncome);
        tvGrowthStage = findViewById(R.id.tvGrowthStage);
        tvStageDescription = findViewById(R.id.tvStageDescription);
        tvStageSuggestions = findViewById(R.id.tvStageSuggestions);

        ivDate = findViewById(R.id.ivDate);

        ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Calendarclass.this, FormActivity.class);
                startActivity(i);
            }
        });

        calendarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Calendarclass.this, Dashboard.class);
                startActivity(i);
            }
        });

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

    // This event will enable the back function to the button on press
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

        // Retrieve data from the intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String selectedDate = extras.getString("selectedDate");
            String selectedCrop = extras.getString("crop");
            String variant = extras.getString("variant");
            String hectares = extras.getString("hectares");
            // Calculate estimated produce per sack and estimated income per sack
            double estimatedProducePerSack = Double.parseDouble(hectares) * 5; // Assume 5 tons per hectare
            double estimatedIncomePerSack = estimatedProducePerSack * 40.50; // 40.50 pesos per kilogram

            // Update TextViews with the retrieved data
            TextView tvSelectedDate = findViewById(R.id.tvSelectedDate);
            tvSelectedDate.setText(selectedDate);

            TextView tvCrop = findViewById(R.id.tvCrop);
            tvCrop.setText(selectedCrop);

            TextView tvVariant = findViewById(R.id.tvVariant);
            tvVariant.setText(variant);

            TextView tvHectares = findViewById(R.id.tvHectares);
            tvHectares.setText(hectares);

            TextView tvEstimatedProduce = findViewById(R.id.tvEstimatedProduce);
            tvEstimatedProduce.setText(String.format("%.2f tons", estimatedProducePerSack));

            TextView tvEstimatedIncome = findViewById(R.id.tvEstimatedIncome);
            tvEstimatedIncome.setText(String.format("%.2f PHP", estimatedIncomePerSack));

            int weeksSincePlanting = calculateWeeksSince(selectedDate);
            GrowthStageData.GrowthStage growthStage = GrowthStageData.getGrowthStage(selectedCrop, weeksSincePlanting);

            tvGrowthStage.setText("Stage: " + growthStage.stage);
            tvStageDescription.setText("Description: " + growthStage.description);
            tvStageSuggestions.setText("Suggestions: " + growthStage.suggestions);
        }
    }

    private int calculateWeeksSince(String date) {
        // (Implementation of date difference calculation from previous code)
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date plantingDate = sdf.parse(date);
            long diffInMillis = new Date().getTime() - plantingDate.getTime();
            long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
            return (int) (diffInDays / 7);  // Convert days to weeks
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
