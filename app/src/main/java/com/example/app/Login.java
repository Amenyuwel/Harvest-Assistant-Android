package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    TextInputEditText etPassword, etID;
    Button btnLogin;
    TextView tvBottomTextSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        etPassword = findViewById(R.id.etPassword);
        etID = findViewById(R.id.etID);
        btnLogin = findViewById(R.id.btnLogin);
        tvBottomTextSignup = findViewById(R.id.tvBottomTextSignup);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to HomeFragment
                Intent intent = new Intent(Login.this, Dashboard.class);
                startActivity(intent);
                finish();

                // Validate input fields
                //if (validateInputFields()) {
                // Perform login if validation passes
                //String username = etID.getText().toString().trim();
                //String password = etPassword.getText().toString().trim();
                //performLogin(username, password);
                //  }
            }
        });

        tvBottomTextSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Redirect to SignupActivity
                Intent intent = new Intent(Login.this, SignupActivity.class);
                startActivity(intent);
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
}
