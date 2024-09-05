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
import android.view.MenuItem;
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
import com.example.app.ml.ModelUnquant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CameraActivity extends AppCompatActivity {

    // Existing variables

    TextView result, confidence, locationTextView;
    ImageView imageView;
    Button picture, analyzeButton;
    int imageSize = 224;
    Bitmap capturedImage;

    FusedLocationProviderClient fusedLocationClient;
    double latitude;
    double longitude;

    // Class-level variables
    private float[] confidences;  // Store confidence values
    private final String[] classes = {"Snail", "Weevil", "No Pest Detected"};  // Store class labels

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.darker_matcha));
        setContentView(R.layout.activity_camera);

        // Set the ActionBar background color
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.darker_matcha)));
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidence);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.btnTakePicture); // Updated to match "Take Picture" button
        analyzeButton = findViewById(R.id.btnAnalyze); // Updated to match "Analyze" button
        locationTextView = findViewById(R.id.location);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });

//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendDataToApi();
//            }
//        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });

        analyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (capturedImage != null) {
                    classifyImage(capturedImage);
                } else {
                    Toast.makeText(CameraActivity.this, "No image to analyze.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, 1);
        } else if (requestCode == 101 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLastLocation();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            int dimension = Math.min(image.getWidth(), image.getHeight());
            image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
            imageView.setImageBitmap(image);

            image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
            classifyImage(image);
            capturedImage = image;

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> locationTask = fusedLocationClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.d("Location", "Latitude: " + latitude + ", Longitude: " + longitude);
                    getAddressFromLocation(latitude, longitude);
                } else {
                    locationTextView.setText("Location not available");
                }
            }
        });
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                String addressText = "";
                if (address.getSubLocality() != null) {
                    addressText += address.getSubLocality() + ", ";
                }
                if (address.getLocality() != null) {
                    addressText += address.getLocality() + ", ";
                }
                if (address.getAdminArea() != null) {
                    addressText += address.getAdminArea() + ", ";
                }
                if (address.getCountryName() != null) {
                    addressText += address.getCountryName();
                }
                locationTextView.setText(addressText);
            } else {
                locationTextView.setText("No address found for location.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            locationTextView.setText("Error getting address.");
        }
    }

    public void classifyImage(Bitmap image) {
        try {
            ModelUnquant model = ModelUnquant.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            ModelUnquant.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();  // This line may need adjustment

            confidences = outputFeature0.getFloatArray();  // Store the confidences

            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            result.setText(classes[maxPos]);

            StringBuilder s = new StringBuilder();
            for (int i = 0; i < classes.length; i++) {
                s.append(String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100));
            }
            confidence.setText(s.toString());

            model.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error processing image.", Toast.LENGTH_SHORT).show();
        }
    }


    private void sendDataToApi() {
        if (capturedImage != null && result.getText() != null) {
            String pestType = result.getText().toString();
            String imageString = convertBitmapToBase64(capturedImage);

            String url = "https://your-api-endpoint.com/pest/";

            StringRequest request = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(CameraActivity.this, "Image sent to city agriculturist.", Toast.LENGTH_SHORT).show();
                            finish(); // Close the current activity
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(CameraActivity.this, "Error sending data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("pestType", pestType);
                    params.put("latitude", String.valueOf(latitude));
                    params.put("longitude", String.valueOf(longitude));
                    params.put("image", imageString);
                    return params;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(request);
        } else {
            Toast.makeText(this, "No image or result available to send.", Toast.LENGTH_SHORT).show();
        }
    }

    private String convertBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    private void sendImageToDatabase() {
        if (capturedImage != null) {
            String imageString = convertBitmapToBase64(capturedImage);
            String pestType = result.getText().toString();  // Get the pest type from the result TextView
            String address = locationTextView.getText().toString();  // Get address text

            // Build the confidence string
            if (confidences == null) {
                Toast.makeText(this, "No confidence data available.", Toast.LENGTH_SHORT).show();
                return;
            }
            StringBuilder confidenceStringBuilder = new StringBuilder();
            for (int i = 0; i < confidences.length; i++) {
                confidenceStringBuilder.append(classes[i]).append(": ").append(confidences[i] * 100).append("% ");
            }
            String confidenceString = confidenceStringBuilder.toString();

            String urlString = "http://192.168.5.109:5000/new_classes/";

            new Thread(() -> {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setDoOutput(true);

                    // Create the POST data string
                    String postData = "image=" + URLEncoder.encode(imageString, "UTF-8")
                            + "&pest_type=" + URLEncoder.encode(pestType, "UTF-8")
                            + "&latitude=" + URLEncoder.encode(String.valueOf(latitude), "UTF-8")
                            + "&longitude=" + URLEncoder.encode(String.valueOf(longitude), "UTF-8")
                            + "&address=" + URLEncoder.encode(address, "UTF-8")  // Add address to the POST data
                            + "&confidences=" + URLEncoder.encode(confidenceString, "UTF-8");

                    // Send the POST data
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(postData.getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();

                    // Check the response code
                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        runOnUiThread(() -> {
                            Toast.makeText(CameraActivity.this, "Image saved to database.", Toast.LENGTH_SHORT).show();
                        });
                    } else {
                        runOnUiThread(() -> {
                            Toast.makeText(CameraActivity.this, "Error saving image: " + responseCode, Toast.LENGTH_SHORT).show();
                        });
                    }
                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        Toast.makeText(CameraActivity.this, "Error saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();
        } else {
            Toast.makeText(this, "No image available to send.", Toast.LENGTH_SHORT).show();
        }
    }




    private void checkLocationServices() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gpsEnabled && !networkEnabled) {
            // Prompt user to enable location services
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