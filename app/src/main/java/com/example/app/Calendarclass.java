package com.example.app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    private ProgressDialog progressDialog;
    Button btnDone, btnReset;

    String TAG = "CalendarActivity";
    String URL = "https://harvest.dermocura.net/PHP_API/fetch_calendar.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.darker_matcha));
        setContentView(R.layout.calendar_activity);

        ivDate = findViewById(R.id.ivDate);
        calendarBackButton = findViewById(R.id.calendarBackButton);
        tvGrowthStage = findViewById(R.id.tvGrowthStage);
        tvStageDescription = findViewById(R.id.tvStageDescription);
        tvStageSuggestions = findViewById(R.id.tvStageSuggestions);
        btnDone = findViewById(R.id.btnDone);
        btnReset = findViewById(R.id.btnReset);

        // Open FormActivity for date selection
        ivDate.setOnClickListener(view -> {
            Intent i = new Intent(Calendarclass.this, FormActivity.class);
            startActivity(i);
        });

        // Go back to Dashboard
        calendarBackButton.setOnClickListener(view -> {
            Intent i = new Intent(Calendarclass.this, Dashboard.class);
            startActivity(i);
        });

        // Handle Done button press (submit harvest date)
        btnDone.setOnClickListener(view -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdf.format(Calendar.getInstance().getTime());

            int farmerID = SharedPreferenceManager.getInstance(this).getFarmerID();

            if (farmerID != -1) {
                postHarvestDate(farmerID, currentDate); // Sending the selected harvest date without crop_id
            } else {
                Toast.makeText(this, "Farmer ID not found", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle Reset button press (reset harvest data)
        btnReset.setOnClickListener(view -> {
            int farmerID = SharedPreferenceManager.getInstance(this).getFarmerID();
            if (farmerID != -1) {
                resetHarvestData(farmerID);  // Call the reset function
            } else {
                Toast.makeText(this, "Farmer ID not found", Toast.LENGTH_SHORT).show();
            }
        });

        // Fetch the data on activity load
        int farmerID = SharedPreferenceManager.getInstance(this).getFarmerID();
        if (farmerID != -1) {
            makeHTTPRequest(farmerID);
        } else {
            Toast.makeText(this, "Farmer ID not found. Please log in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeHTTPRequest(int farmerID) {
        JSONObject requestBody = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(this);

        try {
            requestBody.put("farmer_id", farmerID);
        } catch (JSONException e) {
            Log.e(TAG, "makeHTTPRequest: ", e);
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                requestBody,
                this::onRequestSuccess,
                this::onRequestError
        );
        queue.add(request);
    }

    private void postHarvestDate(int farmerID, String dateHarvested) {
        String postURL = "https://harvest.dermocura.net/PHP_API/update_harvest.php";
        JSONObject requestBody = new JSONObject();

        try {
            // Prepare the JSONObject with farmer_id and date_harvested
            requestBody.put("farmer_id", farmerID);
            requestBody.put("date_harvested", dateHarvested); // Ensure this is in 'yyyy-MM-dd' format
        } catch (JSONException e) {
            Log.e(TAG, "postHarvestDate: Error creating JSON body", e);
            return;
        }

        // Create a JsonObjectRequest to handle the POST request
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                postURL,
                requestBody,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Toast.makeText(Calendarclass.this, "Harvest completed!", Toast.LENGTH_SHORT).show();
                            recreate();  // This will refresh the entire activity
                        } else {
                            String message = response.optString("message", "Failed to update harvest");
                            Toast.makeText(Calendarclass.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "postHarvestDate: Error parsing response", e);
                    }
                },
                error -> {
                    Log.e(TAG, "postHarvestDate: Volley error", error);
                    Toast.makeText(Calendarclass.this, "Failed to update harvest date", Toast.LENGTH_SHORT).show();
                }
        );

        // Add the request to the Volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    // Function to reset harvest data (set is_active to 0)
    private void resetHarvestData(int farmerID) {
        String resetURL = "https://harvest.dermocura.net/PHP_API/reset_harvest.php"; // Update with the correct URL
        JSONObject requestBody = new JSONObject();

        try {
            requestBody.put("farmer_id", farmerID);
        } catch (JSONException e) {
            Log.e(TAG, "resetHarvestData: Error creating JSON body", e);
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                resetURL,
                requestBody,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            Toast.makeText(Calendarclass.this, "Harvest data reset successfully!", Toast.LENGTH_SHORT).show();
                            recreate();  // Refresh the activity
                        } else {
                            String message = response.optString("message", "Failed to reset harvest data");
                            Toast.makeText(Calendarclass.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "resetHarvestData: Error parsing response", e);
                    }
                },
                error -> {
                    Log.e(TAG, "resetHarvestData: Volley error", error);
                    Toast.makeText(Calendarclass.this, "Failed to reset harvest data", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void onRequestSuccess(JSONObject response) {
        try {
            if (response.getBoolean("success")) {
                JSONObject data = response.getJSONObject("data");

                double area = data.getDouble("area");
                String datePlanted = data.getString("date_planted");
                double estimatedProduce = data.getDouble("estimated_produce");
                double estimatedIncome = data.getDouble("estimated_income");
                String cropName = data.getString("crop_name");

                updateUI(datePlanted, area, estimatedProduce, estimatedIncome, cropName);

                ivDate.setVisibility(View.GONE);
            } else {
                String message = response.optString("message", "Unknown error");
                Log.e(TAG, "onRequestSuccess: Error - " + message);

                ivDate.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            Log.e(TAG, "onRequestSuccess: JSON Parsing error", e);
        }
    }

    private void onRequestError(VolleyError error) {
        Log.e(TAG, "onRequestError: " + error.getMessage());
    }

    private void updateUI(String plantingDate, double area, double estimatedProduce, double estimatedIncome, String cropname) {
        TextView tvSelectedDate = findViewById(R.id.tvSelectedDate);
        TextView tvHectares = findViewById(R.id.tvHectares);
        TextView tvEstimatedProduce = findViewById(R.id.tvEstimatedProduce);
        TextView tvEstimatedIncome = findViewById(R.id.tvEstimatedIncome);
        TextView tvCrop = findViewById(R.id.tvCrop);

        tvCrop.setText(cropname);
        tvSelectedDate.setText(plantingDate);
        tvHectares.setText(String.valueOf(area));
        tvEstimatedProduce.setText(String.valueOf(estimatedProduce));
        tvEstimatedIncome.setText(String.valueOf(estimatedIncome));
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
