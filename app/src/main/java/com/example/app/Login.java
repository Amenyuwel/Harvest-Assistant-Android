package com.example.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    TextInputEditText etPassword, etID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        etPassword = findViewById(R.id.etPassword);
        etID = findViewById(R.id.etID);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }

        Button loginButton = findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate input fields
                if (validateInputFields()) {
                    // Perform login if validation passes
                    String username = etID.getText().toString().trim();
                    String password = etPassword.getText().toString().trim();
                    performLogin(username, password);
                }
            }
        });
    }

    private boolean validateInputFields() {
        String username = etID.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            etID.setError("Username is required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return false;
        }

        // Validation passed
        return true;
    }

    private void performLogin(String username, String password) {
        String url = "http://192.168.5.108/HarvestAssistantFinalII/api/login.php"; // Replace with your actual API URL
        RequestQueue rq = Volley.newRequestQueue(Login.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        String message = jsonResponse.getString("message");
                        String token = jsonResponse.getString("remember_token");

                        // Save the token to SharedPreferences
                        saveTokenToSharedPreferences(token);

                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, Dashboard.class);
                        startActivity(intent);
                        finish(); // Finish the login activity

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                    }

                },
                error -> {
                    Toast.makeText(this, "Wrong username or password", Toast.LENGTH_SHORT).show();
                    Log.d("Error: ", error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("rsbsa_num", username);
                params.put("password", password);
                return params;
            }
        };


        rq.add(postRequest);
    }

    // Method to save the token to SharedPreferences
    private void saveTokenToSharedPreferences(String token) {
        // Get SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Get SharedPreferences editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Store the token using a unique key
        editor.putString("token", token);

        // Apply changes
        editor.apply();
    }

    // Method to retrieve the token from SharedPreferences
    private String getTokenFromSharedPreferences() {
        // Get SharedPreferences instance
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Retrieve the token using the unique key
        return sharedPreferences.getString("token", "");
    }
}