package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class SecondLogin extends AppCompatActivity {

    TextInputEditText etUsername;
    Button btnSet;
    String TAG = "SecondLogin";
    String URL = "https://harvest.dermocura.net/PHP_API/set_username.php";
    int farmerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TRANSPARENT STATUS BAR
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_second_login);

        etUsername = findViewById(R.id.etUsernameD);
        btnSet = findViewById(R.id.btnSet);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }

        // Get the farmerID passed from Login activity
        farmerID = getIntent().getIntExtra("farmerID", -1);

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate input field
                if (validateInputField()) {
                    String username = etUsername.getText().toString().trim();
                    makeHTTPRequest(username);
                }
            }
        });
    }

    private boolean validateInputField() {
        String username = etUsername.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Username is required");
            return false;
        }

        // Validation passed
        return true;
    }

    private void makeHTTPRequest(String username) {
        // Create a JSON object for the request body
        JSONObject requestBody = new JSONObject();

        // Create a Volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Populate the JSON request body
        try {
            requestBody.put("farmerID", farmerID);
            requestBody.put("username", username);
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

    private void onRequestSuccess(JSONObject response) {
        try {
            // Extract success status and message from the JSON response
            boolean success = response.getBoolean("success");
            String message = response.getString("message");

            if (success) {
                // Update successful
                Log.d(TAG + " onRequestSuccess", "Message Response: " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                // Redirect to Dashboard
                Intent intent = new Intent(SecondLogin.this, Dashboard.class);
                startActivity(intent);
                finish();
            } else {
                // Update failed
                Log.e(TAG + " onRequestSuccess", "Message Response: " + message);
                Toast.makeText(this, "Failed to set username. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e(TAG + " onRequestSuccess", String.valueOf(e));
            Log.e(TAG + " onRequestSuccess", "Error parsing JSON response");
        }
    }

    private void onRequestError(VolleyError error) {
        // Log and highlight entry
        Log.e(TAG + " onRequestError", "Error Response: " + error.getMessage());
    }
}
