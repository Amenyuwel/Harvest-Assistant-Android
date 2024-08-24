package com.example.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
    TextView tvBottomTextSignup;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        etPassword = findViewById(R.id.etPassword);
        etID = findViewById(R.id.etID);
        btnLogin = findViewById(R.id.btnLogin);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }

    btnLogin.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i = new Intent(Login.this, Dashboard.class);
            startActivity(i);
        }
    });


        tvBottomTextSignup = findViewById(R.id.tvBottomTextSignup);

        Button loginButton = findViewById(R.id.btnLogin);

        tvBottomTextSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, SignupActivity.class);
            startActivity(i);}
        });
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
        String url = "https://harvest.dermocura.net/api/login.php";
        RequestQueue rq = Volley.newRequestQueue(Login.this);
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);

                        String message = jsonResponse.getString("message");
                        String token = jsonResponse.getString("remember_token");
                        Log.d("message:", message);

                        // Save token to SharedPreferences
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
                    if (error.networkResponse != null) {
                        int statusCode = error.networkResponse.statusCode;
                        Log.d("Error: ", "Status Code: " + statusCode);
                        if (statusCode == 403) {
                            Toast.makeText(this, "Forbidden: You do not have permission to access this resource.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Network error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("rsbsa_num", username.toString());
                params.put("password", password.toString());
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        rq.add(postRequest);
    }

    private void saveTokenToSharedPreferences(String token) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }

    // Method to retrieve the token from SharedPreferences
    private String getTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("token", "");
    }
}
