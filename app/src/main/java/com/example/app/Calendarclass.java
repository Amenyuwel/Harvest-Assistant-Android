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
        String url = "https://harvest.dermocura.net/PHP_API/fetch_calendar.php";  // Replace with actual API endpoint

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
                            JSONObject data = response.getJSONObject("data");

                            // Extract data from response
                            String plantingDate = data.getString("date_planted");
                            String crop = data.getString("crop");
                            double area = data.getDouble("hectares");
                            double estimatedProduce = data.getDouble("estimated_produce");
                            double estimatedIncome = data.getDouble("estimated_income");

                            // Update the UI with the fetched data
                            updateUI(plantingDate, crop, area, estimatedProduce, estimatedIncome);
                        } else {
                            Toast.makeText(this, "Error fetching data: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                    Log.e("APIError", "Request failed: " + error.toString());
                    if (error.networkResponse != null) {
                        String errorResponse = new String(error.networkResponse.data);
                        Log.e("APIErrorResponse", "Error response: " + errorResponse);
                    }
                    Toast.makeText(this, "Request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    private void updateUI(String plantingDate, String crop, double area, double estimatedProduce, double estimatedIncome) {
        // Find your TextViews in the layout
        TextView tvSelectedDate = findViewById(R.id.tvSelectedDate);
        TextView tvCrop = findViewById(R.id.tvCrop);
        TextView tvHectares = findViewById(R.id.tvHectares);
        TextView tvEstimatedProduce = findViewById(R.id.tvEstimatedProduce);
        TextView tvEstimatedIncome = findViewById(R.id.tvEstimatedIncome);

        // Set the data to the TextViews
        tvSelectedDate.setText(plantingDate);
        tvCrop.setText(crop);
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
