package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class FormActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Button submitButton;
    private String selectedDate;
    private static final String API_URL = "http://192.168.5.108/HarvestAssistantFinalII/api/farmer_planted.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        calendarView = findViewById(R.id.calendarView);
        submitButton = findViewById(R.id.submitButton);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
        });

        submitButton.setOnClickListener(v -> {
            if (selectedDate != null && !selectedDate.isEmpty()) {
                // Send the selected date to the API endpoint
                sendSelectedDate(selectedDate);
            } else {
                Toast.makeText(FormActivity.this, "Please select a date", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendSelectedDate(String date) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, API_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the API response if needed
                        Toast.makeText(FormActivity.this, "Date sent successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Toast.makeText(FormActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Parameters to be sent to the API
                Map<String, String> params = new HashMap<>();
                params.put("datePlanted", date);
                return params;
            }
        };
        queue.add(request);
    }
}
