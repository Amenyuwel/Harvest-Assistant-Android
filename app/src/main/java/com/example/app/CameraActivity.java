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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
    private static final int GALLERY_REQUEST_CODE = 2; // New request code for gallery

    private TextView result, recommendationTextView, locationTextView;
    private ImageView imageView;
    private Button picture, sendButton, uploadImage; // Added uploadImage button

    private Bitmap capturedImage;
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude;
    private double longitude;

    // Class member variables for encoded image and address
    private String encodedImage;
    private String address;
    private int farmerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        // Set ActionBar color
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.darker_matcha)));
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Initialize UI elements
        // Initialize UI elements
        result = findViewById(R.id.result);
        recommendationTextView = findViewById(R.id.confidence);
        locationTextView = findViewById(R.id.location);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.btnTakePicture);
        sendButton = findViewById(R.id.btnAnalyze);
        uploadImage = findViewById(R.id.uploadImage); // Initialize new button

        // Initialize location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkLocationServices();

        // Retrieve the farmer_id from SharedPreferences
        int farmerID = SharedPreferenceManager.getInstance(this).getFarmerID();

        // Check if the farmerID was successfully retrieved
        if (farmerID == -1) {
            // Handle the case where no farmerID is found
            Toast.makeText(CameraActivity.this, "No farmer ID found. Please log in again.", Toast.LENGTH_LONG).show();
            // Optionally redirect the user to login or handle the error as needed
        }

        // Capture image button
        picture.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            }
        });

        // Upload image button
        uploadImage.setOnClickListener(view -> {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        });

        // Send image button
        sendButton.setOnClickListener(view -> {
            if (capturedImage != null) {
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
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK) { // Handle image selection from gallery
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
                locationTextView.setVisibility(View.VISIBLE); // Show message if location is null
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
                this.address = address.getAddressLine(0); // Save address to class variable
                locationTextView.setText("Location: " + this.address);
                locationTextView.setVisibility(View.VISIBLE); // Make it visible
                sendImageToPredict(capturedImage, latitude, longitude, this.address);
            } else {
                locationTextView.setText("No address found for location.");
                locationTextView.setVisibility(View.VISIBLE); // Show even if no address is found
            }
        } catch (IOException e) {
            e.printStackTrace();
            locationTextView.setText("Error getting address.");
            locationTextView.setVisibility(View.VISIBLE);
        }
    }

    private void sendImageToPredict(Bitmap image, double latitude, double longitude, String address) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT); // Save encoded image to class variable

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://desktop-3mj7q7f.tail98551e.ts.net/predict"; // Update with your Flask server URL

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                this::handleFlaskResponse,
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
                params.put("address", address);
                params.put("farmer_id", String.valueOf(farmerID));  // Add farmer_id to the request
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

        // Replace underscores with spaces and capitalize
        String formatted = pestType.replace('_', ' ').toLowerCase();
        String[] words = formatted.split(" ");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            if (capitalized.length() > 0) {
                capitalized.append(" "); // Add space before the next word
            }
            capitalized.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }

        return capitalized.toString();
    }

    private void handleFlaskResponse(String response) {
        Log.d("Response", response); // Log the response for debugging

        try {
            JSONObject jsonObject = new JSONObject(response);
            String pestType = jsonObject.getString("pest_type");
            String recommendation = jsonObject.getString("recommendation");

            // Format the pest type for display
            String formattedPestType = formatPestType(pestType);

            // Update the TextViews with the classification result
            result.setText("Pest Type: " + formattedPestType);
            recommendationTextView.setText("Recommendation: " + recommendation);

            // Make the TextViews visible
            result.setVisibility(View.VISIBLE);
            recommendationTextView.setVisibility(View.VISIBLE);

            // Send the data to your PHP server for storage
            sendPredictionToPhpServer(encodedImage, formattedPestType, latitude, longitude, address);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSONError", "Failed to parse JSON: " + e.getMessage());
        }
    }

    private void sendPredictionToPhpServer(String image, String pestType, double latitude, double longitude, String address) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://harvest.dermocura.net/retreive_prediction.php"; // Update with your PHP server URL

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> Log.d("PHP Response", response),
                error -> Log.e("Error", error.toString())) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("image", image);
                params.put("pest_type", pestType);
                params.put("latitude", String.valueOf(latitude));
                params.put("longitude", String.valueOf(longitude));
                params.put("address", address);
                params.put("farmer_id", String.valueOf(farmerID)); // Pass farmer_id to PHP server
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
}