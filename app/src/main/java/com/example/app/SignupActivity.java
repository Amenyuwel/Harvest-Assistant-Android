package com.example.app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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
    private static final String TAG = "signing";

    private EditText etFirstName, etMiddleName, etLastName, etExtname, etContact, etArea, etPurok, etStreet, etCity, etProv, etRegion, etpobMun, etRel, etUsername, etPassword, etConfirmPassword;
    private Spinner cropSpinner, brgySpinner;
    private RadioGroup rgSex;
    private Button btnSignup;
    private ImageButton calendarBackButton;
    private Dialog dialog;
    private Button btnDialog, btnDPicker;
    private TextView dateText;
    private String birthdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI components
        etFirstName = findViewById(R.id.etFirstname);
        etMiddleName = findViewById(R.id.middleNameEditText);
        etLastName = findViewById(R.id.lastNameEditText);
        etExtname = findViewById(R.id.etExtname);
        etContact = findViewById(R.id.etContact);
        etArea = findViewById(R.id.etArea);
        etPurok = findViewById(R.id.etPurok);
        etStreet = findViewById(R.id.etStreet);
        etCity = findViewById(R.id.etCity);
        etProv = findViewById(R.id.etProv);
        etRegion = findViewById(R.id.etRegion);
        etpobMun = findViewById(R.id.etpobMun);
        etRel = findViewById(R.id.etRel);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        rgSex = findViewById(R.id.rgSex);
        cropSpinner = findViewById(R.id.cropSpinner);
        brgySpinner = findViewById(R.id.brgySpinner);
        btnSignup = findViewById(R.id.btnSignup);
        btnDPicker = findViewById(R.id.btnDPicker);
        dateText = findViewById(R.id.dateText);
        calendarBackButton = findViewById(R.id.calendarBackButton);

        // Back button listener
        calendarBackButton.setOnClickListener(view -> onBackPressed());

        // Date picker button listener
        btnDPicker.setOnClickListener(view -> openDatePickerDialog());

        // Set up spinners
        setupCropSpinner();
        setupBrgySpinner();

        // Set up registration dialog
        dialog = new Dialog(SignupActivity.this);
        dialog.setContentView(R.layout.register_dialog_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        dialog.setCancelable(false);

        btnDialog = dialog.findViewById(R.id.btnDialog);
        btnDialog.setOnClickListener(view -> {
            // Dismiss the dialog once the user clicks "Yes"
            dialog.dismiss();

            // Navigate to login activity
            Intent i = new Intent(SignupActivity.this, Login.class);
            startActivity(i);
        });

        // Set up the click listener for btnSignup
        btnSignup.setOnClickListener(v -> {
            // Register user when "Sign Up" is clicked
            registerUser();
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
        final String extName = etExtname.getText().toString().trim();
        final String contact = etContact.getText().toString().trim();
        final String area = etArea.getText().toString().trim();
        final String purok = etPurok.getText().toString().trim();
        final String street = etStreet.getText().toString().trim();
        final String city = etCity.getText().toString().trim();
        final String province = etProv.getText().toString().trim();
        final String region = etRegion.getText().toString().trim();
        final String pobMun = etpobMun.getText().toString().trim();
        final String religion = etRel.getText().toString().trim();
        final String username = etUsername.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String confirmPassword = etConfirmPassword.getText().toString().trim();
        final String crop = cropSpinner.getSelectedItem().toString();
        final String barangay = brgySpinner.getSelectedItem().toString();
        final int selectedSexId = rgSex.getCheckedRadioButtonId();
        final String sex = selectedSexId == -1 ? "" : ((RadioButton) findViewById(selectedSexId)).getText().toString();

        // Validate inputs
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (firstName.isEmpty() || contact.isEmpty() || area.isEmpty() || sex.isEmpty() || birthdate == null || birthdate.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Log the input values
        Log.d(TAG, "First Name: " + firstName);
        Log.d(TAG, "Middle Name: " + middleName);
        Log.d(TAG, "Last Name: " + lastName);
        Log.d(TAG, "Contact: " + contact);
        Log.d(TAG, "Area: " + area);
        Log.d(TAG, "Crop: " + crop);
        Log.d(TAG, "Barangay: " + barangay);
        Log.d(TAG, "Sex: " + sex);
        Log.d(TAG, "Birthdate: " + birthdate);

        // API endpoint URL
        String url = "https://harvest.dermocura.net/PHP_API/new_register.php";

        // Create a POST request using Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
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
                            // Show registration dialog
                            dialog.show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "Error parsing response: " + e.getMessage());
                        Toast.makeText(SignupActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e(TAG, "Volley Error: " + (error.getMessage() != null ? error.getMessage() : "Unknown error"));
                    Toast.makeText(SignupActivity.this, "Registration failed. Please check your network and try again.", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("first_name", firstName);
                params.put("middle_name", middleName);
                params.put("last_name", lastName);
                params.put("ext_name", extName);
                params.put("contact_number", contact);
                params.put("area", area);
                params.put("purok", purok);
                params.put("street", street);
                params.put("city", city);
                params.put("province", province);
                params.put("region", region);
                params.put("birthdate", birthdate);
                params.put("place_of_birth", pobMun);
                params.put("religion", religion);
                params.put("username", username);
                params.put("password", password);
                params.put("crop_id", String.valueOf(getCropId(crop)));
                params.put("barangay_id", String.valueOf(getBrgyId(barangay)));
                params.put("sex", sex);
                params.put("role_id", "1");  // Ensure role_id is sent as "1" for farmers
                return params;
            }
        };

        // Add request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private int getCropId(String cropName) {
        switch (cropName.toLowerCase()) {
            case "corn":
                return 1;
            case "rice":
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

    private void openDatePickerDialog() {
        DatePickerDialog dialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
            birthdate = String.format("%d-%02d-%02d", year, month + 1, day);
            dateText.setText(birthdate);
        }, 2024, 0, 30);

        dialog.show();
    }
}
