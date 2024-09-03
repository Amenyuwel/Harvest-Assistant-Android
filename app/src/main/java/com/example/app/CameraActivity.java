package com.example.app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.ml.ModelUnquant;

public class CameraActivity extends AppCompatActivity {

    TextView result, confidence, desc, pestrecom;
    ImageView imageView;
    Button picture, sendButton;
    int imageSize = 224;
    Bitmap capturedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidence);
        desc = findViewById(R.id.desc);
        pestrecom = findViewById(R.id.pestrecom);
        imageView = findViewById(R.id.imageView);
        picture = findViewById(R.id.button);
        sendButton = findViewById(R.id.button_send);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    // Request camera permission if we don't have it.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataToApi();
            }
        });
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
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"Snail", "Weevil", "No Pest Detected"};
            result.setText(classes[maxPos]);

            String s = "";
            for (int i = 0; i < classes.length; i++) {
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
            }
            confidence.setText(s);

            String identifiedPest = classes[maxPos];
            result.setText(identifiedPest);

            // Set description based on identified pest
            String description = "";
            String pesticides = "";
            switch (identifiedPest) {
                case "Snail":
                    description = "Snails are mollusks that can damage plants by eating leaves, stems, and fruits.";
                    pesticides = "Metaldehyde (e.g., Metarex), Iron Phosphate (e.g., Sluggo)";
                    break;
                case "Weevil":
                    description = "Weevils are beetles known for damaging stored grains and crops.";
                    pesticides = "Permethrin, Cypermethrin, Deltamethrin, Diatomaceous Earth";
                    break;
                case "No Pest Detected":
                    description = "No pest detected in the image.";
                    pesticides = "No pesticides recommended.";
                    break;
                default:
                    description = "Unknown pest.";
                    pesticides = "No specific pesticides recommended for this pest.";
                    break;
            }
            desc.setText(description);
            pestrecom.setText(pesticides);

            // Releases model resources if no longer used.
            model.close();

        } catch (IOException e) {
            // TODO Handle the exception
        }
    }

    private void sendDataToApi() {
        String identifiedPest = result.getText().toString();

        if (identifiedPest.equals("No Pest Detected") || capturedImage == null) {
            Toast.makeText(this, "No pest detected or image not available.", Toast.LENGTH_SHORT).show();
            return;
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        capturedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

        String url = "http://192.168.68.114/HarvestAssistantFinalII/api/pest_report.php"; // Replace with your actual API endpoint

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        Toast.makeText(CameraActivity.this, "Data sent successfully", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(CameraActivity.this, "Error sending data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pestName", identifiedPest);
                params.put("uploadPests", encodedImage);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");

                // Retrieve token from SharedPreferences
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                String token = sharedPreferences.getString("token", null);
                if (token != null) {
                    headers.put("Authorization", "Bearer " + token);
                }
                return headers;
            }
        };

        requestQueue.add(stringRequest);
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
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
