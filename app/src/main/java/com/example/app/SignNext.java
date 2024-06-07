package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignNext extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_next);

        // Initialize views
        Button signupButton = findViewById(R.id.btnSignup);
        TextView tvLogin = findViewById(R.id.tvLogin); // Changed to tvLogin

        // Set onClickListener for Signup Button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform validation
                if (validateFields()) {
                    // All fields are valid, proceed with signup
                    Intent intent = new Intent(SignNext.this, CityValid.class);
                    startActivity(intent);
                    finish(); // Optional, if you don't want to keep this activity in the back stack
                } else {
                    // Validation failed, show error message
                    Toast.makeText(SignNext.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set onClickListener for Login TextView
        tvLogin.setOnClickListener(new View.OnClickListener() { // Changed to tvLogin
            @Override
            public void onClick(View v) {
                // Add your login logic here
                // For demonstration, let's redirect to LoginActivity
                Intent intent = new Intent(SignNext.this, Login.class);
                startActivity(intent);
                finish(); // Optional, if you don't want to keep this activity in the back stack
            }
        });
    }

    private boolean validateFields() {
        // Get references to EditText fields
        TextView etCrop = findViewById(R.id.etCrop);
        TextView etContact = findViewById(R.id.etContact);
        TextView etPassword = findViewById(R.id.etPassword);
        TextView etConfirmPassword = findViewById(R.id.etConfirmPassword);

        // Get the text from EditText fields
        String crop = etCrop.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Check if any field is empty
        if (TextUtils.isEmpty(crop) || TextUtils.isEmpty(contact) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            return false;
        }

        // Check if crop contains only letters
        if (!isString(crop)) {
            etCrop.setError("Crop must contain only letters");
            return false;
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            // Passwords don't match, show error message
            etConfirmPassword.setError("Passwords don't match");
            return false;
        }

        // Clear any previous error on confirm password field
        etConfirmPassword.setError(null);

        return true;
    }

    private boolean isString(String input) {
        return input.matches("[a-zA-Z]+");
    }
}
