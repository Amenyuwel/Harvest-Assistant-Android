package com.example.app;

import android.content.Intent;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormActivity extends AppCompatActivity {

    private static final String TAG = "FormActivity";
    private static final String VARIANCE_API_URL = "http://192.168.5.108/HarvestAssistantFinalII/api/display_variance.php";
    private CalendarView calendarView;
    private Spinner spinnerCrops, spinnerVariants;
    private EditText editTextHectares;
    private Button submitButton;
    private String selectedDate;
    private static final double PRODUCE_PER_HECTARE = 5; // in tons
    private static final double INCOME_PER_KILOGRAM = 1800; // in PHP (per kilogram)
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form); // Ensure this matches your XML layout file name

        // Initialize views
        calendarView = findViewById(R.id.calendarView);
        spinnerCrops = findViewById(R.id.spinnerCrops);
        spinnerVariants = findViewById(R.id.spinnerVariants);
        editTextHectares = findViewById(R.id.editTextHectares);
        submitButton = findViewById(R.id.submitButton);

        // Initialize the RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Populate spinnerCrops
        ArrayAdapter<CharSequence> cropsAdapter = ArrayAdapter.createFromResource(this,
                R.array.crops_array, android.R.layout.simple_spinner_item);
        cropsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCrops.setAdapter(cropsAdapter);

        // Fetch and populate spinnerVariants
        ArrayAdapter<CharSequence> variantsAdapter = ArrayAdapter.createFromResource(this,
                R.array.variance_array, android.R.layout.simple_spinner_item);
        cropsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVariants.setAdapter(variantsAdapter);

        // Set CalendarView date change listener
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
        });

        // Set submit button click listener
        submitButton.setOnClickListener(v -> {
            try {
                String crop = spinnerCrops.getSelectedItem().toString();
                String variant = spinnerVariants.getSelectedItem().toString();
                String hectaresStr = editTextHectares.getText().toString();
                double hectares = Double.parseDouble(hectaresStr);

                // Calculate the estimated produce in tons
                double estimatedProduceTons = hectares * PRODUCE_PER_HECTARE;

                // Convert tons to kilograms (1 ton = 1000 kg)
                double estimatedProduceKilograms = estimatedProduceTons * 1000;

                // Divide kilograms by 50 to get the number of sacks
                double estimatedSacks = estimatedProduceKilograms / 50;

                // Calculate estimated income (sacks * 1800)
                double estimatedIncome = estimatedSacks * INCOME_PER_KILOGRAM;

                Intent intent = new Intent(FormActivity.this, Calendarclass.class);
                intent.putExtra("selectedDate", selectedDate);
                intent.putExtra("crop", crop);
                intent.putExtra("variant", variant);
                intent.putExtra("hectares", hectaresStr);
                intent.putExtra("estimatedProduce", estimatedProduceTons);
                intent.putExtra("estimatedIncome", estimatedIncome);
                startActivity(intent);
            } catch (NumberFormatException e) {
                Toast.makeText(FormActivity.this, "Please enter a valid number for hectares.", Toast.LENGTH_SHORT).show();
            }
        });
    }
//
//    private void fetchVarianceData() {
//        StringRequest request = new StringRequest(Request.Method.GET, VARIANCE_API_URL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            // Check if the response is a JSON object (error message) or JSON array (variance data)
//                            JSONObject jsonResponse = new JSONObject(response);
//
//                            // Check for an error message
//                            if (jsonResponse.has("message")) {
//                                String message = jsonResponse.getString("message");
//                                Toast.makeText(FormActivity.this, message, Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//
//                            // If no error message, assume it's the expected JSON array
//                            JSONArray jsonArray = jsonResponse.getJSONArray("data");
//                            List<String> varianceList = new ArrayList<>();
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                varianceList.add(jsonArray.getString(i));
//                            }
//
//                            // Populate the Spinner with the variance data
//                            ArrayAdapter<String> adapter = new ArrayAdapter<>(FormActivity.this, android.R.layout.simple_spinner_item, varianceList);
//                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            spinnerVariants.setAdapter(adapter);
//                        } catch (JSONException e) {
//                            Log.e(TAG, "Error parsing variance data", e);
//                            Toast.makeText(FormActivity.this, "Error parsing variance data", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.e(TAG, "Error fetching variance data", error);
//                        Toast.makeText(FormActivity.this, "Error fetching variance data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                String token = getTokenFromSharedPreferences();
//                headers.put("Authorization", "Bearer " + token);
//                return headers;
//            }
//        };
//        requestQueue.add(request);
//    }
//
//    private String getTokenFromSharedPreferences() {
//        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//        return sharedPreferences.getString("token", "");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (requestQueue != null) {
//            requestQueue.stop();
//        }
    }

