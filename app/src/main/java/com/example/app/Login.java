package com.example.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
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

public class Login extends AppCompatActivity {

    TextInputEditText etPassword, etID;
    TextView btnSignup;
    Button btnLogin;
    String TAG = "Login";
    String URL = "https://harvest.dermocura.net/PHP_API/login.php";
    int sessionKey = 0;  // This can be dynamically updated after fetching from server if required

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TRANSPARENT STATUS BAR
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.login_activity);

        etPassword = findViewById(R.id.etPassword);
        etID = findViewById(R.id.etID);
        btnLogin = findViewById(R.id.btnLogin);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }

        btnSignup = findViewById(R.id.btnSignup);

        Button loginButton = findViewById(R.id.btnLogin);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, SignupActivity.class);
                startActivity(i);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate input fields based on sessionKey
                if (validateInputFields(sessionKey)) {
                    String loginValue = etID.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    makeHTTPRequest(loginValue, password, sessionKey);
                }
            }
        });
    }

    private boolean validateInputFields(int sessionKey) {
        String loginValue = etID.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Validate loginValue based on sessionKey (either rsbsa_num or username)
        if (TextUtils.isEmpty(loginValue)) {
            if (sessionKey == 0) {
                etID.setError("RSBSA number is required");
            } else {
                etID.setError("Username is required");
            }
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return false;
        }

        // Validation passed
        return true;
    }

    private void makeHTTPRequest(String loginValue, String password, int sessionKey) {
        // Define keys for the JSON request body
        String keyEmail = (sessionKey == 0) ? "rsbsa_num" : "username"; // Use rsbsa_num if sessionKey == 0
        String keyPassword = "password";

        // Create a JSON object for the request body
        JSONObject requestBody = new JSONObject();

        // Create a Volley request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Populate the JSON request body
        try {
            requestBody.put(keyEmail, loginValue); // Use rsbsa_num or username based on sessionKey
            requestBody.put(keyPassword, password);
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
                // Get the farmer data from the userData
                JSONObject userData = response.getJSONObject("userData");
                int farmerID = userData.getInt("farmerID");
                int sessionKey = userData.getInt("session_key");

                // Store farmerID in SharedPreferences
                SharedPreferenceManager.getInstance(this).saveFarmerID(farmerID);

                // Login successful
                Log.d(TAG + " onRequestSuccess", "Message Response: " + message);
                Log.d(TAG + " onRequestSuccess", "JSON Received: " + response);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                if (sessionKey == 0) {
                    // Redirect to SecondLogin to set username and update session key
                    Intent intent = new Intent(Login.this, SecondLogin.class);
                    intent.putExtra("farmerID", farmerID);
                    intent.putExtra("rsbsa_num", etID.getText().toString().trim()); // pass RSBSA number
                    startActivity(intent);
                    finish();
                } else if (sessionKey == 1) {
                    // Redirect to Dashboard
                    Intent intent = new Intent(Login.this, Dashboard.class);
                    startActivity(intent);
                    finish();
                }

            } else {
                // Login failed
                Log.e(TAG + " onRequestSuccess", "Message Response: " + message);
                Toast.makeText(this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
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

    // Method to retrieve the token from SharedPreferences
    private String getTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }
}
