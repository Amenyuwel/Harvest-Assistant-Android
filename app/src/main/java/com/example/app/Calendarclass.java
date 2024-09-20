package com.example.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Calendarclass extends AppCompatActivity {

    ImageButton ivDate, calendarBackButton;
    private TextView tvGrowthStage, tvStageDescription, tvStageSuggestions;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    Button btnDone;

    String TAG = "EmmanKissZhak";
    String URL = "https://harvest.dermocura.net/PHP_API/fetch_calendar.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.darker_matcha));
        setContentView(R.layout.calendar_activity);

        // Set the ActionBar background color


        ivDate = findViewById(R.id.ivDate);
        calendarBackButton = findViewById(R.id.calendarBackButton);
        tvGrowthStage = findViewById(R.id.tvGrowthStage);
        tvStageDescription = findViewById(R.id.tvStageDescription);
        tvStageSuggestions = findViewById(R.id.tvStageSuggestions);
        btnDone = findViewById(R.id.btnDone);

        ivDate.setOnClickListener(view -> {
            Intent i = new Intent(Calendarclass.this, FormActivity.class);
            startActivity(i);
        });

        calendarBackButton.setOnClickListener(view -> {
            Intent i = new Intent(Calendarclass.this, Dashboard.class);
            startActivity(i);
        });

        btnDone.setOnClickListener(view -> {
            // Get the current date in the format you need (e.g., YYYY-MM-DD)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdf.format(Calendar.getInstance().getTime());

            // Assuming you have the farmerID and cropID already available
            int farmerID = SharedPreferenceManager.getInstance(this).getFarmerID();
            int cropID = 1; // Get the actual crop ID based on the data

            if (farmerID != -1 && cropID != -1) {
                postHarvestDate(farmerID, cropID, currentDate);
            } else {
                Toast.makeText(this, "Farmer ID or Crop ID not found", Toast.LENGTH_SHORT).show();
            }
        });




        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Get farmerId from SharedPreferences (assuming it's stored during login)
        int farmerID = SharedPreferenceManager.getInstance(this).getFarmerID();
        if (farmerID != -1) {
            // Assume cropId is provided dynamically or set it as a default value
//            int cropId = 1; // Replace this with the actual crop ID you need
            makeHTTPRequest(farmerID);
        } else {
            Toast.makeText(this, "Farmer ID not found. Please log in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeHTTPRequest(int farmerID) {
        // Define keys for the JSON request body
        String keyEmail = "farmer_id";

        // Create a JSON object for the request body
        JSONObject requestBody = new JSONObject();

        // Create a Volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Populate the JSON request body
        try {
            requestBody.put(keyEmail, farmerID);
        } catch (JSONException e) {
            Log.e(TAG + " makeHTTPRequest", String.valueOf(e));
            return;
        }

        // Create a JsonObjectRequest for a POST request to the specified URL
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                requestBody,
                this::onRequestSuccess,
                this::onRequestError
        );

        // Log the JSON request body for debugging
        String stringJSON = requestBody.toString();
        Log.i(TAG + " makeHTTPRequest", stringJSON);

        // Add the request to the Volley request queue
        queue.add(request);
    }

    private void postHarvestDate(int farmerID, int cropID, String dateHarvested) {
        String postURL = "https://harvest.dermocura.net/PHP_API/update_harvest.php"; // Adjust URL if needed

        // Create a JSON object for the request body
        JSONObject requestBody = new JSONObject();

        // Populate the JSON request body
        try {
            requestBody.put("farmer_id", farmerID);
            requestBody.put("crop_id", cropID);
            requestBody.put("date_harvested", dateHarvested);
        } catch (JSONException e) {
            Log.e(TAG + " postHarvestDate", "Error creating JSON body: " + e.getMessage());
            return;
        }

        // Use Volley to send a POST request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                postURL,
                requestBody,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Toast.makeText(Calendarclass.this, "Harvest date successfully updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            String message = response.optString("message", "Failed to update harvest date");
                            Toast.makeText(Calendarclass.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG + " postHarvestDate", "Error parsing response: " + e.getMessage());
                    }
                },
                error -> {
                    Log.e(TAG + " postHarvestDate", "Volley error: " + error.getMessage());
                    Toast.makeText(Calendarclass.this, "Failed to update harvest date", Toast.LENGTH_SHORT).show();
                }
        );

        // Add the request to the Volley queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


    private void onRequestSuccess(JSONObject response) {
        try {
            // Extract success status from the JSON response
            boolean success = response.getBoolean("success");

            if (success) {
                // Extract the data object
                JSONObject data = response.getJSONObject("data");

                // Extract individual fields from the data object
                double area = data.getDouble("area");
                String datePlanted = data.getString("date_planted");
                double estimatedProduce = data.getDouble("estimated_produce");
                double estimatedIncome = data.getDouble("estimated_income");
                String cropName = data.getString("crop_name");

                // Log the extracted values
                Log.d(TAG + " onRequestSuccess", "Area: " + area);
                Log.d(TAG + " onRequestSuccess", "Date Planted: " + datePlanted);
                Log.d(TAG + " onRequestSuccess", "Estimated Produce: " + estimatedProduce);
                Log.d(TAG + " onRequestSuccess", "Estimated Income: " + estimatedIncome);
                Log.d(TAG + " onRequestSuccess", "Crop Name: " + cropName);

//                updateUI(String plantingDate, double area, double estimatedProduce, double estimatedIncome)
                updateUI(datePlanted, area, estimatedProduce, estimatedIncome, cropName);

                // Handle the response data as needed (e.g., update UI)
                // Example: updateTextView(area, datePlanted, estimatedProduce, estimatedIncome, cropName);

            } else {
                // Handle failure case
                String message = response.optString("message", "Unknown error");
                Log.e(TAG + " onRequestSuccess", "Error: " + message);
            }

        } catch (JSONException e) {
            // Handle JSON parsing exceptions
            Log.e(TAG + " onRequestSuccess", "JSON Parsing error: " + e.getMessage());
        }
    }


    private void onRequestError(VolleyError error) {
        // Log and highlight entry
        Log.e(TAG + " onRequestError", "Error Response: " + error.getMessage());
    }

    private void updateUI(String plantingDate, double area, double estimatedProduce, double estimatedIncome, String cropname) {
        TextView tvSelectedDate = findViewById(R.id.tvSelectedDate);
        TextView tvHectares = findViewById(R.id.tvHectares);
        TextView tvEstimatedProduce = findViewById(R.id.tvEstimatedProduce);
        TextView tvEstimatedIncome = findViewById(R.id.tvEstimatedIncome);
        TextView tvCrop = findViewById(R.id.tvCrop);

        if (tvCrop != null) tvCrop.setText(cropname);
        if (tvSelectedDate != null) tvSelectedDate.setText(plantingDate);
        if (tvHectares != null) tvHectares.setText(String.valueOf(area));
        if (tvEstimatedProduce != null) tvEstimatedProduce.setText(String.valueOf(estimatedProduce));
        if (tvEstimatedIncome != null) tvEstimatedIncome.setText(String.valueOf(estimatedIncome));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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
