package com.example.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
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
    private EditText areaEditText;
    private Spinner cropSpinner;
    private Button pickDateButton;
    private RequestQueue requestQueue;

    // Store the selected date
    private String selectedDate;

    // SharedPreferences to store and retrieve farmer_id
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // Initialize views
        calendarView = findViewById(R.id.calendarView);
        areaEditText = findViewById(R.id.areaEditText);
        cropSpinner = findViewById(R.id.cropSpinner);
        pickDateButton = findViewById(R.id.pickDateButton);

        // Initialize the request queue for network operations
        requestQueue = Volley.newRequestQueue(this);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Get farmerId from SharedPreferences (assuming it's stored during login)
        int farmerId = sharedPreferences.getInt("farmer_id", - 1);

        // Populate the spinner with crop options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.crop_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cropSpinner.setAdapter(adapter);

        // Set a listener on the CalendarView
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Format the selected date as needed (e.g., "yyyy-MM-dd")
            String formattedMonth = String.format("%02d", month + 1);
            String formattedDay = String.format("%02d", dayOfMonth);
            selectedDate = year + "-" + formattedMonth + "-" + formattedDay;
        });

        // Set up the button click listener
        pickDateButton.setOnClickListener(v -> {
            String area = areaEditText.getText().toString().trim();
            String selectedCrop = cropSpinner.getSelectedItem().toString();
            int cropId = getCropId(selectedCrop);

            Log.i("PondsTambok", "area" + area);
            Log.i("PondsTambok", "selectedCrop" + selectedCrop);
            Log.i("PondsTambok", "cropId" + cropId);
            Log.i("PondsTambok", "farmerId" + selectedDate);

            schedulePlantingDate(farmerId, cropId, Double.parseDouble(area), selectedDate);
        });
    }

    // Function to schedule planting date
//    private void schedulePlantingDate(int farmerId, int cropId, double area, String datePlanted) {
//        String url = "https://harvest.dermocura.net/PHP_API/calendar.php";  // Replace with your server URL
//
//        // Create JSON object for API request
//        JSONObject jsonBody = new JSONObject();
//        try {
//            jsonBody.put("farmer_id", farmerId);
//            jsonBody.put("crop_id", cropId);
//            jsonBody.put("area", area);
//            jsonBody.put("planting_date", datePlanted);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
//                response -> {
//                    try {
//                        boolean success = response.getBoolean("success");
//                        String message = response.getString("message");
//                        Log.e("APIResponse", "Success:" + success + ", Message:" + message);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                },
//                error -> {
//                    // Handle error
//                    Log.e("APIError", "Error: " + error.toString());
//                }
//        );
//
//        // Add the request to the RequestQueue
//        requestQueue.add(request);
//    }

    private void schedulePlantingDate(int farmerId, int cropId, double area, String datePlanted) {
        // Define keys for the JSON request body
        String url = "https://harvest.dermocura.net/PHP_API/calendar.php";

        // Create a JSON object for the request body
        JSONObject requestBody = new JSONObject();

        // Create a Volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);

//        String datedate = datePlanted.toString();
//        int farmer_id = 1;

        // Populate the JSON request body
        try {
            requestBody.put("farmer_id", farmerId);
            requestBody.put("crop_id", cropId);
            requestBody.put("area", area);
            requestBody.put("planting_date", datePlanted);
        } catch (JSONException e) {
            Log.e("EmmanBayot" + " makeHTTPRequest", String.valueOf(e));
            return;
        }

        // Create a JsonObjectRequest for a POST request to the specified URL
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                this::onRequestSuccess,
                this::onRequestError
        );

        // Log the JSON request body for debugging
        String stringJSON = requestBody.toString();
        Log.i("EmmanBayot" + " makeHTTPRequest", stringJSON);

        // Add the request to the Volley request queue
        queue.add(request);
    }

    private void onRequestSuccess(JSONObject response) {
        try {
            // Extract success status and message from the JSON response
            boolean success = response.getBoolean("success");
            String message = response.getString("message");

            if (success) {
                // Login successful
                Log.d("EmmanBayot" + " onRequestSuccess", "Message Response: " + message);
                Log.d("EmmanBayot" + " onRequestSuccess", "JSON Received: " + response);
            } else {
                // Login failed
                Log.e("EmmanBayot" + " onRequestSuccess", "Message Response: " + message);
            }
        } catch (JSONException e) {
            Log.e("EmmanBayot" + " onRequestSuccess", String.valueOf(e));
            Log.e("EmmanBayot" + " onRequestSuccess", "Error parsing JSON response");
        }
    }

    private void onRequestError(VolleyError error) {
        // Log and highlight entry
        Log.e("EmmanBayot" + " onRequestError", "Error Response: " + error.getMessage());
    }

    // Helper method to map crop names to crop IDs
    private int getCropId(String cropName) {
        switch (cropName.toLowerCase()) {
            case "rice":
                return 1;
            case "corn":
                return 2;
            default:
                return -1;  // Unknown crop
        }
    }
}
