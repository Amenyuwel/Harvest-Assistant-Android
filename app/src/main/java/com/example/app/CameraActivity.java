package com.example.app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class CameraActivity extends AppCompatActivity {

    private static final String TAG = "CameraActivity";
    private static final int REQUEST_CODE_PERMISSIONS = 10;
    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int PICK_IMAGE_REQUEST = 1;
    private PreviewView previewView;
    private ImageCapture imageCapture;


    private PestDetector pestDetector;
    private BoundingBoxOverlayView boundingBoxOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        previewView = findViewById(R.id.previewView);
        boundingBoxOverlay = findViewById(R.id.boundingBoxOverlayView);
        Button captureButton = findViewById(R.id.captureButton);
        Button selectImageButton = findViewById(R.id.selectImageButton);

        if (allPermissionsGranted()) {
            startCamera();
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

        captureButton.setOnClickListener(v -> takePhoto());

        selectImageButton.setOnClickListener(v -> selectImageFromGallery());
    }

    private boolean allPermissionsGranted() {
        for (String permission : REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();

        imageCapture = new ImageCapture.Builder().build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);
    }

    private void takePhoto() {
        if (imageCapture == null) {
            return;
        }

        imageCapture.takePicture(ContextCompat.getMainExecutor(this), new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                Bitmap bitmap = imageProxyToBitmap(image);
                image.close();
                processBitmap(bitmap);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                Log.e("CameraActivity", "Photo capture failed: " + exception.getMessage(), exception);
            }
        });
    }

    private Bitmap imageProxyToBitmap(ImageProxy image) {
        ImageProxy.PlaneProxy[] planes = image.getPlanes();
        ByteBuffer buffer = planes[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, null);
    }

    private void processBitmap(Bitmap bitmap) {
        if (pestDetector == null) {
            pestDetector = new PestDetector(this);
        }

        List<PestDetector.Detection> detections = pestDetector.classifyImage(bitmap);

        for (PestDetector.Detection detection : detections) {
            Log.d(TAG, "Detection: " + detection.label + ", Confidence: " + detection.confidence);
        }

        List<PestDetector.Detection> filteredDetections = new ArrayList<>();
        for (PestDetector.Detection detection : detections) {
            if (detection.confidence >= 0.5) {
                filteredDetections.add(new PestDetector.Detection(detection.label, detection.confidence, null));
            }
        }

        if (filteredDetections.isEmpty()) {
            Toast.makeText(this, "No Pest Detected", Toast.LENGTH_SHORT).show();
            boundingBoxOverlay.setDetections(new ArrayList<>());  // Clear previous detections
            return;
        }

        PestDetector.Detection highestConfidenceDetection = filteredDetections.get(0);
        for (PestDetector.Detection detection : filteredDetections) {
            if (detection.confidence > highestConfidenceDetection.confidence) {
                highestConfidenceDetection = detection;
            }
        }

        String message = "Detected " + highestConfidenceDetection.label + " with confidence " + highestConfidenceDetection.confidence;
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        boundingBoxOverlay.setDetections(filteredDetections);

        // Start ResultActivity with the detection data
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("pest_name", highestConfidenceDetection.label);
        intent.putExtra("confidence", highestConfidenceDetection.confidence);
        startActivity(intent);
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                processBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera();
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
