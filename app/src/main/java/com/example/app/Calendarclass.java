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
        // Construct the URL with farmer_id and crop_id as GET parameters
        String url = "https://harvest.dermocura.net/PHP_API/get_planting_data.php?farmer_id=" + farmerId + "&crop_id=" + cropId;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Send a GET request to the server to fetch planting data
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            // Extract data from the response
                            JSONObject data = response.getJSONObject("data");

                            // Get the fields
                            String plantingDate = data.getString("date_planted");
                            double area = data.getDouble("area");

                            // The estimated produce and income are calculated based on the area and received from the server
                            double estimatedProduce = data.getDouble("estimated_produce");
                            double estimatedIncome = data.getDouble("estimated_income");

                            // Now, update the UI with the fetched data
                            updateUI(plantingDate, area, estimatedProduce, estimatedIncome);
                        } else {
                            // If the server returns an error
                            Toast.makeText(this, "Error fetching data: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle any network or server errors
                    Toast.makeText(this, "Request failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    private void updateUI(String plantingDate, double area, double estimatedProduce, double estimatedIncome) {
        // Find your TextViews in the layout
        TextView tvSelectedDate = findViewById(R.id.tvSelectedDate);
        TextView tvHectares = findViewById(R.id.tvHectares);
        TextView tvEstimatedProduce = findViewById(R.id.tvEstimatedProduce);
        TextView tvEstimatedIncome = findViewById(R.id.tvEstimatedIncome);

        // Set the data to the TextViews
        tvSelectedDate.setText(plantingDate);
        tvHectares.setText(String.valueOf(area));
        tvEstimatedProduce.setText(String.valueOf(estimatedProduce));  // Set from server response
        tvEstimatedIncome.setText(String.valueOf(estimatedIncome));    // Set from server response
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
