package com.example.app;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText etFirstName, etMiddleName, etLastName, etContact, etPassword, etConfirmPassword;
    private TextInputLayout firstNameEditText, middleNameEditText, lastNameEditText, contactLayout, passwordLayout, confirmPasswordLayout;
    private Spinner cropSpinner, brgySpinner;
    private TextView tvBottomTextLogin;
    private Button signupButton;

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
        firstNameInput = findViewById(R.id.etFirstName);
        middleNameInput = findViewById(R.id.etMiddleName);
        lastNameInput = findViewById(R.id.etLastName);
        contactNumberInput = findViewById(R.id.etContact);
        passwordInput = findViewById(R.id.etPassword);
        confirmPasswordInput = findViewById(R.id.etConfirmPassword);
        areaInput = findViewById(R.id.etArea);
        cropSpinner = findViewById(R.id.cropSpinner);
        brgySpinner = findViewById(R.id.brgySpinner);
        signupButton = findViewById(R.id.btnSignup);

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
            String firstName = firstNameInput.getText().toString().trim();
            String middleName = middleNameInput.getText().toString().trim();
            String lastName = lastNameInput.getText().toString().trim();
            String contactNumber = contactNumberInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();
            String selectedCrop = cropSpinner.getSelectedItem().toString();
            String selectedBrgy = brgySpinner.getSelectedItem().toString();
            String areaString = areaInput.getText().toString().trim();

            // Check for required fields
            if (firstName.isEmpty() || lastName.isEmpty() || contactNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || areaString.isEmpty()) {
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
            int farmerId = 1;  // Replace with actual farmerId

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
            // Add other crops as needed
            default:
                return -1;  // Unknown crop
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
            case "apopong":
                return 4;
            case "baluan":
                return 5;
            case "city heights":
                return 6;
            case "conel":
                return 7;
            case "dadiangas east":
                return 8;
            case "dadiangas west":
                return 9;
            case "dadiangas south":
                return 10;
            case "fatima":
                return 11;
            case "katangawan":
                return 12;
            case "ligaya":
                return 13;
            case "mabuhay":
                return 14;
            case "olympog":
                return 15;
            case "san jose":
                return 16;
            case "tinagacan":
                return 17;
            // Add other barangays as needed
            default:
                return -1;  // Unknown barangay
        }
    }

    private void register(int farmerId, int cropId, double area, String rsbsaNum, String password,
                                      String firstName, String middleName, String lastName,
                                      String contactNumber, int barangayId) {
        String url = "https://harvest.dermocura.net/PHP_API/register.php";

        JSONObject requestBody = new JSONObject();
        RequestQueue queue = Volley.newRequestQueue(this);

        try {
            requestBody.put("rsbsa_num", rsbsaNum);
            requestBody.put("password", password);
            requestBody.put("first_name", firstName);
            requestBody.put("middle_name", middleName);
            requestBody.put("last_name", lastName);
            requestBody.put("contact_number", contactNumber);
            requestBody.put("area", area);
            requestBody.put("crop_id", cropId);
            requestBody.put("barangay_id", barangayId);
            requestBody.put("role_id", 1); // This is always 1
        } catch (JSONException e) {
            Log.e("SignupActivity makeHTTPRequest", e.toString());
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                this::onRequestSuccess,
                this::onRequestError
        );

        Log.i("SignupActivity makeHTTPRequest", requestBody.toString());
        queue.add(request);
    }

    private void onRequestSuccess(JSONObject response) {
        try {
            boolean success = response.getBoolean("success");
            String message = response.getString("message");

            if (success) {
                Log.d("SignupActivity" + " onRequestSuccess", "Message Response: " + message);
                Log.d("SignupActivity" + " onRequestSuccess", "JSON Received: " + response);
                Toast.makeText(this, "Planting date scheduled successfully", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("SignupActivity" + " onRequestSuccess", "Message Response: " + message);
                Toast.makeText(this, "Failed to schedule planting date", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Log.e("SignupActivity" + " onRequestSuccess", String.valueOf(e));
            Log.e("SignupActivity" + " onRequestSuccess", "Error parsing JSON response");
        }
    }

    private void onRequestError(VolleyError error) {
        Log.e("SignupActivity" + " onRequestError", "Error Response: " + error.getMessage());
        Toast.makeText(this, "Error scheduling planting date", Toast.LENGTH_SHORT).show();
    }
}

