package com.example.app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "HAHA";

    private EditText etFirstName, etMiddleName, etLastName, etContact, etArea, etRsbsaNum, etPassword, etConfirmPassword;
    private Spinner cropSpinner, brgySpinner;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        etFirstName = findViewById(R.id.etFirstName);
        etMiddleName = findViewById(R.id.etMiddleName);
        etLastName = findViewById(R.id.etLastName);
        etContact = findViewById(R.id.etContact);
        etArea = findViewById(R.id.etArea);
        etRsbsaNum = findViewById(R.id.etRsbsaNum);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        cropSpinner = findViewById(R.id.cropSpinner);
        brgySpinner = findViewById(R.id.brgySpinner);
        btnSignup = findViewById(R.id.btnSignup);

        // Set up spinners
        setupCropSpinner();
        setupBrgySpinner();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void setupCropSpinner() {
        // Set the crop spinner with values from strings.xml
        ArrayAdapter<CharSequence> cropAdapter = ArrayAdapter.createFromResource(this,
                R.array.crop_array, android.R.layout.simple_spinner_item);
        cropAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cropSpinner.setAdapter(cropAdapter);
    }

    private void setupBrgySpinner() {
        // Set the barangay spinner with values from strings.xml
        ArrayAdapter<CharSequence> brgyAdapter = ArrayAdapter.createFromResource(this,
                R.array.brgy_array, android.R.layout.simple_spinner_item);
        brgyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brgySpinner.setAdapter(brgyAdapter);
    }

    private void registerUser() {
        final String firstName = etFirstName.getText().toString().trim();
        final String middleName = etMiddleName.getText().toString().trim();
        final String lastName = etLastName.getText().toString().trim();
        final String contact = etContact.getText().toString().trim();
        final String area = etArea.getText().toString().trim();
        final String rsbsaNum = etRsbsaNum.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String confirmPassword = etConfirmPassword.getText().toString().trim();
        final String crop = cropSpinner.getSelectedItem().toString();
        final String barangay = brgySpinner.getSelectedItem().toString();

        // Validate inputs (for example, password confirmation and empty fields)
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (firstName.isEmpty() || contact.isEmpty() || area.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log the input values
        Log.d(TAG, "First Name: " + firstName);
        Log.d(TAG, "Middle Name: " + middleName);
        Log.d(TAG, "Last Name: " + lastName);
        Log.d(TAG, "Contact: " + contact);
        Log.d(TAG, "Area: " + area);
        Log.d(TAG, "RSBSA Number: " + rsbsaNum);
        Log.d(TAG, "Password: " + password);
        Log.d(TAG, "Crop: " + crop);
        Log.d(TAG, "Barangay: " + barangay);

        // API endpoint URL
        String url = "https://harvest.dermocura.net/PHP_API/register.php";

        // Create a POST request using Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Log the raw response
                            Log.d(TAG, "Response: " + response);

                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            String message = jsonObject.getString("message");

                            // Log the parsed response
                            Log.d(TAG, "Success: " + success);
                            Log.d(TAG, "Message: " + message);

                            Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();

                            if (success) {
                                // Redirect to login or another activity if needed
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Error parsing response: " + e.getMessage());
                            Toast.makeText(SignupActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley Error: " + error.getMessage());
                        Toast.makeText(SignupActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", firstName);
                params.put("middle_name", middleName);
                params.put("last_name", lastName);
                params.put("contact_number", contact);  // Use "contact_number" instead of "contact"
                params.put("area", area);
                params.put("rsbsa_num", rsbsaNum);
                params.put("password", password);
                params.put("crop_id", String.valueOf(getCropId(crop)));
                params.put("barangay_id", String.valueOf(getBrgyId(barangay)));
                params.put("role_id", "1");  // Ensure role_id is sent as "1" for farmers
                return params;


            }

        };

        // Add request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest);
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
            default:
                return -1;  // Unknown barangay
        }
    }
}