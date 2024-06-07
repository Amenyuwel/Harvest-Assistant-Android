package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText etFirstName, etMiddleName, etLastName;
    private TextInputLayout firstNameEditText, middleNameEditText, lastNameEditText;
    private TextView signNext;
    private TextView tvBottomTextLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        etFirstName = findViewById(R.id.etFirstName);
        etMiddleName = findViewById(R.id.etMiddleName);
        etLastName = findViewById(R.id.etLastName);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.textInputLayout7); // Corrected ID
        signNext = findViewById(R.id.signNext); // Corrected to ImageView
        tvBottomTextLogin = findViewById(R.id.tvBottomTextLogin); // Find tvBottomTextLogin

        signNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform validation
                if (validateFields()) {
                    // All fields are valid, proceed to SignNext activity
                    Intent intent = new Intent(SignupActivity.this, SignNext.class);
                    startActivity(intent);
                    finish(); // Optional, if you don't want to keep this activity in the back stack
                } else {
                    // Validation failed, show error message
                    Toast.makeText(SignupActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvBottomTextLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add your login logic here
                // For demonstration, let's redirect to LoginActivity
                Intent intent = new Intent(SignupActivity.this, Login.class);
                startActivity(intent);
                finish(); // Optional, if you don't want to keep this activity in the back stack
            }
        });
    }

    private boolean validateFields() {
        // Get text from EditText fields
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();

        // Perform validation
        if (TextUtils.isEmpty(firstName)) {
            firstNameEditText.setError("First name is required");
            return false;
        } else if (!isString(firstName)) { // Check if the first name contains only letters
            firstNameEditText.setError("First name must contain only letters");
            return false;
        } else {
            firstNameEditText.setError(null);
        }

        if (TextUtils.isEmpty(lastName)) {
            lastNameEditText.setError("Last name is required");
            return false;
        } else if (!isString(lastName)) { // Check if the last name contains only letters
            lastNameEditText.setError("Last name must contain only letters");
            return false;
        } else {
            lastNameEditText.setError(null);
        }

        // Additional validation logic can be added here if needed

        return true;
    }

    private boolean isString(String input) {
        return input.matches("[a-zA-Z]+");
    }
}
