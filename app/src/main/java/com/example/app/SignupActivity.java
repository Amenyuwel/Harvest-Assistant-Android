package com.example.app;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText etFirstName, etMiddleName, etLastName, etContact, etPassword, etConfirmPassword, etArea;
    private Spinner cropSpinner, brgySpinner;
    private Button signupButton;
    private TextView tvBottomTextLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.darker_matcha));
        setContentView(R.layout.activity_signup);

        // Set the ActionBar background color
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.darker_matcha)));
            actionBar.setTitle("");
        }

        // Initialize inputs and Spinners
        etFirstName = findViewById(R.id.etFirstName);
        etMiddleName = findViewById(R.id.etMiddleName);
        etLastName = findViewById(R.id.etLastName);
        etContact = findViewById(R.id.etContact);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etArea = findViewById(R.id.etArea);
        cropSpinner = findViewById(R.id.cropSpinner);
        brgySpinner = findViewById(R.id.brgySpinner);
        signupButton = findViewById(R.id.btnSignup);
        tvBottomTextLogin = findViewById(R.id.tvBottomTextLogin);

        // Set up cropSpinner with array resource
        ArrayAdapter<CharSequence> cropAdapter = ArrayAdapter.createFromResource(this,
                R.array.crop_array, android.R.layout.simple_spinner_item);
        cropAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cropSpinner.setAdapter(cropAdapter);

        // Set up brgySpinner with array resource
        ArrayAdapter<CharSequence> brgyAdapter = ArrayAdapter.createFromResource(this,
                R.array.brgy_array, android.R.layout.simple_spinner_item);
        brgyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brgySpinner.setAdapter(brgyAdapter);

        // Set the onClickListener for the signup button
        signupButton.setOnClickListener(v -> {
            // Validate form and collect data
            String firstName = etFirstName.getText().toString().trim();
            String middleName = etMiddleName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String contactNumber = etContact.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String selectedCrop = cropSpinner.getSelectedItem().toString();
            String selectedBrgy = brgySpinner.getSelectedItem().toString();
            String areaString = etArea.getText().toString().trim();

            // Check for required fields
            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(contactNumber) ||
                    TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(areaString)) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check password confirmation
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // Parse area as double
            double area;
            try {
                area = Double.parseDouble(areaString);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid area value", Toast.LENGTH_SHORT).show();
                return;
            }

            // Map crop name and barangay to their respective IDs
            int cropId = getCropId(selectedCrop);
            int brgyId = getBrgyId(selectedBrgy);

            // Assuming farmerId is retrieved or generated previously, e.g., from SharedPreferences
            int farmerId = 1;

            // Schedule planting date via API
            schedulePlantingDate(farmerId, cropId, area);
        });
    }

    private int getCropId(String cropName) {
        switch (cropName.toLowerCase()) {
            case "rice":
                return 1;
            case "corn":
                return 2;
            default:
                return -1;
        }
    }

    private int getBrgyId(String brgyName) {
        switch (brgyName.toLowerCase()) {
            case "lagao":
                return 1;
            case "san isidro":
                return 2;
            case "bula":
                return 3;
            default:
                return -1;
        }
    }

    private void schedulePlantingDate(int farmerId, int cropId, double area) {
        // dri butang ang pag plant bill
    }
}
