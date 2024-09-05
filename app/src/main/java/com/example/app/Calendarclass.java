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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Calendarclass extends AppCompatActivity {

    ImageButton ivDate, calendarBackButton;
    private TextView tvGrowthStage, tvStageDescription, tvStageSuggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.darker_matcha));
        setContentView(R.layout.calendar_activity);

        // Set the ActionBar background color
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.darker_matcha)));
            actionBar.setTitle(""); // Remove ActionBar Title
            actionBar.setDisplayHomeAsUpEnabled(true); // Show the back button in action bar
        }

        ivDate = findViewById(R.id.ivDate);
        calendarBackButton = findViewById(R.id.calendarBackButton);
        tvGrowthStage = findViewById(R.id.tvGrowthStage);
        tvStageDescription = findViewById(R.id.tvStageDescription);
        tvStageSuggestions = findViewById(R.id.tvStageSuggestions);

        ivDate.setOnClickListener(view -> {
            Intent i = new Intent(Calendarclass.this, FormActivity.class);
            startActivity(i);
        });

        calendarBackButton.setOnClickListener(view -> {
            Intent i = new Intent(Calendarclass.this, Dashboard.class);
            startActivity(i);
        });

        // Example usage of fetching planting data
        fetchPlantingData(1, 2); // Replace with actual farmerId and cropId
    }

    private void fetchPlantingData(int farmerId, int cropId) {
        String url = "https://harvest.dermocura.net/PHP_API/get_planting_data.php";  // Replace with actual API endpoint

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Create a JSONObject to send as parameters
        JSONObject params = new JSONObject();
        try {
            params.put("farmer_id", farmerId);
            params.put("crop_id", cropId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            // Extract data from response
                            String plantingDate = response.getString("planting_date");
                            String crop = response.getString("crop");
                            String cropVariant = response.getString("crop_variant");
                            double area = response.getDouble("area");
                            double estimatedProduce = response.getDouble("estimated_produce");
                            double estimatedIncome = response.getDouble("estimated_income");

                            // Now, set the data to your UI components
                            updateUI(plantingDate, crop, cropVariant, area, estimatedProduce, estimatedIncome);
                        } else {
                            Toast.makeText(this, "Error fetching data: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                    Toast.makeText(this, "Request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    private void updateUI(String plantingDate, String crop, String cropVariant, double area, double estimatedProduce, double estimatedIncome) {
        // Find your TextViews in the layout
        TextView tvSelectedDate = findViewById(R.id.tvSelectedDate);
        TextView tvCrop = findViewById(R.id.tvCrop);
        TextView tvVariant = findViewById(R.id.tvVariant);
        TextView tvHectares = findViewById(R.id.tvHectares);
        TextView tvEstimatedProduce = findViewById(R.id.tvEstimatedProduce);
        TextView tvEstimatedIncome = findViewById(R.id.tvEstimatedIncome);

        // Set the data to the TextViews
        tvSelectedDate.setText(plantingDate);
        tvCrop.setText(crop);
        tvVariant.setText(cropVariant);
        tvHectares.setText(String.valueOf(area));
        tvEstimatedProduce.setText(String.valueOf(estimatedProduce));
        tvEstimatedIncome.setText(String.valueOf(estimatedIncome));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

//
//    // This event will enable the back function to the button on press
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                this.finish();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}
//
//        // Retrieve data from the intent
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            String selectedDate = extras.getString("selectedDate");
//            String selectedCrop = extras.getString("crop");
//            String variant = extras.getString("variant");
//            String hectares = extras.getString("hectares");
//            // Calculate estimated produce per sack and estimated income per sack
//            double estimatedProducePerSack = Double.parseDouble(hectares) * 5; // Assume 5 tons per hectare
//            double estimatedIncomePerSack = estimatedProducePerSack * 40.50; // 40.50 pesos per kilogram
//
//            // Update TextViews with the retrieved data
//            TextView tvSelectedDate = findViewById(R.id.tvSelectedDate);
//            tvSelectedDate.setText(selectedDate);
//
//            TextView tvCrop = findViewById(R.id.tvCrop);
//            tvCrop.setText(selectedCrop);
//
//            TextView tvVariant = findViewById(R.id.tvVariant);
//            tvVariant.setText(variant);
//
//            TextView tvHectares = findViewById(R.id.tvHectares);
//            tvHectares.setText(hectares);
//
//            TextView tvEstimatedProduce = findViewById(R.id.tvEstimatedProduce);
//            tvEstimatedProduce.setText(String.format("%.2f tons", estimatedProducePerSack));
//
//            TextView tvEstimatedIncome = findViewById(R.id.tvEstimatedIncome);
//            tvEstimatedIncome.setText(String.format("%.2f PHP", estimatedIncomePerSack));
//
//            int weeksSincePlanting = calculateWeeksSince(selectedDate);
//            GrowthStageData.GrowthStage growthStage = GrowthStageData.getGrowthStage(selectedCrop, weeksSincePlanting);
//
//            tvGrowthStage.setText("Stage: " + growthStage.stage);
//            tvStageDescription.setText("Description: " + growthStage.description);
//            tvStageSuggestions.setText("Suggestions: " + growthStage.suggestions);
//        }
//    }
//
//    private int calculateWeeksSince(String date) {
//        // (Implementation of date difference calculation from previous code)
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
//        try {
//            Date plantingDate = sdf.parse(date);
//            long diffInMillis = new Date().getTime() - plantingDate.getTime();
//            long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
//            return (int) (diffInDays / 7);  // Convert days to weeks
//        } catch (Exception e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }


