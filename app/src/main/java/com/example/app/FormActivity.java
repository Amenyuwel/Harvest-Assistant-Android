package com.example.app;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class FormActivity extends AppCompatActivity {

    private static final String TAG = "FormActivity";
    private CalendarView calendarView;
    private RequestQueue requestQueue;
    private ImageButton pickDateButton;

    private int farmerId = 1;  // Placeholder - make sure you retrieve this dynamically
    private int cropId = 1;    // Placeholder - make sure you retrieve this dynamically

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form); // Ensure this matches your XML layout file name

        // Initialize views
        calendarView = findViewById(R.id.calendarView);
        pickDateButton = findViewById(R.id.pickDateButton);

        // Initialize the request queue for network operations
        requestQueue = Volley.newRequestQueue(this);

        // Get the current date using java.util.Calendar
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Initialize DatePickerDialog to let user pick a planting date
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            // Format the selected date as needed (e.g., "yyyy-MM-dd")
            String selectedDate = year1 + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

            // Call the function to schedule the planting date
            schedulePlantingDate(farmerId, cropId, selectedDate);
        }, year, month, day);

        // Show the DatePickerDialog when the user clicks on the CalendarView or a button
        calendarView.setOnDateChangeListener((view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            datePickerDialog.updateDate(selectedYear, selectedMonth, selectedDayOfMonth);
            datePickerDialog.show();
        });

        // Alternatively, if you have a button to trigger DatePickerDialog
        pickDateButton = findViewById(R.id.pickDateButton);
        pickDateButton.setOnClickListener(v -> datePickerDialog.show());
    }

    // Function to schedule planting date
    private void schedulePlantingDate(int farmerId, int cropId, String datePlanted) {
        String url = "https://harvest.dermocura.net/PHP_API/calendar.php"; // Replace with your server URL

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("farmer_id", farmerId);
            jsonBody.put("crop_id", cropId);
            jsonBody.put("planting_date", datePlanted);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                response -> {
                    try {
                        boolean success = response.getBoolean("success");
                        String message = response.getString("message");
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                        if (success) {
                            // Handle successful planting date scheduling (e.g., navigate to a different screen)
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                });

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }
}
