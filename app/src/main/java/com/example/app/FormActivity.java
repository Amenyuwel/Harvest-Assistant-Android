package com.example.app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class FormActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Button pickDateButton;
    private RequestQueue requestQueue;

    private int farmerId = 1;  // Placeholder - Retrieve dynamically
    private int cropId = 1;    // Placeholder - Retrieve dynamically

    // Store the selected date
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // Initialize views
        calendarView = findViewById(R.id.calendarView);
        pickDateButton = findViewById(R.id.pickDateButton);

        // Initialize the request queue for network operations
        requestQueue = Volley.newRequestQueue(this);

        // Set a listener on the CalendarView
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Format the selected date as needed (e.g., "yyyy-MM-dd")
            String formattedMonth = String.format("%02d", month + 1);
            String formattedDay = String.format("%02d", dayOfMonth);
            selectedDate = year + "-" + formattedMonth + "-" + formattedDay;
        });

        // Set up the button click listener
        pickDateButton.setOnClickListener(v -> {
            if (selectedDate != null) {
                // Call the function to schedule the planting date
                Log.i("CalendarniBilly", selectedDate);
                schedulePlantingDate(farmerId, cropId, selectedDate);
            } else {
                // No date was selected
                Toast.makeText(FormActivity.this, "Please select a date", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Function to schedule planting date
    private void schedulePlantingDate(int farmerId, int cropId, String datePlanted) {
        String url = "https://harvest.dermocura.net/PHP_API/calendar.php";  // Replace with your server URL

        // Create JSON object for API request
        JSONObject jsonBody = new JSONObject();
        String farmer_id = String.valueOf(farmerId);
        String crop_id = String.valueOf(cropId);

        Log.i("BillyBayot", "Sent to PHP " + farmer_id);
        Log.i("BillyBayot", "Sent to PHP " + crop_id);
        Log.i("BillyBayot", "Sent to PHP " + datePlanted);

        try {
            jsonBody.put("farmer_id", farmerId);
            jsonBody.put("crop_id", cropId);
            jsonBody.put("planting_date", datePlanted);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    Log.d("APIResponse", response.toString());
                    try {
                        boolean success = response.getBoolean("success");
                        String message = response.getString("message");
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        Log.e("BillyBayot", "Error " + message);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                    Log.e("APIError", "Error: " + error.toString());
                    if (error.networkResponse != null) {
                        String errorResponse = new String(error.networkResponse.data);
                        Log.e("APIErrorResponse", "Error response: " + errorResponse);
                    }
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("BillyBayot", "Error " + error.getMessage());
                }
        );

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }
}
