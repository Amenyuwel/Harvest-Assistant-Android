package com.example.app;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity1 extends AppCompatActivity {
    private static final String TAG = "HAHA";
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 100;

    private TextInputEditText etFirstName, etMiddleName, etLastName, etContact, etArea, etRsbsaNum, etPassword, etConfirmPassword;
    private Spinner cropSpinner, brgySpinner;
    private Button btnSignup, btnSelectImage;
    private ImageView ivSelectedImage;
    private ImageButton calendarBackButton;

    private String encodedImage = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);

        // Initialize views
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
        btnSelectImage = findViewById(R.id.btnSelectImage);
        ivSelectedImage = findViewById(R.id.ivSelectedImage);
        calendarBackButton = findViewById(R.id.calendarBackButton);

        // Set up spinners
        setupCropSpinner();
        setupBrgySpinner();

        // Set up image selection button
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionAndOpenGallery();
            }
        });

        // Set up signup button
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        // Handle back button click
        calendarBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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

    private void checkPermissionAndOpenGallery() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        } else {
            // Permission already granted
            openGallery();
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied to access storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Handle image selection result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                // Get bitmap from URI
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                // Display the image in ImageView
                ivSelectedImage.setImageBitmap(bitmap);
                // Encode image to Base64
                encodedImage = encodeImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to select image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Convert Bitmap to Base64 string
    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // Compress the image to reduce size if necessary
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        // Encode to Base64
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
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

        // Validate inputs
        if (firstName.isEmpty() || contact.isEmpty() || area.isEmpty()
                || rsbsaNum.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (encodedImage.isEmpty()) {
            Toast.makeText(this, "Please select an image for validation", Toast.LENGTH_SHORT).show();
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
        Log.d(TAG, "Encoded Image: " + encodedImage.substring(0, Math.min(encodedImage.length(), 50)) + "..."); // Log first 50 chars

        // API endpoint URL
        String url = "https://harvest.dermocura.net/PHP_API/register1.php";

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

                            Toast.makeText(SignupActivity1.this, message, Toast.LENGTH_SHORT).show();

                            if (success) {
                                // Redirect to login or another activity if needed
                                Intent intent = new Intent(SignupActivity1.this, Login.class);
                                startActivity(intent);
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "Error parsing response: " + e.getMessage());
                            Toast.makeText(SignupActivity1.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley Error: " + error.getMessage());
                        Toast.makeText(SignupActivity1.this, "Registration failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("image", encodedImage); // Add the encoded image
                return params;
            }
        };

        // Set a timeout or retry policy if necessary
        // stringRequest.setRetryPolicy(new DefaultRetryPolicy(
        //        5000,
        //        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        //        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Add request to the RequestQueue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private int getCropId(String cropName) {
        switch (cropName.toLowerCase()) {
            case "corn":
                return 1;
            case "rice":
                return 2;
            // Add more crops as needed
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
