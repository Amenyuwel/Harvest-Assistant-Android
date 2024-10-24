package com.example.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CameraActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int LOCATION_REQUEST_CODE = 101;
    private static final int GALLERY_REQUEST_CODE = 2;

    private TextView result, recommendationTextView, locationTextView;
    private ImageView imageView;
    private Button picture, sendButton, uploadImage;

    private Bitmap capturedImage;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longitude;

    // Class member variables for encoded image and address
    private String encodedImage;
    private String address;
    private int farmerID; // Class-level variable for farmer ID
    private String severity; // Added severity here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Set the ActionBar background color
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the ActionBar background color
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.darker_matcha)));
            // Remove ActionBar Title
            actionBar.setTitle("");
            // Show the back button in action bar
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        // Initialize UI elements
        result = findViewById(R.id.result);
        recommendationTextView = findViewById(R.id.confidence);
        locationTextView = findViewById(R.id.location);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.btnTakePicture);
        sendButton = findViewById(R.id.btnAnalyze);
        uploadImage = findViewById(R.id.uploadImage);
        ImageView showDialogButton = findViewById(R.id.getRate);


        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkLocationServices();

        // Retrieve the farmer_id from SharedPreferences
        farmerID = SharedPreferenceManager.getInstance(this).getFarmerID(); // Update to class-level variable

        // Check if the farmerID is valid
        if (farmerID <= 0) {
            Toast.makeText(CameraActivity.this, "No valid farmer ID found. Please log in again.", Toast.LENGTH_LONG).show();
            finish(); // Close activity or redirect to login
            return;
        }

        // Upload image button
        uploadImage.setOnClickListener(view -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        });

        // Capture image button
        picture.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            }
        });
        showDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(); // Call the method to show the dialog
            }
        });
    }

    private void showDialog() {
        // Inflate the dialog layout
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_radio_group, null);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(CameraActivity.this);
        builder.setTitle("SELECT CROP DAMAGE SEVERITY")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroup);
                    int selectedId = radioGroup.getCheckedRadioButtonId();

                    if (selectedId != -1) {
                        RadioButton selectedRadioButton = dialogView.findViewById(selectedId);
                        severity = selectedRadioButton.getText().toString();
                        Toast.makeText(CameraActivity.this, "Selected: " + severity, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CameraActivity.this, "No selection made", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        // Show the dialog
        builder.create().show();


        recommendationTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to open a new activity
                Intent intent = new Intent(CameraActivity.this, PestReal.class);
                startActivity(intent); // Start the new activity
            }
        });


        // Send image button
        sendButton.setOnClickListener(view -> {
            if (capturedImage != null) {
                // Check if severity is selected
                if (severity == null || severity.isEmpty()) {
                    Toast.makeText(CameraActivity.this, "Input pest damage severity", Toast.LENGTH_SHORT).show();
                    return; // Prevent further execution
                }

                // Check for location permissions and proceed
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLastLocation();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
                }
            } else {
                Toast.makeText(CameraActivity.this, "No image captured", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                if (image != null) {
                    int dimension = Math.min(image.getWidth(), image.getHeight());
                    image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                    imageView.setImageBitmap(image);
                    capturedImage = Bitmap.createScaledBitmap(image, 224, 224, false);
                }
            }
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    Bitmap image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    imageView.setImageBitmap(image);
                    capturedImage = Bitmap.createScaledBitmap(image, 224, 224, false);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> locationTask = fusedLocationClient.getLastLocation();
        locationTask.addOnSuccessListener(location -> {
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                getAddressFromLocation(latitude, longitude);
            } else {
                locationTextView.setText("Location not available");
                locationTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        if (!Geocoder.isPresent()) {
            locationTextView.setText("Geocoder not available.");
            locationTextView.setVisibility(View.VISIBLE);
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                this.address = address.getAddressLine(0);
                Log.d("Address Debug", "Fetched Address: " + this.address); // Log the fetched address
                locationTextView.setText("Location: " + this.address);
                locationTextView.setVisibility(View.VISIBLE);
                sendImageToPredict(capturedImage, latitude, longitude, this.address); // Send address
            } else {
                locationTextView.setText("No address found for location.");
                locationTextView.setVisibility(View.VISIBLE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            locationTextView.setText("Error getting address.");
            locationTextView.setVisibility(View.VISIBLE);
        }
    }

    // Sending image and other details to PHP
    private void sendImageToPredict(Bitmap image, double latitude, double longitude, String address) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://desktop-3mj7q7f.tail98551e.ts.net/predict"; // Update with your PHP URL

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> handleFlaskResponse(response, image),
                error -> {
                    Log.e("Error", error.toString());
                    Toast.makeText(CameraActivity.this, "Failed to send prediction", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("image", encodedImage);
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                params.put("address", address); // Pass the address
                params.put("farmer_id", String.valueOf(farmerID)); // Add farmer ID to the request
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        queue.add(stringRequest);
    }

    private String formatPestType(String pestType) {
        if (pestType == null || pestType.isEmpty()) {
            return pestType; // Return as is if null or empty
        }

        String formatted = pestType.replace('_', ' ').toLowerCase();
        String[] words = formatted.split(" ");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            if (capitalized.length() > 0) {
                capitalized.append(" ");
            }
            capitalized.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }

        return capitalized.toString();
    }

    private void handleFlaskResponse(String response, Bitmap imageBitmap) {
        Log.d("Response", response); // Log the response for debugging

        try {
            JSONObject jsonObject = new JSONObject(response);
            String pestType = jsonObject.getString("pest_type");

            String formattedPestType = formatPestType(pestType);

            result.setText(" " + formattedPestType);

            result.setVisibility(View.VISIBLE);
            recommendationTextView.setVisibility(View.VISIBLE);

            // Send the prediction to the PHP server
            sendPredictionToPhpServer(encodedImage, formattedPestType, latitude, longitude, address, farmerID);
        } catch (JSONException e) {
            Log.e("JSONError", "Failed to parse JSON: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void sendPredictionToPhpServer(String encodedImage, String pestType, double latitude, double longitude, String address, int farmerID) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://harvest.dermocura.net/PHP_API/receive_prediction.php";

        Log.d("Sending to PHP", "Encoded Image Length: " + (encodedImage != null ? encodedImage.length() : "null"));
        Log.d("Sending to PHP", "Pest Type: " + pestType);
        Log.d("Sending to PHP", "Latitude: " + latitude);
        Log.d("Sending to PHP", "Longitude: " + longitude);
        Log.d("Sending to PHP", "Address: " + address); // Log address to ensure it's not null
        Log.d("Sending to PHP", "Farmer ID: " + farmerID);
        Log.d("Sending to PHP", "Severity: " + severity); // Log severity for debugging
        //time and date

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    Log.d("PHP Response", response); // Log the PHP response for debugging

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        String message = jsonResponse.getString("message");

                        if (success) {
                            Toast.makeText(this, "Pest report added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("PHP Insert Error", "Error message from PHP: " + message);
                            Toast.makeText(this, "Error: " + message, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Log.e("JSON Error", "Failed to parse JSON: " + e.getMessage());
                        e.printStackTrace();
                    }
                },
                error -> {
                    Log.e("Volley Error", "Failed to send data: " + error.getMessage());
                    Toast.makeText(this, "Failed to send data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("image", encodedImage);
                params.put("pest_type", pestType);
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                params.put("address", address); // Address being sent to PHP
                params.put("farmer_id", String.valueOf(farmerID));
                params.put("severity", severity); // Add severity to parameters
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }
        };

        queue.add(stringRequest);
    }


    // Method to encode Bitmap to Base64
    private String encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void checkLocationServices() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gpsEnabled && !networkEnabled) {
            new AlertDialog.Builder(this)
                    .setTitle("Enable Location Services")
                    .setMessage("Location services are required for this app. Please enable them.")
                    .setPositiveButton("OK", (dialog, which) -> {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}