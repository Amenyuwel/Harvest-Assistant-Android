package com.example.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassCV extends AppCompatActivity {

    private TextInputEditText currentPassword, newPassword, confirmPassword;
    private Button applyButton, cancelButton;
    private static final String TAG = "Eyyy"; // Log tag for this class
    private static final String URL = "https://harvest.dermocura.net/PHP_API/changepassword.php"; // Your server URL

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_passwordcv); // Replace with your layout name

        // Initialize views
        currentPassword = findViewById(R.id.currentPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        applyButton = findViewById(R.id.applyButton); // Make sure to set IDs for buttons in your XML
        cancelButton = findViewById(R.id.cancelButton);

        Log.d(TAG, "UI components initialized");

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Apply button clicked");
                changePassword();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Cancel button clicked");
                finish(); // Close activity if user presses Cancel
            }
        });
    }

    private void changePassword() {
        String currentPwd = currentPassword.getText().toString().trim();
        String newPwd = newPassword.getText().toString().trim();
        String confirmPwd = confirmPassword.getText().toString().trim();

        // Retrieve farmerID from SharedPreferenceManager
        SharedPreferenceManager spManager = SharedPreferenceManager.getInstance(this);
        int farmerID = spManager.getFarmerID();

        // Log the fetched farmerID for debugging
        Log.d(TAG, "Fetched farmerID: " + farmerID);

        // Check if farmerID is valid
        if (farmerID == -1) {
            Toast.makeText(ChangePassCV.this, "Farmer ID not found. Please log in.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Farmer ID is missing in SharedPreferences");
            return;
        }

        // Check if fields are empty
        if (TextUtils.isEmpty(currentPwd)) {
            currentPassword.setError("Current password is required");
            Log.e(TAG, "Current password is empty");
            return;
        }

        if (TextUtils.isEmpty(newPwd)) {
            newPassword.setError("New password is required");
            Log.e(TAG, "New password is empty");
            return;
        }

        if (!newPwd.equals(confirmPwd)) {
            confirmPassword.setError("Passwords do not match");
            Log.e(TAG, "Passwords do not match");
            return;
        }

        // Log the new password to ensure it's not empty
        Log.d(TAG, "New password: " + newPwd);

        // Make the HTTP request
        makeHTTPRequest(farmerID, newPwd);
    }

    private void makeHTTPRequest(int farmerID, String newPassword) {
        // Define keys for the JSON request body
        String keyFarmerID = "farmer_id";
        String keyNewPassword = "newPassword";

        // Create a JSON object for the request body
        JSONObject requestBody = new JSONObject();

        // Create a Volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Populate the JSON request body
        try {
            requestBody.put(keyFarmerID, farmerID);
            requestBody.put(keyNewPassword, newPassword);
        } catch (JSONException e) {
            Log.e(TAG + " makeHTTPRequest", String.valueOf(e));
            return;
        }

        // Log the JSON request body for debugging
        String stringJSON = requestBody.toString();
        Log.i(TAG + " makeHTTPRequest", stringJSON);

        // Create a JsonObjectRequest for a POST request to the specified URL
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                URL,
                requestBody,
                response -> {
                    try {
                        // Handle server response
                        boolean success = response.getBoolean("success");
                        if (success) {
                            Log.d(TAG, "Password updated successfully");
                            Toast.makeText(ChangePassCV.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Close the activity after success
                        } else {
                            String errorMessage = response.getString("message");
                            Log.e(TAG, "Error updating password: " + errorMessage);
                            Toast.makeText(ChangePassCV.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON parsing error: " + e.getMessage());
                        e.printStackTrace();
                        Toast.makeText(ChangePassCV.this, "An error occurred while processing the response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Handle error
                    Log.e(TAG, "Error in HTTP request: " + error.getMessage());
                    Toast.makeText(ChangePassCV.this, "An error occurred while updating the password", Toast.LENGTH_SHORT).show();
                }
        );

        // Add the request to the Volley request queue
        queue.add(request);
    }
}
