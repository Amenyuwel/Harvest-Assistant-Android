package com.example.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

public class FormActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private EditText areaEditText;
    private Spinner cropSpinner;
    private Button pickDateButton;
    private Button formBackButton;
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
        formBackButton = findViewById(R.id.formBackButton);

        formBackButton.setOnClickListener(view -> {
            Intent i = new Intent(FormActivity.this, Calendarclass.class);
            startActivity(i);
        });

        // Initialize the request queue for network operations
        requestQueue = Volley.newRequestQueue(this);

        // Initialize SharedPreferences
        // Get farmerId from SharedPreferences (assuming it's stored during login)
        int farmerID = SharedPreferenceManager.getInstance(this).getFarmerID();
        Log.i("ZhakBayot", "farmerID from sharedpref: " + farmerID);

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

            Log.i("pickDate", "area" + area);
            Log.i("pickDate", "selectedCrop" + selectedCrop);
            Log.i("pickDate", "cropId" + cropId);
            Log.i("pickDate", "farmerId" + selectedDate);

            // Schedule the planting date with the backend
            schedulePlantingDate(farmerID, cropId, Double.parseDouble(area), selectedDate);

            // Once the button is clicked and the data is sent, transition to Calendarclass
            Intent intent = new Intent(FormActivity.this, Calendarclass.class);
            startActivity(intent);
        });
    }

    private void schedulePlantingDate(int farmerId, int cropId, double area, String datePlanted) {
        // Define keys for the JSON request body
        String url = "https://harvest.dermocura.net/PHP_API/calendarbayot.php";

        // Create a JSON object for the request body
        JSONObject requestBody = new JSONObject();

        // Create a Volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        // log input
        Log.i("phpLog", "FarmerID:" + farmerId);
        Log.i("phpLog", "crop_id:" + cropId);
        Log.i("phpLog", "area:" + area);
        Log.i("phpLog", "planting_date:" + datePlanted);

        // Populate the JSON request body
        try {
            requestBody.put("farmer_id", farmerId);
            requestBody.put("crop_id", cropId);
            requestBody.put("area", area);
            requestBody.put("planting_date", datePlanted);
        } catch (JSONException e) {
            Log.e("request" + " makeHTTPRequest", String.valueOf(e));
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
        Log.i("request" + " makeHTTPRequest", stringJSON);

        // Add the request to the Volley request queue
        queue.add(request);
    }

    private void onRequestSuccess(JSONObject response) {
        try {
            // Extract success status and message from the JSON response
            boolean success = response.getBoolean("success");
            String message = response.getString("message");

            if (success) {
                // Log successful response
                Log.d("request" + " onRequestSuccess", "Message Response: " + message);
                Log.d("request" + " onRequestSuccess", "JSON Received: " + response);
            } else {
                // Log failure
                Log.e("request" + " onRequestSuccess", "Message Response: " + message);
            }
        } catch (JSONException e) {
            Log.e("request" + " onRequestSuccess", String.valueOf(e));
            Log.e("request" + " onRequestSuccess", "Error parsing JSON response");
        }
    }

    private void onRequestError(VolleyError error) {
        // Log and highlight error
        Log.e("request" + " onRequestError", "Error Response: " + error.getMessage());
    }

    // Helper method to map crop names to crop IDs
    private int getCropId(String cropName) {
        switch (cropName.toLowerCase()) {
            case "corn":
                return 1;
            case "rice":
                return 2;
            default:
                return -1;  // Unknown crop
        }
    }
}
